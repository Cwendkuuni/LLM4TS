package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test case for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testForType() throws Exception {
 Class<?>[] classes = new Class<?>[] { String.class, Object.class, UUID.class, Integer.class, Long.class,
         Date.class, Calendar.class, Boolean.class, Byte.class, Character.class, Short.class, Float.class,
         Double.class, URI.class, URL.class, Class.class, Locale.class, Currency.class };
 for (Class<?> cls : classes) {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(cls);
 assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
 assertEquals(cls, keyDeserializer.getKeyClass());
 }
 }

 @Test
 public void testBooleanKeyDeserialization() throws Exception {
 StdKeyDeserializer booleanKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 Map<String, Boolean> map = new HashMap<>();
 map.put("true", true);
 map.put("false", false);
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"true\",\"value\":1}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = booleanKeyDeserializer.deserializeKey("true", mapper.getDeserializationContext());
 assertTrue((Boolean) key);
 key = booleanKeyDeserializer.deserializeKey("false", mapper.getDeserializationContext());
 assertFalse((Boolean) key);
 }

 @Test
 public void testByteKeyDeserialization() throws Exception {
 StdKeyDeserializer byteKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
 Map<String, Byte> map = new HashMap<>();
 map.put("123", (byte) 123);
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"123\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = byteKeyDeserializer.deserializeKey("123", mapper.getDeserializationContext());
 assertEquals((byte) 123, key);
 }

 @Test
 public void testEnumKeyDeserialization() throws Exception {
 EnumResolver enumResolver = mapper.getAnnotationIntrospector().findEnumValueResolver(Locale.class);
 StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, enumResolver);
 Map<String, Object> localeMap = new HashMap<>();
 localeMap.put("en_US", Locale.US);
 localeMap.put("fr_FR", Locale.FRANCE);
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"en_US\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = enumKeyDeserializer.deserializeKey("en_US", mapper.getDeserializationContext());
 assertEquals(Locale.US, key);
 }

 @Test
 public void testNullKeyDeserialization() throws Exception {
 StdKeyDeserializer nullKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 Map<String, Boolean> map = new HashMap<>();
 map.put(null, null);
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":null,\"value\":1}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = nullKeyDeserializer.deserializeKey(null, mapper.getDeserializationContext());
 assertNull(key);
 }

 @Test
 public void testInvalidKeyDeserialization() throws Exception {
 StdKeyDeserializer stringKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
 Map<String, Object> map = new HashMap<>();
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"invalidKey\",\"value\":1}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 try {
 Object key = stringKeyDeserializer.deserializeKey("invalidKey", mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 String expectedMessage = "not a valid representation for expected class: java.lang.String";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testUnknownEnumValueDeserialization() throws Exception {
 EnumResolver enumResolver = mapper.getAnnotationIntrospector().findEnumValueResolver(Locale.class);
 StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, enumResolver);
 Map<String, Object> localeMap = new HashMap<>();
 localeMap.put("unknownLocale", "unknownLocaleValue");
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"unknownLocale\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 try {
 Object key = enumKeyDeserializer.deserializeKey("unknownLocale", mapper.getDeserializationContext());
 fail("Expected JsonMappingException with message 'not one of values excepted for Enum class: [Ljava.lang.Enum;@7b648662'");
 } catch (JsonMappingException e) {
 String expectedMessage = "not one of values excepted for Enum class: [Ljava.lang.Enum;@7b648662";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testUrlKeyDeserialization() throws Exception {
 StdKeyDeserializer urlKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 Map<String, URL> map = new HashMap<>();
 map.put("http://example.com", new URL("http://example.com"));
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"http://example.com\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = urlKeyDeserializer.deserializeKey("http://example.com", mapper.getDeserializationContext());
 assertEquals(new URL("http://example.com"), key);
 }

 @Test
 public void testUriKeyDeserialization() throws Exception {
 StdKeyDeserializer uriKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 Map<String, URI> map = new HashMap<>();
 map.put("http://example.com", new URI("http://example.com"));
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"http://example.com\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = uriKeyDeserializer.deserializeKey("http://example.com", mapper.getDeserializationContext());
 assertEquals(new URI("http://example.com"), key);
 }

 @Test
 public void testInvalidUriKeyDeserialization() throws Exception {
 StdKeyDeserializer uriKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 Map<String, URI> map = new HashMap<>();
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"invalidUri://example.com\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 try {
 Object key = uriKeyDeserializer.deserializeKey("invalidUri://example.com", mapper.getDeserializationContext());
 fail("Expected JsonMappingException with message 'Invalid URI: The scheme must not be empty'");
 } catch (JsonMappingException e) {
 String expectedMessage = "Invalid URI: The scheme must not be empty";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testInvalidUrlKeyDeserialization() throws Exception {
 StdKeyDeserializer urlKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 Map<String, URL> map = new HashMap<>();
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"invalidUrl://example.com\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 try {
 Object key = urlKeyDeserializer.deserializeKey("invalidUrl://example.com", mapper.getDeserializationContext());
 fail("Expected JsonMappingException with message 'Invalid URL: The protocol must not be empty or null'");
 } catch (JsonMappingException e) {
 String expectedMessage = "Invalid URL: The protocol must not be empty or null";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testCurrencyKeyDeserialization() throws Exception {
 EnumSet<Locale.Currency> currencies = EnumSet.allOf(Locale.Currency.class);
 StdKeyDeserializer currencyKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
 Map<String, Currency> map = new HashMap<>();
 for (Locale.Currency currency : currencies) {
 map.put(currency.getCurrencyCode(), currency);
 }
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"USD\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 Object key = currencyKeyDeserializer.deserializeKey("USD", mapper.getDeserializationContext());
 assertEquals(Currency.USD, key);
 }

 @Test
 public void testInvalidCurrencyKeyDeserialization() throws Exception {
 EnumSet<Locale.Currency> currencies = EnumSet.allOf(Locale.Currency.class);
 StdKeyDeserializer currencyKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
 Map<String, Currency> map = new HashMap<>();
 for (Locale.Currency currency : currencies) {
 map.put(currency.getCurrencyCode(), currency);
 }
 ObjectReader reader = mapper.readerForMap(Map.class);
 Object readValue = reader.readValue(new StringReader("{\"key\":\"invalidCurrency:XYZ\",\"value\":\"value\"}"));
 Map<String, Object> result = (Map<String, Object>) readValue;
 try {
 Object key = currencyKeyDeserializer.deserializeKey("invalidCurrency:XYZ", mapper.getDeserializationContext());
 fail("Expected JsonMappingException with message 'Invalid Currency: Unknown currency code: XYZ'");
 } catch (JsonMappingException e) {
 String expectedMessage = "Invalid Currency: Unknown currency code: XYZ";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }
}