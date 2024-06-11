package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class StdKeySerializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testGetStringSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 String key = "testKey";
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
 serializer.serialize(key, generator, new SerializerProvider());
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertEquals(key, resultNode.get("name").asText());
 }

 @Test
 public void testGetDateSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 Date date = new Date();
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Date.class, false);
 serializer.serialize(date, generator, new SerializerProvider());
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertEquals(date.toString(), resultNode.get("name").asText());
 }

 @Test
 public void testGetCalendarSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 Calendar calendar = Calendar.getInstance();
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Calendar.class, false);
 serializer.serialize(calendar, generator, new SerializerProvider());
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertEquals(calendar.getTime().toString(), resultNode.get("name").asText());
 }

 @Test
 public void testGetClassSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 Class<?> clazz = String.class;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 clazz, false);
 serializer.serialize(clazz, generator, new SerializerProvider());
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertEquals(clazz.getName(), resultNode.get("name").asText());
 }

 @Test
 public void testGetEnumSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 EnumSet<DaysOfWeek> enumSet = EnumSet.allOf(DaysOfWeek.class);
 ObjectGeneratorConfig config = new ObjectGeneratorConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 ObjectMapper objectMapper = new ObjectMapper(config);
 StdKeySerializers.Default serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 EnumSet.class, false);
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.putPOJO("days", enumSet);
 ObjectNode node = rootNode.get("days");
 ObjectGenerator generator = objectMapper.getFactory().createGenerator(node);
 serializer.serialize(enumSet, generator, new SerializerProvider());
 ObjectNode keyNode = generator.getOutputBuffer().getRootValue();
 assertEquals("[SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY]", keyNode.asText());
 }

 @Test
 public void testGetDynamicSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectGeneratorConfig config = new ObjectGeneratorConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 ObjectMapper objectMapper = new ObjectMapper(config);
 Object testObj = new Object() {
 @Override
 public String toString() {
 return "testObject";
 }};
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(testObj, generator, new SerializerProvider());
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertEquals("testObject", resultNode.get("name").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testNullKey() throws JsonGenerationException, JsonMappingException, IOException {
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(null, generator, new SerializerProvider());
 }

 @Test
 public void testGetDefaultSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 StdKeySerializers serializer = new StdKeySerializers();
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertTrue(resultNode.isMissingNode());
 }

 @Test
 public void testUseDefaultSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 Class<?> clazz = null;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 StdKeySerializers serializer = new StdKeySerializers();
 StdKeySerializers.JsonSerializer<Object> defaultSerializer = serializer.getDefault();
 defaultSerializer.serialize(clazz, generator, new SerializerProvider());
 ObjectNode resultNode = objectMapper.readTree(generator.getOutputBuffer());
 assertTrue(resultNode.isMissingNode());
 }
}