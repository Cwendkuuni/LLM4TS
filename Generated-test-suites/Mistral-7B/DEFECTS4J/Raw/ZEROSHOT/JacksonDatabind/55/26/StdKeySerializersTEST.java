package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer() throws IOException {
 // Test for null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, false);
 assertNull(serializer);

 // Test for String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

 // Test for Object class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Object.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test for Number rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), int.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test for Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), String[].class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());

 // Test for Date rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Date.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());

 // Test for Calendar rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Calendar.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());

 // Test for java.util.UUID rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), java.util.UUID.class, false);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer() throws IOException {
 // Test for null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertNull(serializer);

 // Test for int rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), int.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test for String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

 // Test for Enum rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), Enum.class);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testDefaultGetter() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 @Test
 public void testGetStdKeySerializerWithUseDefaultTrue() throws IOException {
 // Test for null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), null, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

 // Test for non-handled rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Long.class, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
}

 @Test
 public void testDynamicKeySerializer() throws IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 PropertySerializerMap map = new PropertySerializerMap();
 Object value = new Object() {};

 // Test for null value
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer.serialize(null, objectMapper.getJsonFactory().createGenerator(), objectMapper.getSerializerProvider()));

 // Test for non-handled value type
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), value.getClass());
 assertNull(serializer);

 // Test for String value
 assertEquals("value", serializer.serialize(value, objectMapper.getJsonFactory().createGenerator(), objectMapper.getSerializerProvider()));

 // Test for dynamic serializer creation
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), value.getClass(), false);
 assertEquals("value", serializer.serialize(value, objectMapper.getJsonFactory().createGenerator(), objectMapper.getSerializerProvider()));

 // Test for map serializer creation
 map = new PropertySerializerMap(objectMapper.getDefaultSerializerProvider());
 serializer = map.serializerFor(value.getClass());
 assertEquals("value", serializer.serialize(value, objectMapper.getJsonFactory().createGenerator(), objectMapper.getSerializerProvider()));
 }
}