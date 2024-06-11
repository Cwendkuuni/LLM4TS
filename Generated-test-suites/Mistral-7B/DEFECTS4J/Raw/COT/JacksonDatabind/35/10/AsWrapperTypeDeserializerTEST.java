package com.example.junit.test.jackson;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final String BASE_TYPE_NAME = "com.example.MyClass";

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"type\":\"MyType\",\"value\":123}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("123", result.toString());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("[\"".getBytes("UTF-8"), "MyType", "]".getBytes("UTF-8"),
 new ByteArrayInputStream(new byte[] {1, 2, 3}));
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromArray(parser, context);
 assertEquals(3, result.length);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("456");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromScalar(parser, context);
 assertEquals("456", result.toString());
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"type\":\"NotMyType\",\"value\":\"abc\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 try {
 Object result = deserializer.deserializeTypedFromAny(parser, context);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"type\":\"MyType\",\"value\":123}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 context.findRootValueSerializer(BaseTypeName.class).setContentType("application/json");
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("123", result.toString());
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"value\":123}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testMissingValue() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"type\":\"MyType\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected IOException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidTypeIdFormat() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"type\":123,\"value\":123}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected JsonProcessingException");
 }

 @Test
 public void testMergeTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser parser = objectMapper.getFactory().createParser("{\"type\":\"MyType\",\"typeId\":\"MySubType\"}");
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("MySubType", result.getClass().getSimpleName());
 }
}