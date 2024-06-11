package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

 /**
  * Test for getStdKeySerializer method with null configuration and null rawKeyType
  */
 @Test
 public void testGetStdKeySerializerNullConfigNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, false);
 assertNull(serializer);
 }

 /**
  * Test for getStdKeySerializer method with null configuration and valid rawKeyType
  */
 @Test
 public void testGetStdKeySerializerNullConfigValidRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 /**
  * Test for getStdKeySerializer method with valid configuration and null rawKeyType
  */
 @Test
 public void testGetStdKeySerializerValidConfigNullRawKeyType() {
 SerializationConfig config = new SerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
  * Test for getStdKeySerializer method with valid configuration and valid rawKeyType
  */
 @Test
 public void testGetStdKeySerializerValidConfigValidRawKeyType() {
 SerializationConfig config = new SerializationConfig();
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, rawKeyType), serializer);
 }

 /**
  * Test for getFallbackKeySerializer method with null configuration and null rawKeyType
  */
 @Test
 public void testGetFallbackKeySerializerNullConfigNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
  * Test for getFallbackKeySerializer method with null configuration and valid rawKeyType
  */
 @Test
 public void testGetFallbackKeySerializerNullConfigValidRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 /**
  * Test for getFallbackKeySerializer method with valid configuration and null rawKeyType
  */
 @Test
 public void testGetFallbackKeySerializerValidConfigNullRawKeyType() {
 SerializationConfig config = new SerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 /**
  * Test for getFallbackKeySerializer method with valid configuration and valid rawKeyType
  */
 @Test
 public void testGetFallbackKeySerializerValidConfigValidRawKeyType() {
 SerializationConfig config = new SerializationConfig();
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, rawKeyType), serializer);
 }

 /**
  * Test for Dynamic key serializer
  */
 @Test
 public void testDynamicKeySerializer() throws IOException {
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 SerializationConfig config = new SerializationConfig();
 PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
 JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

 // Test serialization of known types
 serializer.serialize(date, generator, config);
 assertEquals("\"2023-03-28T13:45:12.000Z\"", generator.getOutputContentAsString());
 serializer.serialize(calendar, generator, config);
 assertEquals("{\"class\":\"java.util.Calendar\",\"time\":\"2023-03-28T13:45:12Z\"}", generator.getOutputContentAsString());

 // Test serialization of unknown type
 Object unknown = new Object() {};
 generator.clearContent();
 serializer.serialize(unknown, generator, config);
 assertNull(generator.getOutputContentAsString());

 // Test serialization of previously unknown type with dynamic serializer
 JsonSerializer<Object> dynamicSerializer = map.serializerFor(unknown.getClass());
 assertEquals(StdKeySerializers.Dynamic.class, dynamicSerializer.getClass());
 serializer = dynamicSerializer;
 generator.clearContent();
 serializer.serialize(unknown, generator, config);
 assertEquals("\"com.example.MyUnknownClass\"" + System.lineSeparator() + "{\"class\":\"com.example.MyUnknownClass\"}", generator.getOutputContentAsString());
 }

 /**
  * Test for StringKeySerializer
  */
 @Test
 public void testStringKeySerializer() throws IOException {
 SerializationConfig config = new SerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
 assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());
 }
}