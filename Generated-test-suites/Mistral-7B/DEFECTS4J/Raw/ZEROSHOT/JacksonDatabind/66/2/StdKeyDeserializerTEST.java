package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonAnnotationsScan
public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testForTypeBoolean() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
 String key = "true";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, result);
 key = "false";
 result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testForTypeByte() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
 String key = "127";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals((byte) 127, result);
 key = "-128";
 result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals((byte) -128, result);
 }

 @Test
 public void testForTypeShort() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
 String key = "32767";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals((short) 32767, result);
 key = "-32768";
 result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals((short) -32768, result);
 }

 @Test
 public void testForTypeChar() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
 String key = "A";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals('A', result);
 }

 @Test
 public void testForTypeInt() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
 String key = "42";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(42, result);
 }

 @Test
 public void testForTypeLong() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
 String key = "9223372036854775807";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(9223372036854775807L, result);
 }

 @Test
 public void testForTypeFloat() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
 String key = "3.14F";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(3.14f, result);
 }

 @Test
 public void testForTypeDouble() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
 String key = "3.14";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(3.14, result);
 }

 @Test
 public void testForTypeLocale() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
 String key = "en_US";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertEquals(Locale.US, result);
 }

 @Test
 public void testForTypeDate() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
 String key = "2022-01-01";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testForTypeCalendar() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
 String key = "2022-01-01";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testForTypeUri() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
 String key = "https://www.example.com";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testForTypeUrl() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
 String key = "https://www.example.com";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testForTypeClass() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
 String key = "[Ljava.lang.String;";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertTrue(result.getClass().isArray());
 }

 @Test
 public void testNullKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 Object key = null;
 Object result = serializer.deserializeKey(key.toString(), mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testInvalidKey() throws Exception {
 StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
 String key = "invalid_key";
 Object result = serializer.deserializeKey(key, mapper.getDeserializationContext());
 assertNotNull(result);
 fail("Expected an exception");
 }
}