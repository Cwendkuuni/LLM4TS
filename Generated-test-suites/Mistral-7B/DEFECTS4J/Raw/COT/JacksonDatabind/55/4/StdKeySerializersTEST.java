package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializersTest {

 // Add basic test cases for each method

 @Test
 public void testGetStdKeySerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 boolean useDefault = false;
 JsonSerializer<Object> expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> actual = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 assertSame(expected, actual);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> actual = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertSame(expected, actual);
 }

 @Test
 public void testGetDefault() throws Exception {
 JsonSerializer<Object> expected = StdKeySerializers.DEFAULT_KEY_SERIALIZER;
 JsonSerializer<Object> actual = StdKeySerializers.getDefault();
 assertSame(expected, actual);
 }

 // Add test cases for edge cases and exceptions

 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullConfig() throws Exception {
 SerializationConfig config = null;
 Class<?> rawKeyType = String.class;
 boolean useDefault = false;
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullRawKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = null;
 boolean useDefault = false;
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFallbackKeySerializerWithNullRawKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = null;
 JsonSerializer<Object> actual = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetStdKeySerializerWithInvalidRawKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Integer.class;
 boolean useDefault = false;
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 }

 @Test
 public void testGetFallbackKeySerializerWithEnumClass() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Enum.class;
 JsonSerializer<Object> actual = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertSame(StdKeySerializers.new Dynamic(), actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithDynamicKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Object.class;
 JsonSerializer<Object> actual = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithNumberClass() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Integer.class;
 JsonSerializer<Object> actual = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(actual);
 }

 // Test methods in inner classes

 @Test
 public void testDynamicSerialize() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 Object value = new Object() {
 };
 JsonSerializer<Object> serializer = StdKeySerializers.Dynamic.class.getDeclaredConstructor().newInstance();
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 serializer.serialize(value, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeFieldName("");
 }

 @Test
 public void testDefaultSerializeDateKey() throws Exception {
 Date date = new Date();
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(date, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeNumberValue(date.getTime());
 }

 @Test
 public void testDefaultSerializeDateKeyWithNullProvider() throws Exception {
 Date date = new Date();
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(date, null, null);
 }

 @Test
 public void testDefaultSerializeCalendarKey() throws Exception {
 Calendar calendar = Calendar.getInstance();
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(calendar, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeNumberValue(calendar.getTimeInMillis());
 }

 @Test
 public void testDefaultSerializeEnumKey() throws Exception {
 Enum<?> enumValue = Enum.valueOf(Enum.class, "TEST");
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 config.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(enumValue, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeStringValue(enumValue.name());
 }

 @Test
 public void testDefaultSerializeEnumKeyWithToStringEnabled() throws Exception {
 Enum<?> enumValue = Enum.valueOf(Enum.class, "TEST");
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 config.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(enumValue, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeStringValue(enumValue.toString());
 }

 @Test
 public void testDefaultSerializeClassKey() throws Exception {
 Class<?> clazz = String.class;
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(clazz, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeFieldName(clazz.getName());
 }

 @Test
 public void testDefaultSerializeToStringKey() throws Exception {
 Object value = new Object() {
 };
 ObjectMapper objectMapper = new ObjectMapper();
 SerializationConfig config = objectMapper.getSerializationConfig();
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 JsonGenerator generator = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Default.DEFAULT_SERIALIZER.serialize(value, generator, provider);
 Mockito.verify(generator, Mockito.times(1)).writeFieldName(value.toString());
 }
}