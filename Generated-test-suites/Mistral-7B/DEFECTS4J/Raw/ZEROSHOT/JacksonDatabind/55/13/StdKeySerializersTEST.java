package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws Exception {
 // Test for null rawKeyType
 Object result = StdKeySerializers.getStdKeySerializer(null, null, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test for String rawKeyType
 result = StdKeySerializers.getStdKeySerializer(null, String.class, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), result.getClass());

 // Test for primitive types
 result = StdKeySerializers.getStdKeySerializer(null, int.class, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), result.getClass());

 // Test for Class type
 result = StdKeySerializers.getStdKeySerializer(null, Class.class, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.getFallbackKeySerializer(null, Class.class).getClass(), result.getClass());

 // Test for Date type
 result = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.getDefault().getClass(), result.getClass());

 // Test for Calendar type
 result = StdKeySerializers.getStdKeySerializer(null, Calendar.class, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.getDefault().getClass(), result.getClass());

 // Test for java.util.UUID type
 result = StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true);
 assertNotNull(result);
 assertEquals(StdKeySerializers.getDefault().getClass(), result.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 // Test for Enum type
 result = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
 assertNotNull(result);
 assertEquals(StdKeySerializers.new Default(Default.TYPE_ENUM, Enum.class).getClass(), result.getClass());

 // Test for null rawKeyType
 result = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertNull(result);
 }

 @Test
 public void testDefault() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefault();
 ObjectWriter objectWriter = jsonFactory.getObjectWriter();

 // Test for String key
 String key1 = "testKey1";
 ObjectNode rootNode = objectWriter.writeValueAsObject(new Object());
 rootNode.put(key1, "value1");
 String result1 = rootNode.get(key1).asText();
 assertEquals(key1, result1);

 // Test for Integer key
 int key2 = 1;
 ObjectNode rootNode2 = objectWriter.writeValueAsObject(new Object());
 rootNode2.put(key2, "value2");
 int result2 = rootNode2.get(key2).asInt();
 assertEquals(key2, result2);

 // Test for Date key
 Date dateKey = new Date();
 ObjectNode rootNode3 = objectWriter.writeValueAsObject(new Object());
 rootNode3.put(dateKey, "value3");
 assertEquals(dateKey.toString(), rootNode3.get(dateKey).asText());

 // Test for Calendar key
 Calendar calendarKey = Calendar.getInstance();
 ObjectNode rootNode4 = objectWriter.writeValueAsObject(new Object());
 rootNode4.put(calendarKey, "value4");
 assertEquals(calendarKey.getTimeInMillis() + "", rootNode4.get(calendarKey).asText());
 }
}