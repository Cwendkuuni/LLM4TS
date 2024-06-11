package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

public class StdKeySerializersTest {

 private static JsonSerializer<Object> getSerializer(SerializationConfig config, Class<?> rawKeyType) {
 return StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 }

 @Test
 public void testGetStdKeySerializer_NullRawKeyType() {
 JsonSerializer<Object> serializer = getSerializer(null, null);
 assertNull(serializer);
 }

 @Test
 public void testGetStdKeySerializer_StringType() {
 JsonSerializer<Object> serializer = getSerializer(null, String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_ObjectType() {
 JsonSerializer<Object> serializer = getSerializer(null, Object.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_PrimitiveType() {
 JsonSerializer<Object> serializer = getSerializer(null, int.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_NumberType() {
 JsonSerializer<Object> serializer = getSerializer(null, Double.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_ClassType() {
 JsonSerializer<Object> serializer = getSerializer(null, Class.class);
 assertEquals(StdKeySerializers.DEFAULT.getClass(), serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_DateType() {
 JsonSerializer<Object> serializer = getSerializer(null, Date.class);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testGetStdKeySerializer_CalendarType() {
 JsonSerializer<Object> serializer = getSerializer(null, Calendar.class);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testGetStdKeySerializer_UUIDType() {
 JsonSerializer<Object> serializer = getSerializer(null, UUID.class);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testGetStdKeySerializer_EnumType() {
 JsonSerializer<Object> serializer = getSerializer(null, Enum.class);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetStdKeySerializer_CustomType() {
 Class<?> customType = new CustomClass();
 JsonSerializer<Object> serializer = getSerializer(null, customType);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer_NullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer_StringType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_ObjectType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Object.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_PrimitiveType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, int.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_NumberType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Double.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_ClassType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Class.class);
 assertEquals(StdKeySerializers.DEFAULT.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_DateType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Date.class);
 assertEquals(StdKeySerializers.DEFAULT.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer_CalendarType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Calendar.class);
 assertEquals(StdKeySerializers.DEFAULT.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer_UUIDType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, UUID.class);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer_EnumType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer_CustomType() {
 Class<?> customType = new CustomClass();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, customType);
 assertNull(serializer);
 }

 @Test
 public void testDynamicKeySerializer() throws IOException {
 SerializationConfig config = new ObjectMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, CustomClass.class);
 assertNull(serializer);

 PropertySerializerMap map = new PropertySerializerMap();
 JsonSerializer<Object> dynamicSerializer = getSerializer(config, CustomClass.class);
 map.findAndAddKeySerializer(CustomClass.class, new JsonSerializer.Provider() {
 @Override
 public JsonSerializer<?> getSerializerProvider() {
 return new JsonSerializer.Provider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> valueType) {
 return dynamicSerializer;
 }
 };
 }
 }, null);

 assertEquals(dynamicSerializer.getClass(), map.findAndAddKeySerializer(CustomClass.class, null, null).serializer.getClass());
 }

 @Test
 public void testEnumKeySerializer() throws IOException {
 SerializationConfig config = new ObjectMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = getSerializer(config, CustomEnum.class);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

 CustomEnum customEnum = CustomEnum.FOO;
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.valueToTree(customEnum).toString();
 assertEquals("FOO", json);
 }

 // Add more test cases for other methods and functionalities as needed

}

 class CustomClass {}

 enum CustomEnum {
 FOO, BAR
 }