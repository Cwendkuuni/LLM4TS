package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "true";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 key = "false";
 result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "127";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) 127, result);

 key = "-128";
 result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) -128, result);
 }

 @Test
 public void testShortKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "32767";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Short);
 assertEquals((Short) 32767, result);

 key = "-32768";
 result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Short);
 assertEquals((Short) -32768, result);
 }

 @Test
 public void testCharKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "A";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Character);
 assertEquals((Character) 'A', result);
 }

 @Test
 public void testIntKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "123456";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Integer);
 assertEquals((Integer) 123456, result);
 }

 @Test
 public void testLongKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "9223372036854775807";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Long);
 assertEquals((Long) 9223372036854775807L, result);
 }

 @Test
 public void testFloatKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "3.14159";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Float);
 assertEquals((Float) 3.14159f, result);
 }

 @Test
 public void testDoubleKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "6.28318530718";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Double);
 assertEquals((Double) 6.28318530718d, result);
 }

 @Test
 public void testLocaleKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "en_US";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testUrlKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "http://www.example.com";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof URL);
 assertEquals(new URL("http://www.example.com"), result);
 }

 @Test
 public void testEnumKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "RED";
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof MyEnum);
 assertEquals(MyEnum.RED, result);
 }

 @Test
 public void testNullKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = null;
 Object result = kd.deserializeKey(key, ctxt);
 assertNull(result);
 }

 @Test
 public void testUnknownKeyType() throws JsonProcessingException {
 KeyDeserializer kd = StdKeyDeserializer.forType(UnknownType.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 String key = "invalid";
 Object result = kd.deserializeKey(key, ctxt);
 assertNotNull(result);
 // Verify that the result is not of expected type
 assertFalse(result instanceof UnknownType);
 }
}