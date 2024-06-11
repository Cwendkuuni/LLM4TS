package com.example.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

/**
 * Test class for StdKeyDeserializer.
 */
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String trueKey = "true";
 String falseKey = "false";

 Object actualTrue = keyDeserializer.deserializeKey(trueKey, context);
 Object actualFalse = keyDeserializer.deserializeKey(falseKey, context);

 Assert.assertEquals(Boolean.TRUE, actualTrue);
 Assert.assertEquals(Boolean.FALSE, actualFalse);
 }

 @Test
 public void testByteTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validByteKey = "12";
 String invalidByteKey = "123";

 Object actualByte = keyDeserializer.deserializeKey(validByteKey, context);
 Assert.assertEquals((byte) 12, actualByte);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidByteKey, context));
 }

 @Test
 public void testShortTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validShortKey = "123";
 String invalidShortKey = "12345";

 Object actualShort = keyDeserializer.deserializeKey(validShortKey, context);
 Assert.assertEquals((short) 123, actualShort);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidShortKey, context));
 }

 @Test
 public void testCharTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validCharKey = "A";
 String invalidCharKey = "AB";

 Object actualChar = keyDeserializer.deserializeKey(validCharKey, context);
 Assert.assertEquals('A', actualChar);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidCharKey, context));
 }

 @Test
 public void testIntTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validIntKey = "12345";
 String invalidIntKey = "123456";

 Object actualInt = keyDeserializer.deserializeKey(validIntKey, context);
 Assert.assertEquals(12345, actualInt);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidIntKey, context));
 }

 @Test
 public void testLongTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validLongKey = "123456789012345";
 String invalidLongKey = "1234567890123456";

 Object actualLong = keyDeserializer.deserializeKey(validLongKey, context);
 Assert.assertEquals(123456789012345L, actualLong);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidLongKey, context));
 }

 @Test
 public void testLocaleTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validLocaleKey = "en_US";
 String invalidLocaleKey = "en_US_Invalid";

 Object actualLocale = keyDeserializer.deserializeKey(validLocaleKey, context);
 Assert.assertEquals(new Locale("en", "US"), actualLocale);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidLocaleKey, context));
 }

 @Test
 public void testDateTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validDateKey = "2022-03-01";
 String invalidDateKey = "2022-03-32";

 Object actualDate = keyDeserializer.deserializeKey(validDateKey, context);
 Assert.assertNotNull(actualDate);
 Assert.assertEquals("Wed Mar 01 00:00:00 CET 2022", actualDate.toString());

 Assert.assertNull(keyDeserializer.deserializeKey(invalidDateKey, context));
 }

 @Test
 public void testCalendarTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validCalendarKey = "2022-03-01";
 String invalidCalendarKey = "2022-32-01";

 Object actualCalendar = keyDeserializer.deserializeKey(validCalendarKey, context);
 Assert.assertNotNull(actualCalendar);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidCalendarKey, context));
 }

 @Test
 public void testUUIDTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validUUIDKey = "123e4567-e89b-12d3-a456-426614174000";
 String invalidUUIDKey = "123e4567-e89b-12d3-a456-426614174001";

 Object actualUUID = keyDeserializer.deserializeKey(validUUIDKey, context);
 Assert.assertEquals(UUID.fromString(validUUIDKey), actualUUID);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidUUIDKey, context));
 }

 @Test
 public void testURITypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validURIKey = "http://example.com";
 String invalidURIKey = "http://example.com/non_existent_file";

 Object actualURI = keyDeserializer.deserializeKey(validURIKey, context);
 Assert.assertEquals(new URI(validURIKey), actualURI);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidURIKey, context));
 }

 @Test
 public void testURLTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validURLKey = "http://example.com";
 String invalidURLKey = "http://example.com:1234/non_existent_file";

 Object actualURL = keyDeserializer.deserializeKey(validURLKey, context);
 Assert.assertEquals(new URL(validURLKey), actualURL);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidURLKey, context));
 }

 @Test
 public void testEnumTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyColorEnum.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validEnumKey = "RED";
 String invalidEnumKey = "InvalidColor";

 Object actualEnum = keyDeserializer.deserializeKey(validEnumKey, context);
 Assert.assertEquals(MyColorEnum.RED, actualEnum);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidEnumKey, context));
 }

 @Test
 public void testEnumSetTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(EnumSet.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validEnumSetKey = "[BLUE, GREEN]";
 String invalidEnumSetKey = "[BLUE, GREEN, INVALID]";

 Object actualEnumSet = keyDeserializer.deserializeKey(validEnumSetKey, context);
 EnumSet<MyColorEnum> expectedEnumSet = EnumSet.of(MyColorEnum.BLUE, MyColorEnum.GREEN);
 Assert.assertEquals(expectedEnumSet, actualEnumSet);

 Assert.assertNull(keyDeserializer.deserializeKey(invalidEnumSetKey, context));
 }

 @Test
 public void testClassTypeKeyDeserialization() throws IOException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyClass.class);
 DeserializationContext context = objectMapper.getDeserializationContext();
 String validClassKey = "com.example.MyClass";
 String invalidClassKey = "com.example.InvalidClass";

 Object actualClass = keyDeserializer.deserializeKey(validClassKey, context);
 Assert.assertNotNull(actualClass);
 Assert.assertEquals(MyClass.class, actualClass.getClass());

 Assert.assertNull(keyDeserializer.deserializeKey(invalidClassKey, context));
 }
}