package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper; // for JUnit 5 testing, replace with this import for JUnit 4
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new JsonMapper();
 }

 @Test
 public void testBooleanKeyDeserialization() throws JsonProcessingException {
 String trueKey = "true";
 String falseKey = "false";
 assertEquals(Boolean.TRUE, mapper.readValue("{\"key\":\"" + trueKey + "\"}", StdKeyDeserializer.class).deserializeKey(trueKey, mapper.getDeserializationContext()));
 assertEquals(Boolean.FALSE, mapper.readValue("{\"key\":\"" + falseKey + "\"}", StdKeyDeserializer.class).deserializeKey(falseKey, mapper.getDeserializationContext()));
 }

 @Test
 public void testByteKeyDeserialization() throws JsonProcessingException {
 String key1 = "0";
 String key2 = "127";
 String key3 = "128";
 assertEquals((byte) 0, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals((byte) 127, mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext()));
 assertTrue(mapper.readValue("{\"key\":\"" + key3 + "\"}", StdKeyDeserializer.class).deserializeKey(key3, mapper.getDeserializationContext()) instanceof IllegalArgumentException);
 }

 @Test
 public void testShortKeyDeserialization() throws JsonProcessingException {
 String key1 = "0";
 String key2 = "32767";
 String key3 = "32768";
 assertEquals((short) 0, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals((short) 32767, mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext()));
 assertTrue(mapper.readValue("{\"key\":\"" + key3 + "\"}", StdKeyDeserializer.class).deserializeKey(key3, mapper.getDeserializationContext()) instanceof IllegalArgumentException);
 }

 @Test
 public void testCharKeyDeserialization() throws JsonProcessingException {
 String key1 = "\u0041";
 String key2 = "\u0061";
 assertEquals((char) 'A', mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals((char) 'a', mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext()));
 }

 @Test
 public void testIntKeyDeserialization() throws JsonProcessingException {
 String key1 = "0";
 String key2 = "Integer.MAX_VALUE";
 assertEquals(0, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()).hashCode());
 try {
 mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext());
 fail("Expected IllegalStateException or IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 } catch (IllegalStateException e) {
 fail("Unexpected IllegalStateException");
 }
 }

 @Test
 public void testLongKeyDeserialization() throws JsonProcessingException {
 String key1 = "0";
 String key2 = "Long.MAX_VALUE";
 assertEquals(0L, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()).hashCode());
 try {
 mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext());
 fail("Expected IllegalStateException or IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 } catch (IllegalStateException e) {
 fail("Unexpected IllegalStateException");
 }
 }

 @Test
 public void testFloatKeyDeserialization() throws JsonProcessingException {
 String key1 = "0.0";
 String key2 = "Float.MAX_VALUE";
 assertEquals(0f, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()).hashCode(), 0.0f);
 try {
 mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext());
 fail("Expected IllegalStateException or IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 } catch (IllegalStateException e) {
 fail("Unexpected IllegalStateException");
 }
 }

 @Test
 public void testDoubleKeyDeserialization() throws JsonProcessingException {
 String key1 = "0.0";
 String key2 = "Double.MAX_VALUE";
 assertEquals(0d, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()).hashCode(), 0.0d);
 try {
 mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext());
 fail("Expected IllegalStateException or IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 } catch (IllegalStateException e) {
 fail("Unexpected IllegalStateException");
 }
 }

 @Test
 public void testEnumKeyDeserialization() throws JsonProcessingException {
 String key1 = "Locale.US";
 String key2 = "Locale.GERMANY";
 Class<Locale> enumClass = Locale.class;
 assertEquals(Locale.US, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals(Locale.GERMANY, mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.class).deserializeKey(key2, mapper.getDeserializationContext()));
 }

 @Test
 public void testNullKeyDeserialization() throws JsonProcessingException {
 String nullKey = "null";
 assertEquals(null, mapper.readValue("{\"key\":\"" + nullKey + "\"}", StdKeyDeserializer.class).deserializeKey(nullKey, mapper.getDeserializationContext()));
 }

 @Test
 public void testUriKeyDeserialization() throws JsonProcessingException {
 String key1 = "http://example.com";
 String key2 = "https://example.com";
 assertEquals(new URI(key1), mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.Class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals(new URI(key2), mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.Class).deserializeKey(key2, mapper.getDeserializationContext()));
 }

 @Test
 public void testUrlKeyDeserialization() throws JsonProcessingException {
 String key1 = "http://example.com";
 String key2 = "https://example.com";
 assertEquals(new URL(key1), mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.Class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals(new URL(key2), mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.Class).deserializeKey(key2, mapper.getDeserializationContext()));
 }

 @Test
 public void testClassKeyDeserialization() throws JsonProcessingException {
 String key1 = "java.util.HashMap";
 String key2 = "java.util.ArrayList";
 Class<?> keyClass1 = Class.forName(key1);
 Class<?> keyClass2 = Class.forName(key2);
 assertEquals(keyClass1, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.Class).deserializeKey(key1, mapper.getDeserializationContext()));
 assertEquals(keyClass2, mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.Class).deserializeKey(key2, mapper.getDeserializationContext()));
 }

 @Test
 public void testByteArrayKeyDeserialization() throws JsonProcessingException {
 String key1 = "Rm9udCAoKTs=";
 String key2 = "SSBvZmZpeCw=";
 byte[] key1Bytes = mapper.readValue("{\"key\":\"" + key1 + "\"}", byte[].class);
 byte[] key2Bytes = mapper.readValue("{\"key\":\"" + key2 + "\"}", byte[].class);
 assertEquals(key1Bytes, mapper.readValue("{\"key\":\"" + key1 + "\"}", StdKeyDeserializer.Class).deserializeKey(key1, mapper.getDeserializationContext()).getBytes());
 assertEquals(key2Bytes, mapper.readValue("{\"key\":\"" + key2 + "\"}", StdKeyDeserializer.Class).deserializeKey(key2, mapper.getDeserializationContext()).getBytes());
 }

 @Test
 public void testDeserializationWithUnsupportedType() throws JsonProcessingException {
 try {
 mapper.readValue("{\"key\":\"int\"}", StdKeyDeserializer.Class).deserializeKey("int", mapper.getDeserializationContext());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDeserializationWithNullType() throws JsonProcessingException {
 try {
 mapper.readValue("{\"key\":null}", StdKeyDeserializer.Class).deserializeKey(null, mapper.getDeserializationContext());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}