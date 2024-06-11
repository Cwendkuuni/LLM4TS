package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertySerializerMap;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializersTest {

 @Mock
 private SerializerProvider serializerProvider;

 @Mock
 private JsonGenerator jsonGenerator;

 @InjectMocks
 private StdKeySerializers stdKeySerializers;

 @Test
 public void testGetStdKeySerializer_String() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "String";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
 }

 @Test
 public void testGetStdKeySerializer_Date() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "java.util.Date";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.Default.class, result.getClass());
 }

 @Test
 public void testGetStdKeySerializer_Calendar() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "java.util.Calendar";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.Default.class, result.getClass());
 }

 @Test
 public void testGetStdKeySerializer_Number() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "int";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetStdKeySerializer_Object() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "java.lang.Object";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertNull(result);
 }

 @Test
 public void testGetStdKeySerializer_Enum() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "Enum";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, result.getClass().getField("_typeId").getInt(result));
 }

 @Test
 public void testGetStdKeySerializer_Class() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "Class";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.Default.class, result.getClass());
 }

 @Test
 public void testGetStdKeySerializer_UUID() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 String rawKeyType = "java.util.UUID";
 boolean useDefault = false;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), useDefault);

 // Then
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, result.getClass().getField("_typeId").getInt(result));
 }

 @Test
 public void testGetFallbackKeySerializer_Object() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Object.class;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

 // Then
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testGetFallbackKeySerializer_Class() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

 // Then
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
 }

 @Test
 public void testGetFallbackKeySerializer_Enum() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Enum.class;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

 // Then
 assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_Null() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = null;

 // When
 JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

 // Then
 assertNull(result);
 }

 @Test
 public void testSerialize_Default() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Date date = new Date();
 JsonSerializer<Object> keySerializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;

 // When
 keySerializer.serialize(date, jsonGenerator, serializerProvider);

 // Then
 Mockito.verify(jsonGenerator).writeFieldName(date.toString());
 }

 @Test
 public void testSerialize_Date() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Date date = new Date();
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);

 // When
 keySerializer.serialize(date, jsonGenerator, serializerProvider);

 // Then
 Mockito.verify(jsonGenerator).writeFieldName(date.toString());
 }

 @Test
 public void testSerialize_Calendar() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);

 // When
 keySerializer.serialize(calendar, jsonGenerator, serializerProvider);

 // Then
 Mockito.verify(jsonGenerator).writeFieldName(calendar.getTimeInMillis() + "");
 }

 @Test
 public void testSerialize_Enum() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Enum<?> enumValue = EnumSet.allOf(TestEnum.class).iterator().next();
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, enumValue.getClass(), false);

 // When
 keySerializer.serialize(enumValue, jsonGenerator, serializerProvider);

 // Then
 Mockito.verify(jsonGenerator, Mockito.atLeastOnce()).writeFieldName(enumValue.name());
 }

 @Test
 public void testSerialize_Class() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> clazz = String.class;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, clazz, false);

 // When
 keySerializer.serialize("test", jsonGenerator, serializerProvider);

 // Then
 Mockito.verify(jsonGenerator).writeFieldName("test");
 }

 @Test
 public void testSerialize_Dynamic() throws JsonMappingException, IOException {
 // Given
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Object object = new Object() {
 @Override
 public String toString() {
 return "test";
 }
 };
 JsonSerializer<Object> keySerializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;

 // When
 PropertySerializerMap propertySerializerMap = PropertySerializerMap.emptyForProperties();
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 keySerializer.serialize(object, jsonGenerator, serializerProvider);
 PropertySerializerMap m = (PropertySerializerMap) serializerProvider.getSerializers();

 // Then
 SerializerAndMapResult result = Mockito.spy(m.findAndAddKeySerializer(object.getClass(), serializerProvider, null));
 assertEquals("test", result.serializer.serializeValuesAsProperties().get(0).getValue()[0].asText());
 }
}

class TestEnum {
 @SuppressWarnings("unused")
 public enum TestEnum {
 A, B, C
}
}