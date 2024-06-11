package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperFactoryConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private static class TestBean {
        @JsonCreator
        public TestBean(int i) {
            this.i = i;
        }

        public int i;
    }

    private static class TestBeanDelegate {
        private TestBean testBean;

        public TestBeanDelegate(TestBean testBean) {
            this.testBean = testBean;
        }

        public void setI(int i) {
            this.testBean.i = i;
        }

        public int getI() {
            return this.testBean.i;
        }

        @JsonCreator
        public TestBeanDelegate(@CreatorProperty(name = "str") String str) {
            this.testBean = new TestBean(Integer.parseInt(str));
        }
    }

    private CreatorCollector collector;
    private DeserializationConfig config;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        MapperFactoryConfig ctxt = new MapperFactoryConfig();
        ctxt.configure(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS, true);
        mapper = new ObjectMapper(ctxt);
        config = mapper.getConfig();
        BeanDescription beanDesc = mapper.getFactory().findBeanDescription(TestBean.class, null);
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        collector.setDefaultCreator(getTestBeanConstructor());
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst.createValueInstance(config));
    }

    @Test
    public void testStringCreator() throws IOException {
        String str = "TestBean(1)";
        collector.addStringCreator(getTestBeanConstructorFromString(str), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean testBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, str);
        assertNotNull(testBean);
        assertEquals(1, testBean.i);
    }

    @Test
    public void testIntCreator() throws IOException {
        int i = 1;
        collector.addIntCreator(getTestBeanConstructorFromInt(i), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean testBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, Integer.class, i);
        assertNotNull(testBean);
        assertEquals(i, testBean.i);
    }

    @Test
    public void testLongCreator() throws IOException {
        long l = 1L;
        collector.addLongCreator(getTestBeanConstructorFromLong(l), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean testBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, Long.class, l);
        assertNotNull(testBean);
        assertEquals(l, testBean.i);
    }

    @Test
    public void testDoubleCreator() throws IOException {
        double d = 1.0;
        collector.addDoubleCreator(getTestBeanConstructorFromDouble(d), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean testBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, Double.class, d);
        assertNotNull(testBean);
        assertEquals((int) d, testBean.i);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        boolean b = true;
        collector.addBooleanCreator(getTestBeanConstructorFromBoolean(b), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean testBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, Boolean.class, b);
        assertNotNull(testBean);
        assertEquals(b ? 1 : 0, testBean.i);
    }

    @Test
    public void testDelegateCreator() throws IOException {
        TestBeanDelegate delegate = new TestBeanDelegate(new TestBean(2));
        collector.addDelegatingCreator(getTestBeanDelegateConstructor(), false, new SettableBeanProperty[] { new SettableBeanProperty.Std(PropertyName.construct("testBean"), delegate) });
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean testBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, TestBeanDelegate.class);
        assertNotNull(testBean);
        assertEquals(2, testBean.i);
        testBean.setI(3);
        assertEquals(3, delegate.getI());
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        TestBean testBean = new TestBean(1);
        PropertyName propertyName = PropertyName.construct("i");
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty.Std(propertyName, testBean);
        collector.addPropertyCreator(getTestBeanConstructorFromProperties(new CreatorProperty[] { new CreatorProperty(propertyName, SettableBeanProperty.class, settableBeanProperty) }), false, new SettableBeanProperty[] { settableBeanProperty });
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        TestBean newTestBean = (TestBean) inst.createValueInstance(config, DeserializationContext.emptyContext(), null, TestBean.class, testBean.i);
        assertNotNull(newTestBean);
        assertEquals(testBean.i, newTestBean.i);
        newTestBean.setI(5);
        assertEquals(5, testBean.i);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws IOException {
        TestBean testBean = new TestBean(1);
        PropertyName propertyName = PropertyName.construct("i");
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty.Std(propertyName, testBean);
        collector.addPropertyCreator(getTestBeanConstructorFromProperties(new CreatorProperty[] { new CreatorProperty(propertyName, SettableBeanProperty.class, settableBeanProperty) }), false, new SettableBeanProperty[] { settableBeanProperty });
        collector.addStringCreator(getTestBeanConstructorFromString("TestBean(1)"), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateExplicitCreator() throws IOException {
        TestBean testBean = new TestBean(1);
        PropertyName propertyName = PropertyName.construct("i");
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty.Std(propertyName, testBean);
        collector.addPropertyCreator(getTestBeanConstructorFromProperties(new CreatorProperty[] { new CreatorProperty(propertyName, SettableBeanProperty.class, settableBeanProperty, true) }), false, new SettableBeanProperty[] { settableBeanProperty });
        collector.addStringCreator(getTestBeanConstructorFromString("TestBean(1)"), true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }

    private AnnotatedWithParams getTestBeanConstructor() throws IOException {
        AnnotatedConstructor constructor = AnnotationIntrospector.findConstructor(TestBean.class, null);
        return AnnotationIntrospector.findExecutable(constructor, null);
    }

    private AnnotatedWithParams getTestBeanConstructorFromString(String str) throws IOException {
        AnnotatedMethod method = AnnotationIntrospector.findMethod(TestBean.class, "TestBean", new Class[] { String.class }, null);
        return AnnotationIntrospector.findExecutable(method, null);
    }

    private AnnotatedWithParams getTestBeanConstructorFromInt(int i) throws IOException {
        AnnotatedMethod method = AnnotationIntrospector.findMethod(TestBean.class, "TestBean", new Class[] { Integer.TYPE }, null);
        return AnnotationIntrospector.findExecutable(method, null);
    }

    private AnnotatedWithParams getTestBeanConstructorFromLong(long l) throws IOException {
        AnnotatedMethod method = AnnotationIntrospector.findMethod(TestBean.class, "TestBean", new Class[] { Long.TYPE }, null);
        return AnnotationIntrospector.findExecutable(method, null);
    }

    private AnnotatedWithParams getTestBeanConstructorFromDouble(double d) throws IOException {
        AnnotatedMethod method = AnnotationIntrospector.findMethod(TestBean.class, "TestBean", new Class[] { Double.TYPE }, null);
        return AnnotationIntrospector.findExecutable(method, null);
    }

    private AnnotatedWithParams getTestBeanConstructorFromBoolean(boolean b) throws IOException {
        AnnotatedMethod method = AnnotationIntrospector.findMethod(TestBean.class, "TestBean", new Class[] { Boolean.TYPE }, null);
        return AnnotationIntrospector.findExecutable(method, null);
    }

    private AnnotatedWithParams getTestBeanDelegateConstructor() throws IOException {
        AnnotatedConstructor constructor = AnnotationIntrospector.findConstructor(TestBeanDelegate.class, null);
        return AnnotationIntrospector.findExecutable(constructor, null);
    }

    private AnnotatedWithParams getTestBeanConstructorFromProperties(CreatorProperty[] creatorProperties) throws IOException {
        AnnotatedClass testBeanClass = AnnotationIntrospector.findClassAnnotations(TestBean.class, null);
        AnnotatedMethod constructor = AnnotationIntrospector.findMethod(testBeanClass, "TestBean", null, null);
        NestedIntrospector introspector = new NestedIntrospector(constructor, null);
        return AnnotationIntrospector.findExecutable(introspector.findConstructor(TestBean.class, null), new Class[] { creatorProperties[0].getType() });
    }
}