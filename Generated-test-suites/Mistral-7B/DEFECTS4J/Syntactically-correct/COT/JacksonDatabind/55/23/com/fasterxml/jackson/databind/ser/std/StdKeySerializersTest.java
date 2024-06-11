package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind. JaneDoClass;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertySerializerMap;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializersTest {

 private SerializationConfig config;
 private StdKeySerializers serializers;
 private PropertySerializerMap dynamicSerializers;

 @Before
 public void setup() {
 config = mock(SerializationConfig.class);
 serializers = new StdKeySerializers();
 dynamicSerializers = PropertySerializerMap.emptyForProperties();
 }

 @Test
 public void testGetStdKeySerializer() {
 // Test when rawKeyType is null
 JsonSerializer<Object> result = serializers.getStdKeySerializer(config, null, false);
 assertNull(result);

 // Test when rawKeyType is String
 result = serializers.getStdKeySerializer(config, String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

 // Test when rawKeyType is primitive or Number
 result = serializers.getStdKeySerializer(config, int.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);

 // Test when rawKeyType is Class
 result = serializers.getStdKeySerializer(config, Class.class, false);
 assertEquals(StdKeySerializers.DEFAULT, result);

 // Test when rawKeyType is Date
 result = serializers.getStdKeySerializer(config, Date.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, result._typeId);

 // Test when rawKeyType is Calendar
 result = serializers.getStdKeySerializer(config, Calendar.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, result._typeId);

 // Test when rawKeyType is UUID
 result = serializers.getStdKeySerializer(config, java.util.UUID.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, result._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer() {
 // Test when rawKeyType is Enum
 JsonSerializer<Object> result = serializers.getFallbackKeySerializer(config, Enum.class);
 assertEquals(StdKeySerializers.Dynamic.class, result.getClass());

 // Test when rawKeyType is not Enum
 result = serializers.getFallbackKeySerializer(config, java.lang.Integer.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testDefaultGetSerializer() {
 JsonSerializer<Object> result = serializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testDynamicKeySerializer() throws JsonMappingException, IOException {
 // Initialize dynamic serializers
 SerializerProvider provider = mock(SerializerProvider.class);
 JsonSerializer<Object> serializedValue = mock(JsonSerializer.class);
 when(provider.findValueSerializer(JaneDoClass.class)).thenReturn(serializedValue);
 dynamicSerializers = serializers.getFallbackKeySerializer(config, JaneDoClass.class)._dynamicSerializers;

 // Test serialization
 Object key = new JaneDoClass();
 JsonGenerator generator = mock(JsonGenerator.class);
 serializers.getStdKeySerializer(config, JaneDoClass.class, false).serialize(key, generator, provider);
 verify(generator).writeFieldName(eq(key.getClass().getName()));
 }

 @Test(expected = JsonMappingException.class)
 public void testDynamicKeySerializer_NullProvider() throws JsonMappingException, IOException {
 Object key = new JaneDoClass();
 serializers.getStdKeySerializer(config, JaneDoClass.class, false).serialize(key, mock(JsonGenerator.class), null);
 }

 @Test
 public void testStringKeySerializer() throws IOException {
 // Test serialization
 String key = "test";
 JsonGenerator generator = mock(JsonGenerator.class);
 serializers.getStringKeySerializer().serialize(key, generator, new SerializerProvider() {});
 verify(generator).writeFieldName(eq("test"));
 }
}