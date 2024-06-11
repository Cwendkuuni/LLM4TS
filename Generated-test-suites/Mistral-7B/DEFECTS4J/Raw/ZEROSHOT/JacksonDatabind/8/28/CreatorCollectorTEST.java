package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedName;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector.Vanilla;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

	@Test
	public void testCreatorCollector() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestBean.class);
		CreatorCollector collector = new CreatorCollector(beanDesc, true);

		TestBean testBean = new TestBean();
		AnnotatedConstructor defaultConstructor = testBean.getClass().getDeclaredConstructor();
		collector.setDefaultCreator(ValueIntrospector.findDefaultCreator(defaultConstructor));

		AnnotatedMethod stringCreator = TestBean.class.getDeclaredMethod("StringCreator");
		collector.addStringCreator(ValueIntrospector.findMethodValueInjector(stringCreator), false);

		AnnotatedMethod intCreator = TestBean.class.getDeclaredMethod("IntCreator");
		collector.addIntCreator(ValueIntrospector.findMethodValueInjector(intCreator), false);

		AnnotatedMethod longCreator = TestBean.class.getDeclaredMethod("LongCreator");
		collector.addLongCreator(ValueIntrospector.findMethodValueInjector(longCreator), false);

		AnnotatedMethod doubleCreator = TestBean.class.getDeclaredMethod("DoubleCreator");
		collector.addDoubleCreator(ValueIntrospector.findMethodValueInjector(doubleCreator), false);

		AnnotatedMethod booleanCreator = TestBean.class.getDeclaredMethod("BooleanCreator");
		collector.addBooleanCreator(ValueIntrospector.findMethodValueInjector(booleanCreator), false);

		AnnotatedMethod delegateCreator = TestBean.class.getDeclaredMethod("DelegateCreator");
		CreatorProperty[] delegateArgs = { new CreatorProperty(new PropertyName("arg1"), createValue("arg1Value")) };
		collector.addDelegatingCreator(ValueIntrospector.findMethodValueInjector(delegateCreator), false, delegateArgs);

		AnnotatedMethod propertyCreator = TestBean.class.getDeclaredMethod("PropertyCreator");
		CreatorProperty[] propertyArgs = { new CreatorProperty(new PropertyName("arg1"), createValue("arg1Value")),
				new CreatorProperty(new PropertyName("arg2"), createValue("arg2Value")) };
		collector.addPropertyCreator(ValueIntrospector.findMethodValueInjector(propertyCreator), false, propertyArgs);

		AnnotatedField incompleteField = TestBean.class.getDeclaredField("incompleteField");
		collector.addIncompeteParameter(ValueIntrospector.findFieldValueInjector(incompleteField));

		ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());

		TestBean testBean2 = inst.createValueInstance(null, Map.class);
		assertEquals(testBean.hashCode(), testBean2.hashCode());
		assertEquals(testBean.toString(), testBean2.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateCreator() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestBean.class);
		CreatorCollector collector = new CreatorCollector(beanDesc, true);

		AnnotatedMethod stringCreator = TestBean.class.getDeclaredMethod("StringCreator");
		collector.addStringCreator(ValueIntrospector.findMethodValueInjector(stringCreator), false);

		AnnotatedMethod stringCreator2 = TestBean.class.getDeclaredMethod("StringCreator2");
		collector.addStringCreator(ValueIntrospector.findMethodValueInjector(stringCreator2), false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExplicitCreatorAndProperty() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestBean.class);
		CreatorCollector collector = new CreatorCollector(beanDesc, true);

		AnnotatedMethod stringCreator = TestBean.class.getDeclaredMethod("StringCreator");
		collector.addStringCreator(ValueIntrospector.findMethodValueInjector(stringCreator), true);

		AnnotatedMethod propertyCreator = TestBean.class.getDeclaredMethod("PropertyCreator");
		CollectorProperty[] propertyArgs = { new CollectorProperty(new PropertyName("arg1"), createValue("arg1Value")),
				new CollectorProperty(new PropertyName("arg2"), createValue("arg2Value")) };
		collector.addPropertyCreator(ValueIntrospector.findMethodValueInjector(propertyCreator), true, propertyArgs);
	}

	@Test
	public void testCanFixAccess() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BeanDescription beanDesc = mapper.getTypeFactory().constructType(TestBeanWithPrivateConstructor.class);
		CreatorCollector collector = new CreatorCollector(beanDesc, true);
		ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
		TestBeanWithPrivateConstructor testBean = inst.createValueInstance(null, Map.class);
		assertEquals(TestBeanWithPrivateConstructor.class.getDeclaredConstructor().newInstance(), testBean);
	}

	@Test
	public void testVanillaCreator() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BeanDescription beanDesc = mapper.getTypeFactory().constructType(Map.class);
		CreatorCollector collector = new CreatorCollector(beanDesc, true);
		ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
		Map<String, Object> map = inst.createValueInstance(null, Map.class);
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	private Object createValue(Object value) {
		return new Object() {
			@JsonProperty
			public String value() {
				return value.toString();
			}
		};
	}

	private static class TestBean {
		private String arg1;
		private int arg2;
		private long arg3;
		private double arg4;
		private boolean arg5;
		private TestBean delegate;

		@JsonCreator
		public TestBean(String arg1, int arg2, long arg3, double arg4, boolean arg5, TestBean delegate) {
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
			this.arg5 = arg5;
			this.delegate = delegate;
		}

		@JsonProperty
		public String arg1() {
			return arg1;
		}

		@JsonProperty
		public int arg2() {
			return arg2;
		}

		@JsonProperty
		public long arg3() {
			return arg3;
		}

		@JsonProperty
		public double arg4() {
			return arg4;
		}

		@JsonProperty
		public boolean arg5() {
			return arg5;
		}

		@JsonProperty
		public TestBean delegate() {
			return delegate;
		}

		public void StringCreator() {
			this.arg1 = "string";
		}

		public void IntCreator(int i) {
			this.arg2 = i;
		}

		public void LongCreator(long l) {
			this.arg3 = l;
		}

		public void DoubleCreator(double d) {
			this.arg4 = d;
		}

		public void BooleanCreator(boolean b) {
			this.arg5 = b;
		}

		public void DelegateCreator(String arg1, TestBean arg2) {
			this.arg1 = arg1;
			this.delegate = arg2;
		}

		public void PropertyCreator(String arg1, String arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		private String incompleteField;
	}

	private static class TestBeanWithPrivateConstructor {
		private String arg1;
		private int arg2;

		@JsonCreator
		public TestBeanWithPrivateConstructor(@JsonProperty("arg1") String arg1, @JsonProperty("arg2") int arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		private TestBeanWithPrivateConstructor() {
		}

		@JsonProperty
		public String arg1() {
			return arg1;
		}

		@JsonProperty
		public int arg2() {
			return arg2;
		}
	}

	private static class CollectorProperty extends CreatorProperty {
		private final Object value;

		public CollectorProperty(PropertyName name, Object value) {
			super(name);
			this.value = value;
		}

		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public JavaType getType(DeserializationConfig ctxt, BeanPropertyDefinition prop) throws IOException {
			return ctxt.constructType(new TypeReference<Object>() {}.getType());
		}
	}
}