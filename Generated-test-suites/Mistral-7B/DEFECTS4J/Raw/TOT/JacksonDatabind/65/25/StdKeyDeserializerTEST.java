package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testStringKeyDeserializer() throws Exception {
 String key = "test";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeKey(key, ctxt);
 assertEquals(key, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidBooleanKeyDeserializer() throws Exception {
 String key = "invalid";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 deserializer.deserializeKey(key, ctxt);
 fail("Should have thrown JsonMappingException for invalid boolean key");
 }

 @Test
 public void testValidBooleanKeyDeserializer() throws Exception {
 String keyTrue = "true";
 String keyFalse = "false";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object resultTrue = deserializer.deserializeKey(keyTrue, ctxt);
 assertTrue((Boolean) resultTrue);
 Object resultFalse = deserializer.deserializeKey(keyFalse, ctxt);
 assertFalse((Boolean) resultFalse);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidByteKeyDeserializer() throws Exception {
 String key = "256";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 deserializer.deserializeKey(key, ctxt);
 fail("Should have thrown JsonMappingException for invalid byte key");
 }

 @Test
 public void testValidByteKeyDeserializer() throws Exception {
 String key = "50";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeKey(key, ctxt);
 assertEquals((Byte) 50, result);
 }

 // Test other data types similarly

 @Test(expected = JsonMappingException.class)
 public void testInvalidEnumKeyDeserializer() throws Exception {
 String key = "XYZ";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 deserializer.deserializeKey(key, ctxt);
 fail("Should have thrown JsonMappingException for invalid enum key");
 }

 @Test
 public void testValidEnumKeyDeserializer() throws Exception {
 String key = "MY_ENUM_VALUE";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeKey(key, ctxt);
 assertEquals(MyEnum.MY_ENUM_VALUE, result);
 }

 @Test
 public void testNullKeyDeserializer() throws Exception {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeKey(null, ctxt);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidUUIDKeyDeserializer() throws Exception {
 String key = "invalid_uuid";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 deserializer.deserializeKey(key, ctxt);
 fail("Should have thrown JsonMappingException for invalid UUID key");
 }

 @Test
 public void testValidUUIDKeyDeserializer() throws Exception {
 String key = "123e4567-e89b-12d3-a456-426614174000";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeKey(key, ctxt);
 assertEquals(UUID.fromString(key), result);
 }

 // Test other data types and edge cases as needed

}