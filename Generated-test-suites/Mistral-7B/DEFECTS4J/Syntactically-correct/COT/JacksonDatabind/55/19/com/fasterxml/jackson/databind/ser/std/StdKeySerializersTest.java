package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ StdKeySerializers.class })
public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetStdKeySerializerWithNullType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 JsonSerializer<Object> expectedSerializer = new Dynamic();
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getStdKeySerializer(config, null, false);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getStdKeySerializer(config, null, false);
 PowerMockito.verifyStatic(Dynamic.class);
 new Dynamic();
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetStdKeySerializerWithObjectType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Object.class;
 JsonSerializer<Object> expectedSerializer = new Dynamic();
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 PowerMockito.verifyStatic(Dynamic.class);
 new Dynamic();
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetStdKeySerializerWithDateType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> expectedSerializer = new Default(Default.TYPE_DATE, rawKeyType);
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 PowerMockito.verifyStatic(Default.class);
 new Default(Default.TYPE_DATE, rawKeyType);
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetStdKeySerializerWithCalendarType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Calendar.class;
 JsonSerializer<Object> expectedSerializer = new Default(Default.TYPE_CALENDAR, rawKeyType);
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 PowerMockito.verifyStatic(Default.class);
 new Default(Default.TYPE_CALENDAR, rawKeyType);
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetStdKeySerializerWithDynamicType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = java.util.UUID.class;
 JsonSerializer<Object> expectedSerializer = new Default(Default.TYPE_TO_STRING, rawKeyType);
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 PowerMockito.verifyStatic(Default.class);
 new Default(Default.TYPE_TO_STRING, rawKeyType);
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Enum.class;
 JsonSerializer<Object> expectedSerializer = new Dynamic();
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 PowerMockito.verifyStatic(Dynamic.class);
 new Dynamic();
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetFallbackKeySerializerWithClassType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Class.class;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 PowerMockito.verifyStatic(Default.class);
 StdKeySerializers.DEFAULT_KEY_SERIALIZER;
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testGetFallbackKeySerializerWithNullType() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;
 JsonSerializer<Object> resultSerializer = StdKeySerializers.getFallbackKeySerializer(config, null);
 assertNotNull(resultSerializer);
 PowerMockito.verifyStatic(StdKeySerializers.class);
 StdKeySerializers.getFallbackKeySerializer(config, null);
 PowerMockito.verifyStatic(Default.class);
 StdKeySerializers.DEFAULT_KEY_SERIALIZER;
 assertNotNull(resultSerializer);
 assertSame(expectedSerializer, resultSerializer);
 }

 @Test
 public void testDynamicSerializedWithSerializerMap() throws JsonMappingException, IOException {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 PropertySerializerMap map = Mockito.mock(PropertySerializerMap.class);
 Class<?> cls = EnumSet.class;
 JsonSerializer<Object> serializer = new DefaultSerializer();
 SerializerProvider provider = Mockito.mock(SerializerProvider.class);
 PowerMockito.when(map.serializerFor(cls)).thenReturn(serializer);
 PowerMockito.when(config.getSerializerProvider()).thenReturn(provider);
 PowerMockito.when(provider.getSerializers()).thenReturn(new DefaultSerializers.SerializerProviderDefault());
 JsonGenerator g = Mockito.mock(JsonGenerator.class);
 StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
 dynamic.serialize(new Object(), g, config);
 PowerMockito.verifyStatic(map.class);
 map.findAndAddKeySerializer(cls, config, null);
 }
}