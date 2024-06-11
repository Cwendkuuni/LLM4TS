package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty _property;

 @Before
 public void setUp() {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectCodec objectCodec = jsonFactory.getObjectCodec();
 BeanProperty property = new BeanProperty(new AnnotatedClass(TestClass.class), "testProperty", new SimpleBeanPropertyDefinition(TestClass.class, "testProperty", JsonType.STRING_TYPE, null));
 AnnotatedMethod setter = new AnnotatedMethod(TestClass.class, "setTestProperty", Access.PUBLIC, new AnnotatedParameter[0]);
 JavaType type = objectCodec.getTypeFactory().constructType(TestClass.class);
 _property = new SettableAnyProperty(property, setter, type, null, null, null);
 }

 @Test
 public void testConstructor() {
 assertNotNull(_property);
 }

 @Test
 public void testGetProperty() {
 BeanProperty result = _property.getProperty();
 assertEquals(property, result);
 }

 @Test
 public void testHasValueDeserializer() {
 assertFalse(_property.hasValueDeserializer());
 }

 @Test
 public void testGetType() {
 JavaType result = _property.getType();
 assertEquals(type, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeAndSetNullInstance() throws IOException {
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property.deserializeAndSet(p, null, null, "name");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetNullKey() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, null);
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeAndSetNullValueDeserializer() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property = _property.withValueDeserializer(null);
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals("John", instance.getTestProperty());
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetNullValue() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":null}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeAndSetEmptyStringValue() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":\"\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals("", instance.getTestProperty());
 }

 @Test
 public void testDeserializeAndSetStringValue() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals("John", instance.getTestProperty());
 }

 @Test
 public void testDeserializeAndSetNumberValue() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":42}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals(42, instance.getTestProperty());
 }

 @Test
 public void testDeserializeAndSetBooleanValue() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":true}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 assertTrue(instance.isTestProperty());
 }

 @Test
 public void testDeserializeAndSetNullKeyDeserializer() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"name\":null}");
 _property = _property.withKeyDeserializer(null);
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetUnrecognizedKey() throws IOException {
 Object instance = new TestClass();
 JsonParser p = new JsonFactory().createParser("{\"notName\":\"John\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "notName");
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetKeyDeserializerReturnNull() throws IOException {
 Object instance = new TestClass();
 KeyDeserializer keyDeserializer = new KeyDeserializer() {
 @Override
 public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property = _property.withKeyDeserializer(keyDeserializer);
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 fail("Expected IOException");
 }

 @Test
 public void testSetWithValueDeserializer() throws IOException {
 Object instance = new TestClass();
 String expectedValue = "expectedValue";
 JsonParser p = new JsonFactory().createParser("{\"name\":\""+expectedValue+"\"}");
 JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return expectedValue;
 }
 };
 _property = _property.withValueDeserializer(valueDeserializer);
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals(expectedValue, instance.getTestProperty());
 }

 @Test(expected = IOException.class)
 public void testSetWithNullValueDeserializer() throws IOException {
 Object instance = new TestClass();
 _property = _property.withValueDeserializer(null);
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, "name");
 fail("Expected IOException");
 }

 @Test
 public void testSetWithTypeDeserializer() throws IOException {
 Object instance = new TestClass();
 Class<Integer> integerType = Integer.class;
 TypeDeserializer typeDeserializer = new StubTypeDeserializer() {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
 return 42;
 }
 };
 _property = _property.withValueTypeDeserializer(typeDeserializer);
 _property.deserializeAndSet(new JsonFactory().createParser("{\"name\":42}"), new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals(42, instance.getTestProperty());
 }

 @Test
 public void testSetWithTypeDeserializerAndValueDeserializer() throws IOException {
 Object instance = new TestClass();
 Class<Integer> integerType = Integer.class;
 TypeDeserializer typeDeserializer = new StubTypeDeserializer() {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
 return 42;
 }
 };
 JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "expectedValue";
 }
 };
 _property = _property.withValueDeserializer(valueDeserializer).withValueTypeDeserializer(typeDeserializer);
 _property.deserializeAndSet(new JsonFactory().createParser("{\"name\":42}"), new DeserializationContext(new ObjectMapper()), instance, "name");
 assertEquals(42, instance.getTestProperty());
 }

 @Test(expected = IOException.class)
 public void testSetWithNullKeyDeserializer() throws IOException {
 Object instance = new TestClass();
 _property = _property.withKeyDeserializer(null);
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testSetWithNullValueDeserializerAndNullKeyDeserializer() throws IOException {
 Object instance = new TestClass();
 _property = _property.withValueDeserializer(null).withKeyDeserializer(null);
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\"}");
 _property.deserializeAndSet(p, new DeserializationContext(new ObjectMapper()), instance, null);
 fail("Expected IOException");
 }
}

 class StubTypeDeserializer implements TypeDeserializer {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
 return null;
 }
}

 class TestClass {
 private String testProperty;

 public String getTestProperty() { return testProperty; }
 public void setTestProperty(String testProperty) { this.testProperty = testProperty; }
 public boolean isTestProperty() { return testProperty != null; }
 }