package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanDeserialization() throws JsonProcessingException {
 String key = "true";
 JsonDeserializer<Boolean> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 Boolean result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertTrue(result);
 }

 @Test
 public void testBooleanDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid";
 JsonDeserializer<Boolean> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testByteDeserialization() throws JsonProcessingException {
 String key = "5";
 JsonDeserializer<Byte> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
 Byte result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals((byte) 5, result);
 }

 @Test
 public void testByteDeserializationOutOfRange() throws JsonProcessingException {
 String key = "256";
 JsonDeserializer<Byte> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testShortDeserialization() throws JsonProcessingException {
 String key = "100";
 JsonDeserializer<Short> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
 Short result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals((short) 100, result);
 }

 @Test
 public void testShortDeserializationOutOfRange() throws JsonProcessingException {
 String key = "32000";
 JsonDeserializer<Short> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testCharDeserialization() throws JsonProcessingException {
 String key = "A";
 JsonDeserializer<Character> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
 Character result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals('A', result);
 }

 @Test
 public void testCharDeserializationInvalid() throws JsonProcessingException {
 String key = "\\u0061";
 JsonDeserializer<Character> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
 Character result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals('\u0061', result);
 }

 @Test
 public void testIntDeserialization() throws JsonProcessingException {
 String key = "1000";
 JsonDeserializer<Integer> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
 Integer result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(1000, result.intValue());
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 String key = "1234567890123456789";
 JsonDeserializer<Long> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
 Long result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(1234567890123456789L, result.longValue());
 }

 @Test
 public void testFloatDeserialization() throws JsonProcessingException {
 String key = "3.1415";
 JsonDeserializer<Float> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
 Float result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(3.1415f, result, 0.0001f);
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 String key = "3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128451119531";
 JsonDeserializer<Double> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
 Double result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128451119531d, result);
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 String key = "en_US";
 JsonDeserializer<Locale> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
 Locale result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(Locale.US, result);
 }

 @Test
 public void testLocaleDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_locale";
 JsonDeserializer<Locale> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testDateDeserialization() throws JsonProcessingException {
 String key = "2022-12-31";
 JsonDeserializer<Date> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
 Date result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals("Fri Dec 31 00:00:00 CET 2022", result.toString());
 }

 @Test
 public void testDateDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_date";
 JsonDeserializer<Date> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testCalendarDeserialization() throws JsonProcessingException {
 String key = "2022-12-31";
 JsonDeserializer<Calendar> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
 Calendar result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(2022, result.get(Calendar.YEAR));
 Assert.assertEquals(12, result.get(Calendar.MONTH));
 Assert.assertEquals(31, result.get(Calendar.DAY_OF_MONTH));
 }

 @Test
 public void testCalendarDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_calendar";
 JsonDeserializer<Calendar> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testUriDeserialization() throws JsonProcessingException {
 String key = "http://www.example.com";
 JsonDeserializer<URI> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 URI result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals("http://www.example.com", result.toString());
 }

 @Test
 public void testUriDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_uri";
 JsonDeserializer<URI> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testUrlDeserialization() throws JsonProcessingException {
 String key = "http://www.example.com";
 JsonDeserializer<URL> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 URL result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals("http://www.example.com", result.toString());
 }

 @Test
 public void testUrlDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_url";
 JsonDeserializer<URL> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 String key = "java.lang.String";
 JsonDeserializer<Class<?>> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
 Class<?> result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(String.class, result);
 }

 @Test
 public void testClassDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_class";
 JsonDeserializer<Class<?>> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }

 @Test
 public void testEnumDeserialization() throws JsonProcessingException {
 String key = "DayOfWeek.SUNDAY";
 JsonDeserializer<DayOfWeek> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, DayOfWeek.class) {
 @Override
 protected EnumResolver getEnumResolver() {
 return EnumResolver.constructUnsafe(DayOfWeek.class);
 }
 };
 DayOfWeek result = objectMapper.readValue(key, deserializer.getType());
 Assert.assertEquals(DayOfWeek.SUNDAY, result);
 }

 @Test
 public void testEnumDeserializationInvalid() throws JsonProcessingException {
 String key = "invalid_enum";
 JsonDeserializer<DayOfWeek> deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, DayOfWeek.class) {
 @Override
 protected EnumResolver getEnumResolver() {
 return EnumResolver.constructUnsafe(DayOfWeek.class);
 }
 };
 Assert.assertNull(objectMapper.readValue(key, deserializer.getType()));
 }
}