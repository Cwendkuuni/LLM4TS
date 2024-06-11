package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testGetStdKeySerializer() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 Class<?> types[] = { Object.class, String.class, int.class, long.class, Calendar.class, Date.class,
 UUID.class, Class.class, Enum.class, Enum.class, Enum.class };

 for (Class<?> type : types) {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), type, true);
 assertNotNull(serializer);
 System.out.println("Serializer for type " + type.getName() + " is " + serializer.getClass().getName());
 }
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 Class<?> types[] = { null, Object.class, String.class, int.class, long.class, Calendar.class, Date.class,
 UUID.class, Class.class, Enum.class, Enum.class, Enum.class };

 for (Class<?> type : types) {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), type);
 assertNotNull(serializer);
 System.out.println("Serializer for type " + (type != null ? type.getName() : "null") + " is " +
 serializer.getClass().getName());
 }
 }

 @Test
 public void testSerializeDefault() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 Object defaultSerializer = StdKeySerializers.getDefault();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(defaultSerializer, generator, mapper.getSerializerProvider());
 generator.flush();
 }

 @Test
 public void testSerializeString() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 String key = "testStringKey";
 JsonSerializer<Object> stringSerializer = StdKeySerializers.getDefaultStringSerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 stringSerializer.serialize(key, generator, mapper.getSerializerProvider());
 generator.flush();
 }

 @Test
 public void testSerializePrimitive() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 int intKey = 1;
 long longKey = 1L;
 Calendar calendarKey = Calendar.getInstance();
 Date dateKey = new Date();
 UUID uuidKey = UUID.randomUUID();

 JsonSerializer<Object> intSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, true);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 intSerializer.serialize(intKey, generator, mapper.getSerializerProvider());
 generator.flush();

 JsonSerializer<Object> longSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), long.class, true);
 JsonGenerator longGenerator = mapper.getFactory().createGenerator(System.out);
 longSerializer.serialize(longKey, longGenerator, mapper.getSerializerProvider());
 longGenerator.flush();

 JsonSerializer<Object> calendarSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 JsonGenerator calendarGenerator = mapper.getFactory().createGenerator(System.out);
 calendarSerializer.serialize(calendarKey, calendarGenerator, mapper.getSerializerProvider());
 calendarGenerator.flush();

 JsonSerializer<Object> dateSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 JsonGenerator dateGenerator = mapper.getFactory().createGenerator(System.out);
 dateSerializer.serialize(dateKey, dateGenerator, mapper.getSerializerProvider());
 dateGenerator.flush();

 JsonSerializer<Object> uuidSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), UUID.class, true);
 JsonGenerator uuidGenerator = mapper.getFactory().createGenerator(System.out);
 uuidSerializer.serialize(uuidKey, uuidGenerator, mapper.getSerializerProvider());
 uuidGenerator.flush();
 }

 @Test
 public void testSerializeClass() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 Class<?> classKey = String.class;
 JsonSerializer<Object> classSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), classKey, true);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 classSerializer.serialize(classKey.getName(), generator, mapper.getSerializerProvider());
 generator.flush();
 }

 @Test
 public void testSerializeEnum() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 Enum testEnum = Enum.valueOf(Enum.class, "TEST");
 JsonSerializer<Object> enumSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Enum.class, true);
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 enumSerializer.serialize(testEnum, generator, mapper.getSerializerProvider());
 generator.flush();
 }

 @Test
 public void testSerializeDynamic() throws Exception {
 ObjectNode root = mapper.createObjectNode();
 mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

 Object dynamicKey = new Object() {
 @Override
 public String toString() {
 return "testDynamicKey";
 }
 };

 PropertySerializerMap serializers = PropertySerializerMap.emptyForProperties();
 JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getDefault()._findAndAddDynamic(serializers, dynamicKey.getClass(), mapper.getSerializerProvider());
 SerializerProvider provider = new SerializerProvider() {
 @Override
 public JsonSerializer<Object> findValueSerializer(Class<?> type, JsonSerializer<?> existingSerializer) {
 return dynamicSerializer;
 }
 };

 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 dynamicSerializer.serialize(dynamicKey, generator, provider);
 generator.flush();
 }
}