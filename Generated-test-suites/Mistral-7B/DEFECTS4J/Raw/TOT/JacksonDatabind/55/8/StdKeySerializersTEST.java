package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link StdKeySerializers} class.
 */
public class StdKeySerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with default key serializer.
 */
 @Test
 public void testGetStdKeySerializerWithDefaultSerializer() throws JsonMappingException, IOException {
 Class<?> intClass = int.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), intClass, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with custom key serializer for Integer.
 */
 @Test
 public void testGetStdKeySerializerWithCustomSerializerForInteger() throws JsonMappingException, IOException {
 Class<Integer> intClass = Integer.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), intClass, false);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with null key type.
 */
 @Test(expected = JsonMappingException.class)
 public void testGetStdKeySerializerWithNullKeyType() throws JsonMappingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), null, true);
 fail("Should throw JsonMappingException for null key type");
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with String key type.
 */
 @Test
 public void testGetStdKeySerializerWithStringKeyType() throws JsonMappingException, IOException {
 Class<String> stringClass = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), stringClass, true);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with custom key serializer for String.
 */
 @Test
 public void testGetStdKeySerializerWithCustomSerializerForString() throws JsonMappingException, IOException {
 Class<String> stringClass = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), stringClass, false);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with Boolean key type.
 */
 @Test
 public void testGetStdKeySerializerWithBooleanKeyType() throws JsonMappingException, IOException {
 Class<Boolean> booleanClass = Boolean.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), booleanClass, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with custom key serializer for Boolean.
 */
 @Test
 public void testGetStdKeySerializerWithCustomSerializerForBoolean() throws JsonMappingException, IOException {
 Class<Boolean> booleanClass = Boolean.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), booleanClass, false);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with Date key type.
 */
 @Test
 public void testGetStdKeySerializerWithDateKeyType() throws JsonMappingException, IOException {
 Class<Date> dateClass = Date.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), dateClass, true);
 assertEquals(StdKeySerializers.DEFAULT.TYPE_DATE, serializer._typeId);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with custom key serializer for Date.
 */
 @Test
 public void testGetStdKeySerializerWithCustomSerializerForDate() throws JsonMappingException, IOException {
 Class<Date> dateClass = Date.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), dateClass, false);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with Calendar key type.
 */
 @Test
 public void testGetStdKeySerializerWithCalendarKeyType() throws JsonMappingException, IOException {
 Class<Calendar> calendarClass = Calendar.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), calendarClass, true);
 assertEquals(StdKeySerializers.DEFAULT.TYPE_CALENDAR, serializer._typeId);
 }

 /**
 * Test for {@link StdKeySerializers#getStdKeySerializer(SerializationConfig, Class, boolean)}
 * method with custom key serializer for Calendar.
 */
 @Test
 public void testGetStdKeySerializerWithCustomSerializerForCalendar() throws JsonMappingException, IOException {
 Class<Calendar> calendarClass = Calendar.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), calendarClass, false);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with null key type.
 */
 @Test(expected = JsonMappingException.class)
 public void testGetFallbackKeySerializerWithNullKeyType() throws JsonMappingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), null);
 fail("Should throw JsonMappingException for null key type");
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with Integer key type.
 */
 @Test
 public void testGetFallbackKeySerializerWithIntegerKeyType() throws JsonMappingException, IOException {
 Class<Integer> intClass = Integer.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), intClass);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with custom key serializer for Integer.
 */
 @Test
 public void testGetFallbackKeySerializerWithCustomSerializerForInteger() throws JsonMappingException, IOException {
 Class<Integer> intClass = Integer.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), intClass);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with null key type.
 */
 @Test(expected = JsonMappingException.class)
 public void testGetFallbackKeySerializerWithNullKeyType2() throws JsonMappingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), null);
 fail("Should throw JsonMappingException for null key type");
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with String key type.
 */
 @Test
 public void testGetFallbackKeySerializerWithStringKeyType() throws JsonMappingException, IOException {
 Class<String> stringClass = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), stringClass);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with custom key serializer for String.
 */
 @Test
 public void testGetFallbackKeySerializerWithCustomSerializerForString() throws JsonMappingException, IOException {
 Class<String> stringClass = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), stringClass);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with Boolean key type.
 */
 @Test
 public void testGetFallbackKeySerializerWithBooleanKeyType() throws JsonMappingException, IOException {
 Class<Boolean> booleanClass = Boolean.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), booleanClass);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with custom key serializer for Boolean.
 */
 @Test
 public void testGetFallbackKeySerializerWithCustomSerializerForBoolean() throws JsonMappingException, IOException {
 Class<Boolean> booleanClass = Boolean.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), booleanClass);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with Date key type.
 */
 @Test
 public void testGetFallbackKeySerializerWithDateKeyType() throws JsonMappingException, IOException {
 Class<Date> dateClass = Date.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), dateClass);
 assertEquals(StdKeySerializers.DEFAULT, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with custom key serializer for Date.
 */
 @Test
 public void testGetFallbackKeySerializerWithCustomSerializerForDate() throws JsonMappingException, IOException {
 Class<Date> dateClass = Date.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), dateClass);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with Calendar key type.
 */
 @Test
 public void testGetFallbackKeySerializerWithCalendarKeyType() throws JsonMappingException, IOException {
 Class<Calendar> calendarClass = Calendar.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), calendarClass);
 assertEquals(StdKeySerializers.DEFAULT, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getFallbackKeySerializer(SerializationConfig, Class)}
 * method with custom key serializer for Calendar.
 */
 @Test
 public void testGetFallbackKeySerializerWithCustomSerializerForCalendar() throws JsonMappingException, IOException {
 Class<Calendar> calendarClass = Calendar.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), calendarClass);
 assertNotNull(serializer);
 assertNotEquals(StdKeySerializers.DEFAULT, serializer);
 }

 /**
 * Test for {@link StdKeySerializers#getDefault()} method.
 */
 @Test
 public void testGetDefault() throws JsonMappingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }
}