package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

 @Mock
 private SerializerProvider serializerProvider;

 @Mock
 private JsonGenerator jsonGenerator;

 @Mock
 private SerializationConfig config;

 @InjectMocks
 private StdKeySerializers stdKeySerializers;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetStdKeySerializerForNullType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, null, false);
 assertNull(result);
 }

 @Test
 public void testGetStdKeySerializerForStringType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, String.class, false);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
 }

 @Test
 public void testGetStdKeySerializerForNumberType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, int.class, false);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetStdKeySerializerForClassType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.class, false);
 assertEquals(StdKeySerializers.DEFAULT, result);
 }

 @Test
 public void testGetStdKeySerializerForDateType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Date.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, result._typeId);
 }

 @Test
 public void testGetStdKeySerializerForCalendarType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, result._typeId);
 }

 @Test
 public void testGetStdKeySerializerForUUIDType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, UUID.class, false);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, result._typeId);
 }

 @Test
 public void testGetFallbackKeySerializerForNullType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, null);
 assertNull(result);
 }

 @Test
 public void testGetFallbackKeySerializerForStringType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
 }

 @Test
 public void testGetFallbackKeySerializerForNumberType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, int.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetFallbackKeySerializerForClassType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, Class.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetFallbackKeySerializerForDateType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, Date.class);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, result._typeId);
 }

 @Test
 public void testGetFallbackKeySerializerForCalendarType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, result._typeId);
 }

 @Test
 public void testGetFallbackKeySerializerForUUIDType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, UUID.class);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, result._typeId);
 }

 @Test
 public void testGetFallbackKeySerializerForEnumType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, Enum.class);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, result._typeId);
 }

 @Test
 public void testGetFallbackKeySerializerForUnknownType() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, new Object(){});
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetDefaultKeySerializer() throws JsonProcessingException {
 JsonSerializer<Object> result = stdKeySerializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testDynamicKeySerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = mapper.getSerializerProvider();
 when(provider.defaultSerializeDateKey((Date) anyObject(), jsonGenerator)).thenReturn(null);
 when(provider.defaultSerializeDateKey((long) anyLong(), jsonGenerator)).thenReturn("test");
 when(config.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
 when(config.findValueSerializer(String.class)).thenReturn(stdKeySerializers.StringKeySerializer);

 JsonSerializer<Object> keySerializer = stdKeySerializers.getStdKeySerializer(config, String.class.getClassLoader().getResource("Test.class").getName(), false);
 assertEquals(StdKeySerializers.Dynamic.class, keySerializer.getClass());
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 Date date = new Date();
 when(serializerProvider.defaultSerializeDateKey(date, jsonGenerator)).thenReturn("testDate");

 Calendar calendar = Calendar.getInstance();
 when(serializerProvider.defaultSerializeDateKey(calendar.getTimeInMillis(), jsonGenerator)).thenReturn("testCalendar");

 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

 when(config.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(true);
 JsonSerializer<Object> keySerializer = stdKeySerializers.getStdKeySerializer(config, Date.class, false);
 keySerializer.serialize(date, jsonGenerator, serializerProvider);
 assertEquals("testDate", jsonGenerator.getOutputBuffer().toString());

 keySerializer = stdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
 keySerializer.serialize(calendar, jsonGenerator, serializerProvider);
 assertEquals("testCalendar", jsonGenerator.getOutputBuffer().toString());

 keySerializer = stdKeySerializers.getStdKeySerializer(config, String.class, false);
 keySerializer.serialize("test", jsonGenerator, serializerProvider);
 assertEquals("test", jsonGenerator.getOutputBuffer().toString());
 }

}