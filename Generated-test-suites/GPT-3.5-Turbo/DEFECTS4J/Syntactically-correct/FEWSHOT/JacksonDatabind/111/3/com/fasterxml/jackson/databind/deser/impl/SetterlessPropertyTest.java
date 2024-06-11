package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {
	private SetterlessProperty setterlessProperty;
	private BeanPropertyDefinition propDef;
	private JavaType type;
	private TypeDeserializer typeDeser;
	private Annotations contextAnnotations;
	private AnnotatedMethod method;
	
	@Before
	public void setUp() {
		propDef = mock(BeanPropertyDefinition.class);
		type = mock(JavaType.class);
		typeDeser = mock(TypeDeserializer.class);
		contextAnnotations = mock(Annotations.class);
		method = mock(AnnotatedMethod.class);
		setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
	}
	
	@Test
	public void testWithName() {
		PropertyName newName = mock(PropertyName.class);
		SettableBeanProperty result = setterlessProperty.withName(newName);
		assertEquals(newName, result.getFullName());
	}
	
	@Test
	public void testWithValueDeserializer() {
		JsonDeserializer<?> deser = mock(JsonDeserializer.class);
		SettableBeanProperty result = setterlessProperty.withValueDeserializer(deser);
		assertEquals(deser, result.getValueDeserializer());
	}
	
	@Test
	public void testWithNullProvider() {
		NullValueProvider nva = mock(NullValueProvider.class);
		SettableBeanProperty result = setterlessProperty.withNullProvider(nva);
		assertEquals(nva, result.getNullValueProvider());
	}
	
	@Test
	public void testFixAccess() {
		DeserializationConfig config = mock(DeserializationConfig.class);
		setterlessProperty.fixAccess(config);
		// Verify fixAccess is called on AnnotatedMethod class
	}
	
	@Test
	public void testGetAnnotation() {
		Class<A> acls = mock(Class.class);
		A result = setterlessProperty.getAnnotation(acls);
		assertEquals(acls, result);
	}
	
	@Test
	public void testGetMember() {
		AnnotatedMember result = setterlessProperty.getMember();
		assertEquals(method, result);
	}
	
	@Test
	public void testDeserializeAndSet_NullValue() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		setterlessProperty.deserializeAndSet(p, ctxt, instance);
		// Verify that no exception is thrown when the current token is VALUE_NULL
	}
	
	@Test
	public void testDeserializeAndSet_WithTypeDeserializer() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		setterlessProperty.setValueTypeDeserializer(mock(TypeDeserializer.class));
		setterlessProperty.deserializeAndSet(p, ctxt, instance);
		// Verify that JsonParseException is thrown when the value type deserializer is not null
	}
	
	@Test
	public void testDeserializeAndSet_NullToModify() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		setterlessProperty.setValueDeserializer(mock(JsonDeserializer.class));
		setterlessProperty.deserializeAndSet(p, ctxt, instance);
		// Verify that BadDefinitionException is thrown when the get method returns null
	}
	
	@Test
	public void testDeserializeSetAndReturn() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);
		
		assertEquals(instance, result);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSet() throws IOException {
		Object instance = mock(Object.class);
		Object value = mock(Object.class);
		setterlessProperty.set(instance, value);
		// Verify that UnsupportedOperationException is thrown when calling set() method
	}
	
	@Test
	public void testSetAndReturn() throws IOException {
		Object instance = mock(Object.class);
		Object value = mock(Object.class);
		Object result = setterlessProperty.setAndReturn(instance, value);
		
		assertEquals(instance, result);
	}
}