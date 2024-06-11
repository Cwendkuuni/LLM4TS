package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializersTest {

 @InjectMocks
 private StdKeySerializers stdKeySerializers;

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer_NullRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, null, true);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_StringRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, String.class, true);
 assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_NumberRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, Double.class, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_ClassRawKeyType() throws Exception {
 Class<MyClass> rawKeyType = MyClass.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_DateRawKeyType() throws Exception {
 Class<Date> rawKeyType = Date.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_CalendarRawKeyType() throws Exception {
 Class<Calendar> rawKeyType = Calendar.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_UUIDRawKeyType() throws Exception {
 Class<java.util.UUID> rawKeyType = java.util.UUID.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_BooleanRawKeyType() throws Exception {
 Class<Boolean> rawKeyType = Boolean.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_EnumRawKeyType() throws Exception {
 Class<MyEnum> rawKeyType = MyEnum.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, rawKeyType, true);
 assertEquals(StdKeySerializers.Default.class, serializer.getClass());
 }

 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializer_NullRawKeyType_UseDefaultFalse() throws Exception {
 JsonSerializer<Object> serializer = stdKeySerializers.getStdKeySerializer(null, null, false);
 }

 @Test
 public void testGetFallbackKeySerializer_NullRawKeyType() throws Exception {
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_StringRawKeyType() throws Exception {
 Class<String> rawKeyType = String.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_NumberRawKeyType() throws Exception {
 Class<Double> rawKeyType = Double.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_ClassRawKeyType() throws Exception {
 Class<MyClass> rawKeyType = MyClass.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_DateRawKeyType() throws Exception {
 Class<Date> rawKeyType = Date.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_CalendarRawKeyType() throws Exception {
 Class<Calendar> rawKeyType = Calendar.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_UUIDRawKeyType() throws Exception {
 Class<java.util.UUID> rawKeyType = java.util.UUID.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_EnumRawKeyType() throws Exception {
 Class<MyEnum> rawKeyType = MyEnum.class;
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFallbackKeySerializer_NullRawKeyType_UseDefaultTrue() throws Exception {
 JsonSerializer<Object> serializer = stdKeySerializers.getFallbackKeySerializer(null, null);
 }

 @Test
 public void testDynamicKeySerializer_SerializeNullValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonSerializer<Object> serializer = stdKeySerializers.DEFAULT_KEY_SERIALIZER;
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 Mockito.when(serializerProvider.getSerializerProvider()).thenReturn(serializerProvider);
 Mockito.when(serializerProvider.findValueSerializer(null, null)).thenReturn(serializer);

 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(null, jsonGenerator, serializerProvider);

 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 assertEquals("null", result);
 }

 @Test
 public void testDynamicKeySerializer_SerializePrimitiveNumber() throws Exception {
 int value = 123;
 JsonSerializer<Object> serializer = stdKeySerializers.getDefault();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 ObjectMapper objectMapper = new ObjectMapper();
 Mockito.when(serializerProvider.getSerializerProvider()).thenReturn(serializerProvider);
 Mockito.when(serializerProvider.findValueSerializer(Integer.TYPE, null)).thenReturn(serializer);

 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(value, jsonGenerator, serializerProvider);

 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 assertEquals("123", result);
 }

 @Test
 public void testDynamicKeySerializer_SerializeEnumValue() throws Exception {
 MyEnum enumValue = MyEnum.VALUE1;
 JsonSerializer<Object> serializer = stdKeySerializers.getDefault();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 ObjectMapper objectMapper = new ObjectMapper();
 Mockito.when(serializerProvider.getSerializerProvider()).thenReturn(serializerProvider);
 Mockito.when(serializerProvider.isEnabled(Mockito.any(SerializationFeature.class)))
 .thenReturn(true);
 Mockito.when(serializerProvider.findValueSerializer(MyEnum.class, null)).thenReturn(serializer);

 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(enumValue, jsonGenerator, serializerProvider);

 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 assertEquals("VALUE1", result);
 }

 @Test
 public void testDynamicKeySerializer_SerializeClassValue() throws Exception {
 MyClass classValue = new MyClass();
 JsonSerializer<Object> serializer = stdKeySerializers.getDefault();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 ObjectMapper objectMapper = new ObjectMapper();
 Mockito.when(serializerProvider.getSerializerProvider()).thenReturn(serializerProvider);
 Mockito.when(serializerProvider.findValueSerializer(MyClass.class, null)).thenReturn(serializer);

 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(classValue, jsonGenerator, serializerProvider);

 String result = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 assertEquals(classValue.getClass().getName(), result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDynamicKeySerializer_SerializeUnknownType() throws Exception {
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 ObjectMapper objectMapper = new ObjectMapper();
 Mockito.when(serializerProvider.getSerializerProvider()).thenReturn(serializerProvider);

 try {
 dynamicSerializer.serialize(new Object(), jsonGenerator, serializerProvider);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

}

class MyClass {}

enum MyEnum {
 VALUE1,
 VALUE2
}