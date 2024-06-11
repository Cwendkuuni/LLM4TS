package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import java.io.IOException;
import java.util.*;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() {
 Class<?> types[] = {Object.class, String.class, int.class, long.class, Class.class, Date.class, Calendar.class, java.util.UUID.class};
 boolean useDefault[] = {false, false, false, false, false, false, false, false};
 SerializationConfig config = new SerializationConfig();
 for (int i = 0; i < types.length; i++) {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, types[i], useDefault[i]);
 assertNotNull(serializer);
 }
 }

 @Test
 public void testGetStdKeySerializer_NullType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), null, false);
 assertNull(serializer);
 }

 @Test
 public void testGetStdKeySerializer_UseDefault() {
 SerializationConfig config = new SerializationConfig();
 Class<?> types[] = {Integer.TYPE, Long.TYPE, java.util.UUID.class};
 JsonSerializer<Object> serializer = null;
 for (Class<?> type : types) {
 serializer = StdKeySerializers.getStdKeySerializer(config, type, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }
 }

 @Test
 public void testGetFallbackKeySerializer() {
 Class<?> types[] = {Object.class, Enum.class, String[].class};
 JsonSerializer<Object> serializer = null;
 SerializationConfig config = new SerializationConfig();
 for (Class<?> type : types) {
 serializer = StdKeySerializers.getFallbackKeySerializer(config, type);
 assertNotNull(serializer);
 }
 }

 @Test
 public void testGetFallbackKeySerializer_NullType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), null);
 assertNull(serializer);
 }

 @Test
 public void testDefaultKeySerializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 String result = mapper.writeValueAsString(new SimpleObject(model));
 assertEquals("\"class com.fasterxml.jackson.databind.ser.std.SimpleSerializableModel\"", result);
 }

 @Test
 public void testDefaultStringSerializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 String key = "test";
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 String result = mapper.writeValueAsString(new SimpleObject(model, key));
 assertEquals("\"test\"", result);
 }

 @Test
 public void testStdKeySerializer_Date() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Date.class, false);
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 String result = mapper.writeValueAsString(new SimpleObject(model, date));
 assertEquals("\"2023-03-17T12:34:56.789Z\"", result);
 }

 @Test
 public void testStdKeySerializer_Calendar() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Calendar.class, false);
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 String result = mapper.writeValueAsString(new SimpleObject(model, calendar));
 assertEquals("\"2023-03-17T12:34:56Z\"", result);
 }

 @Test
 public void testStdKeySerializer_Enum() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 SimpleEnum enumValue = SimpleEnum.VALUE1;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), enumValue.getClass(), false);
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 String result = mapper.writeValueAsString(new SimpleObject(model, enumValue));
 assertEquals("\"VALUE1\"", result);
 }

 @Test
 public void testStdKeySerializer_Class() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 Class<?> clazz = String[].class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), clazz, false);
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 String result = mapper.writeValueAsString(new SimpleObject(model, clazz));
 assertEquals("\"[Ljava.lang.String;\"", result);
 }

 @Test
 public void testDynamicKeySerializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleSerializableModel model = new SimpleSerializableModel();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 PropertySerializerMap propertySerializerMap = new PropertySerializerMap();
 mapper.setSerializerFactory(new DefaultSerializerFactory(serializer));
 SimpleSerializableDynamicModel dynamicModel = new SimpleSerializableDynamicModel();
 String result = mapper.writeValueAsString(new SimpleObject(model, dynamicModel));
 String expected = "{\"class\":\"com.fasterxml.jackson.databind.ser.std.SimpleSerializableDynamicModel\"}";
 assertEquals(expected, result);
 }
}