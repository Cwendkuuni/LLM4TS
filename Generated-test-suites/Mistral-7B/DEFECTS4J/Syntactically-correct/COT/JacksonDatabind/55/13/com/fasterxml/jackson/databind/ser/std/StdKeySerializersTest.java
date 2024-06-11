package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class StdKeySerializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testGetStdKeySerializer() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 String.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StringKeySerializer.class, serializer);
 }

 @Test
 public void testGetStdKeySerializerForDate() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Date.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);
 }

 @Test
 public void testGetStdKeySerializerForCalendar() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Calendar.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);
 }

 @Test
 public void testGetStdKeySerializerForClass() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Class.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);
 }

 @Test
 public void testGetStdKeySerializerForNull() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 null, false);
 assertNull(serializer);
 }

 @Test
 public void testGetStdKeySerializerForUnknownType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Integer.TYPE, false);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Integer.TYPE);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerForEnum() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Enum.class);
 assertInstanceOf(Dynamic.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializerForClassWithNoMatch() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 String[].class);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerForNullType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 null);
 assertNull(serializer);
 }

 @Test
 public void testDynamicSerialization() throws JsonProcessingException {
 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(provider.getConfig(),
 MyDynamicClass.class, true);
 assertNotNull(serializer);
 assertInstanceOf(Dynamic.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 Calendar calendar = Calendar.getInstance();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 generator.setSerializerProvider(provider);
 serializer.serialize(calendar, generator, provider);
 generator.flush();
 }

 @Test
 public void testDynamicSerializationWithMultipleInstances() throws JsonProcessingException {
 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Object> serializer1 = StdKeySerializers.getStdKeySerializer(provider.getConfig(),
 MyDynamicClass.class, true);
 JsonSerializer<Object> serializer2 = StdKeySerializers.getStdKeySerializer(provider.getConfig(),
 MyDynamicClass.class, true);
 assertNotNull(serializer1);
 assertInstanceOf(Dynamic.class, serializer1);
 assertInstanceOf(Dynamic.class, serializer2);
 assertNotSame(serializer1, serializer2);

 ObjectMapper mapper = new ObjectMapper();
 Calendar calendar1 = Calendar.getInstance();
 Calendar calendar2 = Calendar.getInstance();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 generator.setSerializerProvider(provider);
 serializer1.serialize(calendar1, generator, provider);
 serializer2.serialize(calendar2, generator, provider);
 generator.flush();
 }

 @Test
 public void testDynamicSerializationWithMissingSerializer() throws JsonProcessingException {
 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(provider.getConfig(),
 MyMissingClass.class, true);
 assertNull(serializer);
 }

 @Test
 public void testDynamicSerializationWithCustomSerializer() throws JsonProcessingException {
 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Object> customSerializer = new MyCustomSerializer();
 PropertySerializerMap map = new PropertySerializerMap();
 map.addSerializer(MyCustomClass.class, customSerializer);
 provider.setActiveSerializerMap(map);
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(provider.getConfig(),
 MyCustomClass.class, true);
 assertInstanceOf(customSerializer.getClass(), serializer);
 }

 @Test
 public void testDynamicSerializationWithCustomSerializerRegistered() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerSerializer(MyCustomClass.class, new MyCustomSerializer());
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
 MyCustomClass.class, true);
 assertInstanceOf(MyCustomSerializer.class, serializer);
 }

 @Test
 public void testStringKeySerializerSerialization() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 String.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StringKeySerializer.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize("test", generator, objectMapper.getSerializationConfig());
 generator.flush();
 }

 @Test
 public void testDefaultSerializationForDate() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Date.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(new Date(), generator, objectMapper.getSerializationConfig());
 generator.flush();
 }

 @Test
 public void testDefaultSerializationForCalendar() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Calendar.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 Calendar calendar = Calendar.getInstance();
 serializer.serialize(calendar, generator, objectMapper.getSerializationConfig());
 generator.flush();
 }

 @Test
 public void testDefaultSerializationForClass() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Class.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(Class.class, generator, objectMapper.getSerializationConfig());
 generator.flush();
 }

 @Test
 public void testDefaultSerializationForEnum() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 MyEnum.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(MyEnum.VALUE1, generator, objectMapper.getSerializationConfig());
 generator.flush();
 }

 @Test
 public void testToStringSerializationForUuid() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 java.util.UUID.class, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(java.util.UUID.randomUUID(), generator, objectMapper.getSerializationConfig());
 generator.flush();
 }

 @Test
 public void testDefaultSerializationForUnknownType() throws JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 MyUnknownClass.class, false);
 assertNull(serializer);
 }

 private static class MyDynamicClass {
 }

 private static class MyMissingClass {
 }

 private static class MyCustomClass {
 }

 private static class MyCustomSerializer extends JsonSerializer<MyCustomClass> {
 @Override
 public void serialize(MyCustomClass value, JsonGenerator g, SerializerProvider provider)
 throws IOException {
 g.writeFieldName("custom");
 }
 }
}