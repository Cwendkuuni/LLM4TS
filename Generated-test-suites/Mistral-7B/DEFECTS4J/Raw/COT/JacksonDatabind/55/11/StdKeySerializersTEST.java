package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.PropertySerializerMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StdKeySerializers.class})
public class StdKeySerializersTest {

 // Test getStdKeySerializer method with valid arguments
 @Test
 public void testGetStdKeySerializerWithValidArguments() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 boolean useDefault = false;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 assertEquals(expectedSerializer, actualSerializer);
 }

 // Test getStdKeySerializer method with null rawKeyType
 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullRawKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 boolean useDefault = false;
 StdKeySerializers.getStdKeySerializer(config, null, useDefault);
 }

 // Test getStdKeySerializer method with Object.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithObjectRawKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 boolean useDefault = false;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.new Dynamic();
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getStdKeySerializer(config, Object.class, useDefault);
 assertEquals(expectedSerializer, actualSerializer);
 }

 // Test getStdKeySerializer method with custom type
 @Test
 public void testGetStdKeySerializerWithCustomType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = MyCustomClass.class;
 boolean useDefault = false;
 JsonSerializer<Object> expectedSerializer = Mockito.mock(JsonSerializer.class);
 PowerMockito.when(StdKeySerializers.getSerializerFor(Mockito.eq(rawKeyType), Mockito.eq(config))).thenReturn(expectedSerializer);
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 assertEquals(expectedSerializer, actualSerializer);
 }

 // Test getStdKeySerializer method with useDefault as true and no matching serializer
 @Test
 public void testGetStdKeySerializerWithUseDefaultAsTrueAndNoMatchingSerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = MyCustomClass.class;
 boolean useDefault = true;
 PowerMockito.when(StdKeySerializers.getSerializerFor(Mockito.eq(rawKeyType), Mockito.eq(config))).thenReturn(null);
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 assertNull(actualSerializer);
 }

 // Test getStdKeySerializer method with useDefault as false and no matching serializer
 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithUseDefaultAsFalseAndNoMatchingSerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = MyCustomClass.class;
 boolean useDefault = false;
 PowerMockito.when(StdKeySerializers.getSerializerFor(Mockito.eq(rawKeyType), Mockito.eq(config))).thenReturn(null);
 StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
 }

 // Test getFallbackKeySerializer method with valid arguments
 @Test
 public void testGetFallbackKeySerializerWithValidArguments() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = MyCustomClass.class;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.new Default(Default.TYPE_CLASS, rawKeyType.getClass());
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expectedSerializer, actualSerializer);
 }

 // Test getFallbackKeySerializer method with null rawKeyType
 @Test(expected = IllegalArgumentException.class)
 public void testGetFallbackKeySerializerWithNullRawKeyType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 StdKeySerializers.getFallbackKeySerializer(config, null);
 }

 // Test getFallbackKeySerializer method with Enum type
 @Test
 public void testGetFallbackKeySerializerWithEnumType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Enum.class;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.new Dynamic();
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expectedSerializer, actualSerializer);
 }

 // Test getFallbackKeySerializer method with Class type
 @Test
 public void testGetFallbackKeySerializerWithClassType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Class.class;
 JsonSerializer<Object> expectedSerializer = StdKeySerializers.getDefault();
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expectedSerializer, actualSerializer);
 }

 // Test getFallbackKeySerializer method with other types
 @Test
 public void testGetFallbackKeySerializerWithOtherTypes() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> actualSerializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(actualSerializer);
 }

 // Test serialize method of Default class with Date type
 @Test
 public void testSerializeWithDate() throws Exception {
 Date date = new Date();
 JsonSerializer<Object> serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator generator = provider.getJsonGenerator(new StringWriter());
 serializer.serialize(date, generator, provider);
 String expectedOutput = "\"\""; // empty string is used as key for Date serialization
 String actualOutput = generator.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 // Test serialize method of Default class with Calendar type
 @Test
 public void testSerializeWithCalendar() throws Exception {
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator generator = provider.getJsonGenerator(new StringWriter());
 serializer.serialize(calendar, generator, provider);
 String expectedOutput = "\"\""; // empty string is used as key for Calendar serialization
 String actualOutput = generator.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 // Test serialize method of Dynamic class with custom type
 @Test
 public void testSerializeWithCustomType() throws Exception {
 MyCustomClass customClass = new MyCustomClass();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator generator = provider.getJsonGenerator(new StringWriter());
 serializer.serialize(customClass, generator, provider);
 String expectedOutput = "\"MyCustomClass\"";
 String actualOutput = generator.toString();
 assertEquals(expectedOutput, actualOutput);
 }
}

class MyCustomClass {
 @JsonSerialize(using = StdKeySerializers.StringKeySerializer.class)
 String name;

 public String getName() {
 return name;
 }
}