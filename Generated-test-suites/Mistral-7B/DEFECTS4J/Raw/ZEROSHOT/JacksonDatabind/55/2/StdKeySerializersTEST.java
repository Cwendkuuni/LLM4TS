package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 private static ObjectMapper objectMapper;

 static {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializerDefault() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertNotNull(serializer);
 }

 @Test
 public void testGetStdKeySerializerString() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializerDate() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Date.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetStdKeySerializerCalendar() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Calendar.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetStdKeySerializerClass() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Class.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetStdKeySerializerEnum() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Enum.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetStdKeySerializerNumber() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Double.TYPE, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializerNull() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 null, false);
 assertNull(serializer);
 }

 @Test
 public void testGetStdKeySerializerDynamic() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 String[].class, false);
 assertNotEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializerDefault() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 null);
 assertNotNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerString() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializerDate() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Date.class);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetFallbackKeySerializerCalendar() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Calendar.class);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetFallbackKeySerializerClass() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Class.class);
 assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetFallbackKeySerializerEnum() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Enum.class);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer.getClass().getField("_typeId").getInt(null));
 }

 @Test
 public void testGetFallbackKeySerializerNumber() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Double.TYPE, false);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerNull() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 null);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerDynamic() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 String[].class, false);
 assertNull(serializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetStdKeySerializerUnknownClass() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Object[].class, false);
 fail("Expected JsonMappingException for unknown class");
 }

 @Test
 public void testSerializerWithDynamicSerializers() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 PropertySerializerMap propertySerializerMap = new PropertySerializerMap(mapper.getDefaultSerializationConfig());
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(),
 String[].class);
 propertySerializerMap.setSerializer(String[].class, serializer);
 mapper.setDefaultSerializerProvider(new SerializerProvider() {
 @Override
 public PropertySerializerMap getSerializerProvider() {
 return propertySerializerMap;
 }
 });
 ObjectNode node = writer.writeValueAsTree(new String[]{"test1", "test2"});
 String expected = "[\n  \"test1\",\n  \"test2\"\n]";
 String actual = node.toString();
 assertEquals(expected, actual);
 }
}