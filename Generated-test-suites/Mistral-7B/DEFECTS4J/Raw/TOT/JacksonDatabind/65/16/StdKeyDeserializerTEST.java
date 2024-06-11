package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBooleanKeyDeserialization() throws Exception {
 String key = "true";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testBooleanKeyDeserialization_false() throws Exception {
 String key = "false";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidBooleanKeyDeserialization() throws Exception {
 String key = "invalid";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testByteKeyDeserialization() throws Exception {
 String key = "127";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) 127, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testByteKeyDeserialization_invalid() throws Exception {
 String key = "256";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testShortKeyDeserialization() throws Exception {
 String key = "shortMaxValue";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Short);
 assertEquals((Short) Short.MAX_VALUE, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testShortKeyDeserialization_invalid() throws Exception {
 String key = "shortMaxValue+1";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testIntKeyDeserialization() throws Exception {
 String key = "123456";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Integer);
 assertEquals((Integer) 123456, result);
 }

 @Test
 public void testLongKeyDeserialization() throws Exception {
 String key = "1234567890123456";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Long);
 assertEquals((Long) 1234567890123456L, result);
 }

 @Test
 public void testFloatKeyDeserialization() throws Exception {
 String key = "3.14159";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Float);
 assertEquals((Float) 3.14159f, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatKeyDeserialization_invalid() throws Exception {
 String key = "invalid";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testDoubleKeyDeserialization() throws Exception {
 String key = "3.14159265358979323846";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Double);
 assertEquals((Double) 3.14159265358979323846, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDoubleKeyDeserialization_invalid() throws Exception {
 String key = "invalid";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testLocaleKeyDeserialization() throws Exception {
 String key = "en_US";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testLocaleKeyDeserialization_invalid() throws Exception {
 String key = "invalidLocale";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testDateKeyDeserialization() throws Exception {
 String key = "2022-01-01";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Date);
 assertEquals(new Date(0), result); // Date constructor with String sets time to 0
 }

 @Test(expected = JsonMappingException.class)
 public void testDateKeyDeserialization_invalid() throws Exception {
 String key = "invalidDate";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testCalendarKeyDeserialization() throws Exception {
 String key = "2022-01-01";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Calendar);
 assertEquals(Calendar.getInstance(), result); // Calendar constructor with String sets time to current time
 }

 @Test(expected = JsonMappingException.class)
 public void testCalendarKeyDeserialization_invalid() throws Exception {
 String key = "invalidCalendar";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testUUIDKeyDeserialization() throws Exception {
 String key = "123e4567-e89b-12d3-a456-426614174000";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof UUID);
 assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testUUIDKeyDeserialization_invalid() throws Exception {
 String key = "invalidUUID";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testURIKeyDeserialization() throws Exception {
 String key = "https://example.com";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof URI);
 assertEquals(new URI("https://example.com"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testURIKeyDeserialization_invalid() throws Exception {
 String key = "invalidURI";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testURLKeyDeserialization() throws Exception {
 String key = "https://example.com";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof URL);
 assertEquals(new URL("https://example.com"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testURLKeyDeserialization_invalid() throws Exception {
 String key = "invalidURL";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }

 @Test
 public void testClassKeyDeserialization() throws Exception {
 String key = "java.lang.String";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Class.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 Object result = kd.deserializeKey(key, ctxt);
 assertTrue(result instanceof Class);
 assertEquals(String.class, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testClassKeyDeserialization_invalid() throws Exception {
 String key = "invalidClass";
 StdKeyDeserializer kd = StdKeyDeserializer.forType(Class.class);
 DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();
 kd.deserializeKey(key, ctxt);
 fail("Expected JsonMappingException but got: " + kd.deserializeKey(key, ctxt));
 }
}