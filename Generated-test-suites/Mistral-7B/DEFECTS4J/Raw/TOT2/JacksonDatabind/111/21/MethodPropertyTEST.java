package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;

public class MethodPropertyTest extends TestBase {

private static final Class<MethodPropertyTest> TEST_CLASS = MethodPropertyTest.class;
private Object testInstance;

@Before public void setup() throws Exception { testInstance = new Object(); }

// Test constructor
@Test public void testMethodPropertyConstructor() throws Exception { BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", String.class); JavaType type = new TypeFactory().constructType(String.class); TypeDeserializer typeDeser = new JsonDeserializer<>(); Annotations contextAnnotations = new StandardAnnotations(); AnnotatedMethod method = new AnnotatedMethod(TEST_CLASS.getDeclaredMethod("testMethod"), new Class[]{String.class}); MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method); assertNotNull(methodProperty); }

// Test deserializeAndSet
@Test public void testDeserializeAndSetValidJson() throws Exception { JsonParser jsonParser = new TestJsonParser(); DeserializationContext ctxt = new TestDeserializationContext(); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); jsonParser.nextValue("testValue"); methodProperty.deserializeAndSet(jsonParser, ctxt, testInstance); assertEquals("testValue", methodProperty.getName().getName(), testInstance.getClass().getField("testProperty").get(testInstance)); }

@Test public void testDeserializeAndSetNullValue() throws Exception { JsonParser jsonParser = new TestJsonParser(); DeserializationContext ctxt = new TestDeserializationContext(); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); jsonParser.nextToken(JsonToken.VALUE_NULL); methodProperty.deserializeAndSet(jsonParser, ctxt, testInstance); assertNull(methodProperty.getName().getName(), testInstance.getClass().getField("testProperty")); }

@Test public void testDeserializeAndSetInvalidJsonTokens() throws Exception { JsonParser jsonParser = new TestJsonParser(); DeserializationContext ctxt = new TestDeserializationContext(); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); jsonParser.nextToken(JsonToken.VALUE_NUMBER_INT); methodProperty.deserializeAndSet(jsonParser, ctxt, testInstance); fail("Expected IOException when deserializing with invalid JsonToken"); }

// Test deserializeSetAndReturn
@Test public void testDeserializeSetAndReturnValidJson() throws Exception { JsonParser jsonParser = new TestJsonParser(); DeserializationContext ctxt = new TestDeserializationContext(); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); jsonParser.nextValue("testValue"); Object result = methodProperty.deserializeSetAndReturn(jsonParser, ctxt, testInstance); assertEquals("testValue", result); }

@Test public void testDeserializeSetAndReturnNullValue() throws Exception { JsonParser jsonParser = new TestJsonParser(); DeserializationContext ctxt = new TestDeserializationContext(); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); jsonParser.nextToken(JsonToken.VALUE_NULL); Object result = methodProperty.deserializeSetAndReturn(jsonParser, ctxt, testInstance); assertNull(result); }

// Test set
@Test public void testSetValidValue() throws Exception { Object value = "testValue"; MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); ReflectionTestUtils.setFieldValue(methodProperty, "testInstance", testInstance); methodProperty.set(testInstance, value); assertEquals("testValue", methodProperty.getName().getName(), testInstance.getClass().getField("testProperty").get(testInstance)); }

@Test public void testSetNullValue() throws Exception { MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); ReflectionTestUtils.setFieldValue(methodProperty, "testInstance", testInstance); methodProperty.set(testInstance, null); assertNull(methodProperty.getName().getName(), testInstance.getClass().getField("testProperty")); }

@Test(expected = IOException.class) public void testSetInvalidValueType() throws Exception { Object invalidValue = new Object(); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); ReflectionTestUtils.setFieldValue(methodProperty, "testInstance", testInstance); methodProperty.set(testInstance, invalidValue); fail("Expected IOException when setting invalid value type"); }

// Test access private setter
@Test public void testAccessPrivateSetter() throws Exception { Method setterMethod = TEST_CLASS.getDeclaredMethod("setTestProperty"); setAccessible(setterMethod); MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); ReflectionTestUtils.setFieldValue(methodProperty, "testInstance", testInstance); ReflectionTestUtils.invokeMethod(methodProperty, "set", testInstance, "testValue"); assertEquals("testValue", methodProperty.getName().getName(), testInstance.getClass().getField("testProperty").get(testInstance)); }

// Test readResolve
@Test public void testReadResolve() throws Exception { MethodProperty methodProperty = new MethodProperty(null, null, null, null, null); methodProperty = (MethodProperty) methodProperty.readResolve(); assertNotNull(methodProperty); }
}