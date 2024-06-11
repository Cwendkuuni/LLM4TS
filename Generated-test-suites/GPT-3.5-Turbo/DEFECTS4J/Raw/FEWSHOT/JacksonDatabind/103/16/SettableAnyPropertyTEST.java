package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

	private SettableAnyProperty settableAnyProperty;

	@Before
	public void setup() {
		BeanProperty property = null;
		AnnotatedMember setter = null;
		JavaType type = null;
		KeyDeserializer keyDeser = null;
		JsonDeserializer<Object> valueDeser = null;
		TypeDeserializer typeDeser = null;

		settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
	}

	@Test
	public void testGetAndSetProperty() {
		BeanProperty property = null;

		settableAnyProperty.setProperty(property);

		assertEquals(property, settableAnyProperty.getProperty());
	}

	@Test
	public void testHasValueDeserializer() {
		assertFalse(settableAnyProperty.hasValueDeserializer());

		JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {
			@Override
			public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return null;
			}
		};

		settableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);

		assertTrue(settableAnyProperty.hasValueDeserializer());
	}

	@Test
	public void testGetType() {
		JavaType type = null;

		settableAnyProperty.setType(type);

		assertEquals(type, settableAnyProperty.getType());
	}

	@Test
	public void testDeserializeAndSet_NullKeyDeserializer() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;
		Object instance = new Object();
		String propName = "prop";

		try {
			settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
		} catch (Exception e) {
			assertEquals(JsonMappingException.class, e.getClass());
			assertEquals("Cannot deserialize 'any' property 'prop' of class null (expected type: null; actual type: null) (no error message provided)", e.getMessage());
		}
	}

	@Test
	public void testDeserializeAndSet_NullValueDeserializer() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;
		Object instance = new Object();
		String propName = "prop";

		KeyDeserializer keyDeser = new KeyDeserializer() {
			@Override
			public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
				return null;
			}
		};

		settableAnyProperty = new SettableAnyProperty(null, null, null, keyDeser, null, null);

		try {
			settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
		} catch (Exception e) {
			assertEquals(JsonMappingException.class, e.getClass());
			assertEquals("Cannot deserialize 'any' property 'prop' of class null (expected type: null; actual type: null) (no error message provided)", e.getMessage());
		}
	}

	@Test
	public void testDeserialize() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;

		TestJsonDeserializer valueDeserializer = new TestJsonDeserializer();

		settableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);

		assertEquals("Test Value", settableAnyProperty.deserialize(p, ctxt));
	}

	@Test
	public void testDeserialize_NullToken() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;

		TestJsonDeserializer valueDeserializer = new TestJsonDeserializer() {
			@Override
			public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return null;
			}
		};

		settableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);

		assertNull(settableAnyProperty.deserialize(p, ctxt));
	}

	@Test
	public void testSet_SetterIsField() throws IOException {
		AnnotatedMember setter = new AnnotatedField(null, null, null);

		settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);

		FieldSetterTestObject instance = new FieldSetterTestObject();
		String propName = "prop";
		Object value = "Test Value";

		settableAnyProperty.set(instance, propName, value);

		Map<Object, Object> field = instance.getField();

		assertTrue(field.containsKey(propName));
		assertEquals(value, field.get(propName));
	}

	@Test
	public void testSet_SetterIsMethod() throws IOException {
		AnnotatedMember setter = new AnnotatedMethod(null, null, null, null);

		settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);

		MethodSetterTestObject instance = new MethodSetterTestObject();
		String propName = "prop";
		Object value = "Test Value";

		settableAnyProperty.set(instance, propName, value);

		Map<Object, Object> method = instance.getMethod();

		assertTrue(method.containsKey(propName));
		assertEquals(value, method.get(propName));
	}

	@Test
	public void testThrowAsIOE() throws IOException {
		Exception e = new IllegalArgumentException();
		String propName = "prop";
		Object value = "Test Value";

		try {
			settableAnyProperty._throwAsIOE(e, propName, value);
		} catch (Exception ex) {
			assertEquals(JsonMappingException.class, ex.getClass());
			assertEquals("Problem deserializing \"any\" property 'prop' of class null (expected type: null; actual type: java.lang.String) (no error message provided)", ex.getMessage());
		}
	}

	@Test
	public void testAnySetterReferring() throws IOException {
		UnresolvedForwardReference reference = new UnresolvedForwardReference();
		Class<?> type = String.class;
		Object instance = new Object();
		String propName = "prop";

		SettableAnyProperty.AnySetterReferring referring = new SettableAnyProperty.AnySetterReferring(settableAnyProperty, reference, type, instance, propName);

		assertEquals(instance, referring.getPojo());
		assertEquals(propName, referring.getPropName());
	}

	private class TestJsonDeserializer extends JsonDeserializer<Object> {
		@Override
		public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return "Test Value";
		}
	}

	private class FieldSetterTestObject {
		private Map<Object, Object> field;

		public Map<Object, Object> getField() {
			return field;
		}

		public void setField(Map<Object, Object> field) {
			this.field = field;
		}
	}

	private class MethodSetterTestObject {
		private Map<Object, Object> method;

		public Map<Object, Object> getMethod() {
			return method;
		}

		public void setMethod(Map<Object, Object> method) {
			this.method = method;
		}
	}
}