package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 // Test with null rawKeyType
 Object result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, false);
 Assert.assertNotNull(result);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test with String rawKeyType
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
 Assert.assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), result.getClass());

 // Test with primitive types
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, false);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), boolean.class, false);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test with Number type
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Double.class, false);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test with Class type
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, false);
 Assert.assertEquals(StdKeySerializers.DEFAULT.getClass(), result.getClass());

 // Test with Date type
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) result)._typeId);

 // Test with Calendar type
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) result)._typeId);

 // Test with UUID type
 result = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class, false);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) result)._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 // Test with null rawKeyType
 Object result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 Assert.assertNull(result);

 // Test with String rawKeyType
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 Assert.assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), result.getClass());

 // Test with primitive types
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), int.class);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), boolean.class);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test with Number type
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Double.class);
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test with Class type
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Class.class);
 Assert.assertEquals(StdKeySerializers.DEFAULT.getClass(), result.getClass());

 // Test with Date type
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Date.class);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) result)._typeId);

 // Test with Calendar type
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Calendar.class);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) result)._typeId);

 // Test with UUID type
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) result)._typeId);

 // Test with Enum type
 result = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Enum.class);
 Assert.assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) result)._typeId);
 }

 @Test
 public void testSerializeWithDefaultKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Object obj = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(obj, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 String expected = "\"default\"";
 String actual = gen.getOutputContent();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeWithStringKeySerializer() throws JsonProcessingException, JsonGenerationException {
 String key = "test";
 Object obj = key;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.DEFAULT_STRING_SERIALIZER.serialize(obj, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 String expected = "\"" + key + "\"";
 String actual = gen.getOutputContent();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeWithDefaultSerializerForDate() throws JsonProcessingException, JsonGenerationException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false).serialize(date, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 String expected = "\"" + date.toISO8601String() + "\"";
 String actual = gen.getOutputContent();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeWithDefaultSerializerForCalendar() throws JsonProcessingException, JsonGenerationException {
 Calendar calendar = Calendar.getInstance();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false).serialize(calendar, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 String expected = "\"" + calendar.getTimeInMillis() + "\"";
 String actual = gen.getOutputContent();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeWithDynamicKeySerializer() throws JsonProcessingException, JsonGenerationException {
 Object obj = new Object() {
 @Override
 public String toString() {
 return "dynamicKey";
 }
 };
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.DEFAULT.serialize(obj, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
 String expected = "\"dynamicKey\"";
 String actual = gen.getOutputContent();
 Assert.assertEquals(expected, actual);
 }
}