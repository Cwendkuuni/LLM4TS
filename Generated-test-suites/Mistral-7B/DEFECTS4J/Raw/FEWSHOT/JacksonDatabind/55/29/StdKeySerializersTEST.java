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

 @Test
 public void testgetDefaultKeySerializer() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(null, null, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 @Test
 public void testGetStdKeySerializerWithNullRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, false);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializerWithStringRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 @Test
 public void testGetStdKeySerializerWithPrimitiveOrNumberRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, int.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 @Test
 public void testGetStdKeySerializerWithClassRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Class.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializerWithDateRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializerWithCalendarRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Calendar.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializerWithUUIDRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializerWithNullRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 @Test
 public void testGetFallbackKeySerializerWithEnumRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializerWithOtherRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Integer.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 @Test
 public void testDynamicKeySerializer() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(null, null, false);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

 Object key1 = new Object();
 JsonGenerator generator = createJsonGenerator();
 serializer.serialize(key1, generator, new JsonSerializer.DefaultSerializerProvider());
 assertEquals("{}", generator.getOutputValue());

 // Add a serializer for Integer key type
 PropertySerializerMap map = new PropertySerializerMap();
 JsonSerializer<Object> intSerializer = new SimpleIntSerializer();
 map.addSerializer(Integer.class, intSerializer);

 serializer = _findAndAddDynamic(map, Integer.class, new JsonSerializer.DefaultSerializerProvider());
 Object key2 = new Integer(10);
 generator = createJsonGenerator();
 serializer.serialize(key2, generator, new JsonSerializer.DefaultSerializerProvider());
 assertEquals("10", generator.getOutputValue());
 }

 @Test
 public void testStringKeySerializer() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(null, null, false);
 assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());

 Object key = new Object();
 JsonGenerator generator = createJsonGenerator();
 serializer.serialize(key, generator, new JsonSerializer.DefaultSerializerProvider());
 assertEquals("\"\"", generator.getOutputValue());
 }

 private static JsonGenerator createJsonGenerator() throws JsonProcessingException {
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 return writer.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 }

 private static JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map,
 Class<?> type, SerializerProvider provider) throws JsonMappingException {
 PropertySerializerMap.SerializerAndMapResult result = map.findAndAddKeySerializer(type, provider, null);
 return result.serializer;
 }
}