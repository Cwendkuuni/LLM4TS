package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializersTest {

 @SuppressWarnings("unchecked")
 @InjectMocks
 private StdKeySerializers stdKeySerializers;

 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;
 private SerializerProvider serializerProvider;
 private PropertySerializerMap propertySerializerMap;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 serializerProvider = objectMapper.getSerializerProvider();
 propertySerializerMap = new PropertySerializerMap(serializerProvider);
 }

 @Test
 public void testGetStdKeySerializer() throws IOException {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expected = stdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> actual = stdKeySerializers.getStdKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType, false);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StringKeySerializer.class));
 Mockito.verify(serializerProvider, Mockito.times(1)).findValueSerializer(String.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStdKeySerializerWithNullRawKeyType() throws IOException {
 JsonSerializer<Object> actual = stdKeySerializers.getStdKeySerializer(Mockito.mock(SerializationConfig.class), null, false);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Dynamic.class));
 }

 @Test
 public void testGetStdKeySerializerWithObjectRawKeyType() throws IOException {
 JsonSerializer<Object> actual = stdKeySerializers.getStdKeySerializer(Mockito.mock(SerializationConfig.class), Object.class, false);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Dynamic.class));
 }

 @Test
 public void testGetStdKeySerializerWithDateRawKeyType() throws IOException {
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> expected = stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_DATE, rawKeyType);
 JsonSerializer<Object> actual = stdKeySerializers.getStdKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType, false);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Default.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStdKeySerializerWithCalendarRawKeyType() throws IOException {
 Class<?> rawKeyType = Calendar.class;
 JsonSerializer<Object> expected = stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CALENDAR, rawKeyType);
 JsonSerializer<Object> actual = stdKeySerializers.getStdKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType, false);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Default.class));
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializerWithNullSerializationConfig() {
 stdKeySerializers.getStdKeySerializer(null, String.class, false);
 }

 @Test
 public void testGetFallbackKeySerializerWithNullRawKeyType() {
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), null);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class));
 }

 @Test
 public void testGetFallbackKeySerializerWithEmptyClassArray() {
 Class<?>[] rawKeyTypes = {};
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), null);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class));
 }

 @Test
 public void testGetFallbackKeySerializerWithStringRawKeyType() throws IOException {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expected = stdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StringKeySerializer.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithEnumRawKeyType() throws IOException {
 Class<?> rawKeyType = Enum.class;
 JsonSerializer<Object> expected = stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_ENUM, rawKeyType);
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Default.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithClassRawKeyType() throws IOException {
 Class<?> rawKeyType = Class.class;
 JsonSerializer<Object> expected = stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CLASS, rawKeyType);
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Default.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithDateRawKeyType() throws IOException {
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> expected = stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_DATE, rawKeyType);
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Default.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithCalendarRawKeyType() throws IOException {
 Class<?> rawKeyType = Calendar.class;
 JsonSerializer<Object> expected = stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CALENDAR, rawKeyType);
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType);
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.Default.class));
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFallbackKeySerializerWithUnknownRawKeyType() throws IOException {
 Class<?> rawKeyType = Float.class;
 JsonSerializer<Object> actual = stdKeySerializers.getFallbackKeySerializer(Mockito.mock(SerializationConfig.class), rawKeyType);
 assertNull(actual);
 }

 @Test
 public void testGetDefaultKeySerializer() throws IOException {
 JsonSerializer<Object> actual = stdKeySerializers.getDefault();
 assertNotNull(actual);
 assertNotNull(actual.asInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class));
 }

}