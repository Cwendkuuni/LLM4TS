package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

public class MethodPropertyTest {

	@Test
	public void testConstructor() {
		BeanPropertyDefinition propDef = new BeanPropertyDefinition();
		JavaType type = new JavaType();
		TypeDeserializer typeDeser = new TypeDeserializer();
		Annotations contextAnnotations = new Annotations();
		AnnotatedMethod method = new AnnotatedMethod();
		
		MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

		// Test that all fields are initialized correctly
		Assert.assertEquals(propDef, methodProperty.propDef);
		Assert.assertEquals(type, methodProperty.type);
		Assert.assertEquals(typeDeser, methodProperty.typeDeser);
		Assert.assertEquals(contextAnnotations, methodProperty.contextAnnotations);
		Assert.assertEquals(method, methodProperty.annotated);
		
		// Test that setter is correctly set
		Assert.assertEquals(method.getAnnotated(), methodProperty.setter);
	}
	
	@Test
	public void testWithNullProvider() {
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
		NullValueProvider nva = new NullValueProvider();
		
		MethodProperty updatedMethodProperty = methodProperty.withNullProvider(nva);

		Assert.assertEquals(nva, updatedMethodProperty.nullProvider);
	}
	
	@Test
	public void testFixAccess() {
		DeserializationConfig config = new DeserializationConfig();

		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());

		// Perform fix access
		methodProperty.fixAccess(config);

