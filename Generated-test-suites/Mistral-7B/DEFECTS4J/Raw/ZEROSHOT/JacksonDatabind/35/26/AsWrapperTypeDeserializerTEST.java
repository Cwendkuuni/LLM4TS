package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

 private static final String JSON_WRAPPER = "{\"@type\":\"com.TestClass\",\"value\":123}";

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(TestClass.class), null, "_class", true, TestDefaultImpl.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDefaultTyping(DefaultTyping.NON_FINAL);
 ObjectReader reader = mapper.readerForType(TestClass.class);

 Object result = reader.readValue(new StringReader(JSON_WRAPPER), deserializer);
 assertEquals(TestClass.class, result.getClass());
 assertEquals(123, ((TestClass) result).getValue());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(TestClass[].class), null, "_class", true, TestDefaultImpl[].class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForArray(TestClass.class);

 Object result = reader.readValue(new StringReader(JSON_WRAPPER), deserializer);
 assertEquals(TestClass[].class, result.getClass());
 assertArrayEquals(new TestClass[]{new TestClass(123)}, (TestClass[]) result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(String.class), null, "_class", true, String.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForValue("abc");

 Object result = reader.readValue(new StringReader(JSON_WRAPPER), deserializer);
 assertEquals(String.class, result.getClass());
 assertEquals("123", result.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(Object.class), null, "_class", true, Object.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForAny();

 Object result = reader.readValue(new StringReader(JSON_WRAPPER), deserializer);
 assertEquals(TestClass.class, result.getClass());
 assertEquals(123, ((TestClass) result).getValue());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(TestClass.class), null, "_class", true, TestDefaultImpl.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDefaultTyping(DefaultTyping.STANDARD_OBJECT);
 ObjectReader reader = mapper.readerForType(TestClass.class);

 Object result = reader.readValue(new StringReader(JSON_WRAPPER), deserializer);
 assertEquals(TestClass.class, result.getClass());
 assertEquals(123, ((TestClass) result).getValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeWrongJsonFormat() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(TestClass.class), null, "_class", true, TestDefaultImpl.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(TestClass.class);

 reader.readValue(new StringReader("{\"value\":123}"));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithoutTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(JavaType.of(TestClass.class), null, "_class", true, TestDefaultImpl.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerForType(TestClass.class);

 reader.readValue(new StringReader("{value:123}"));
 }
}

class TestClass {
 private int value;

 public TestClass(int value) {
 this.value = value;
 }

 public int getValue() {
 return value;
 }
}

class TestDefaultImpl {
}