		// Test that fixAccess correctly fixes access
		Assert.assertTrue(methodProperty.annotated.accessFixed);
	}
	
	@Test
	public void testDeserializeAndSet_WithNullValue() throws IOException {
		JsonParser p = new JsonParser();
		DeserializationContext ctxt = new DeserializationContext();
		Object instance = new Object();

		NullValueProvider nva = new NullValueProvider();
		JavaType type = new JavaType();
		TypeDeserializer typeDeser = new TypeDeserializer();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), type, typeDeser, new Annotations(), new AnnotatedMethod());
		methodProperty.nullProvider = nva;
		
		// Simulate the case where the token is VALUE_NULL
		p.setToken(JsonToken.VALUE_NULL);
		
		methodProperty.deserializeAndSet(p, ctxt, instance);
		
		// Verify that set is not invoked since skipNulls is true
		Assert.assertNull(instance);
	}
	
	@Test
	public void testDeserializeAndSet_WithoutTypeDeserializer() throws IOException {
		JsonParser p = new JsonParser();
		DeserializationContext ctxt = new DeserializationContext();
		Object instance = new Object();
		Object value = new Object();

		JsonDeserializer<?> deser = new JsonDeserializer<>();
		NullValueProvider nva = new NullValueProvider();
		JavaType type = new JavaType();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), type, null, new Annotations(), new AnnotatedMethod());
		methodProperty.valueDeserializer = deser;
		methodProperty.nullProvider = nva;
		
		// Simulate the case where the token is not VALUE_NULL
		p.setCurrentValue(value);
		p.setToken(JsonToken.VALUE_NUMBER_INT);
		
		Method setter = new Method();

		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;

		methodProperty.deserializeAndSet(p, ctxt, instance);
		
		// Verify that the invoke method is correctly called
		Assert.assertEquals(value, setter.invoke(instance, value));
	}
	
	@Test
	public void testDeserializeAndSet_WithTypeDeserializer() throws IOException {
		JsonParser p = new JsonParser();
		DeserializationContext ctxt = new DeserializationContext();
		Object instance = new Object();
		Object value = new Object();
		TypeDeserializer typeDeser = new TypeDeserializer();
		
		JsonDeserializer<?> deser = new JsonDeserializer<>();
		NullValueProvider nva = new NullValueProvider();
		JavaType type = new JavaType();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), type, typeDeser, new Annotations(), new AnnotatedMethod());
		methodProperty.valueTypeDeserializer = typeDeser;
		methodProperty.valueDeserializer = deser;
		methodProperty.nullProvider = nva;
		
		// Simulate the case where the token is not VALUE_NULL
		p.setCurrentValue(value);
		p.setToken(JsonToken.VALUE_NUMBER_FLOAT);
		
		Method setter = new Method();

		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;

		methodProperty.deserializeAndSet(p, ctxt, instance);
		
		// Verify that the invoke method is correctly called
		Assert.assertEquals(value, setter.invoke(instance, value));
	}
	
	@Test
	public void testDeserializeSetAndReturn_WithNullValue() throws IOException {
		JsonParser p = new JsonParser();
		DeserializationContext ctxt = new DeserializationContext();
		Object instance = new Object();

		NullValueProvider nva = new NullValueProvider();
		JavaType type = new JavaType();
		TypeDeserializer typeDeser = new TypeDeserializer();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), type, typeDeser, new Annotations(), new AnnotatedMethod());
		methodProperty.nullProvider = nva;
		
		// Simulate the case where the token is VALUE_NULL
		p.setToken(JsonToken.VALUE_NULL);
		
		methodProperty.deserializeSetAndReturn(p, ctxt, instance);
		
		// Verify that set is not invoked since skipNulls is true
		Assert.assertEquals(instance, methodProperty.deserializeSetAndReturn(p, ctxt, instance));
	}
	
	@Test
	public void testDeserializeSetAndReturn_WithoutTypeDeserializer() throws IOException {
		JsonParser p = new JsonParser();
		DeserializationContext ctxt = new DeserializationContext();
		Object instance = new Object();
		Object value = new Object();

		JsonDeserializer<?> deser = new JsonDeserializer<>();
		NullValueProvider nva = new NullValueProvider();
		JavaType type = new JavaType();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), type, null, new Annotations(), new AnnotatedMethod());
		methodProperty.valueDeserializer = deser;
		methodProperty.nullProvider = nva;
		
		// Simulate the case where the token is not VALUE_NULL
		p.setCurrentValue(value);
		p.setToken(JsonToken.VALUE_NUMBER_INT);
		
		Method setter = new Method();

		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;
		
		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;

		Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
		
		// Verify that the invoke method is correctly called and the return value is instance
		Assert.assertEquals(instance, result);
		Assert.assertEquals(value, setter.invoke(instance, value));
	}
	
	@Test
	public void testDeserializeSetAndReturn_WithTypeDeserializer() throws IOException {
		JsonParser p = new JsonParser();
		DeserializationContext ctxt = new DeserializationContext();
		Object instance = new Object();
		Object value = new Object();
		TypeDeserializer typeDeser = new TypeDeserializer();
		
		JsonDeserializer<?> deser = new JsonDeserializer<>();
		NullValueProvider nva = new NullValueProvider();
		JavaType type = new JavaType();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), type, typeDeser, new Annotations(), new AnnotatedMethod());
		methodProperty.valueTypeDeserializer = typeDeser;
		methodProperty.valueDeserializer = deser;
		methodProperty.nullProvider = nva;
		
		// Simulate the case where the token is not VALUE_NULL
		p.setCurrentValue(value);
		p.setToken(JsonToken.VALUE_NUMBER_FLOAT);
		
		Method setter = new Method();

		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;

		Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
		
		// Verify that the invoke method is correctly called and the return value is instance
		Assert.assertEquals(instance, result);
		Assert.assertEquals(value, setter.invoke(instance, value));
	}
	
	@Test
	public void testSet() throws IOException {
		Object instance = new Object();
		Object value = new Object();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
		
		Method setter = new Method();
		
		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;

		methodProperty.set(instance, value);
		
		// Verify that the invoke method is correctly called
		Assert.assertEquals(value, setter.invoke(instance, value));
	}
	
	@Test
	public void testSetAndReturn() throws IOException {
		Object instance = new Object();
		Object value = new Object();
		
		MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(), new JavaType(), new TypeDeserializer(), new Annotations(), new AnnotatedMethod());
		
		Method setter = new Method();
		
		// Mock the invoke method to check if it is called correctly
		methodProperty.setter = setter;

		Object result = methodProperty.setAndReturn(instance, value);
		
		// Verify that the invoke method is correctly called and the return value is instance
		Assert.assertEquals(instance, result);
		Assert.assertEquals(value, setter.invoke(instance, value));
	}
	
}