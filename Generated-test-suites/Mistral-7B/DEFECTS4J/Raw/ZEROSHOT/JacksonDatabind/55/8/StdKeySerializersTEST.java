package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws JsonMappingException, JsonGenerationException {
 SerializationConfig config = new SerializationConfig();

 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Integer.TYPE;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = java.util.Date.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = java.util.Calendar.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = java.util.UUID.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = Enum.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = Class.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = boolean.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = Object[].class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = null;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonMappingException, JsonGenerationException {
 SerializationConfig config = new SerializationConfig();

 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Integer.TYPE;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = java.util.Date.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);

 rawKeyType = java.util.Calendar.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);

 rawKeyType = java.util.UUID.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);

 rawKeyType = Enum.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);

 rawKeyType = Class.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNotNull(serializer);

 rawKeyType = boolean.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(serializer);

 rawKeyType = Object[].class;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(serializer);

 rawKeyType = null;
 serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertNull(serializer);
 }

 @Test
 public void testGetStdKeySerializerWithUseDefault() throws JsonMappingException, JsonGenerationException {
 SerializationConfig config = new SerializationConfig();

 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Integer.TYPE;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = java.util.Date.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = java.util.Calendar.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = java.util.UUID.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = Enum.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = Class.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = boolean.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = Object[].class;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNotNull(serializer);

 rawKeyType = null;
 serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertNull(serializer);
 }

 @Test
 public void testDynamicKeySerializer() throws JsonMappingException, JsonGenerationException {
 SerializationConfig config = new SerializationConfig();
 ObjectNode rootNode = ObjectMapper.getDefaultInstance().createObjectNode();
 JsonGenerator gen = rootNode.setUpRootValueGenerator(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
 PropertySerializerMap propertyMap = new PropertySerializerMap();

 // Test with types we've seen StdKeySerializers implement serializer for
 Class<?> testTypes[] = {String.class, Integer.TYPE, java.util.Date.class, java.util.Calendar.class, java.util.UUID.class, Enum.class, Class.class};
 for (Class<?> testType : testTypes) {
 Class<?> rawKeyType = testType;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 Object value = null;
 if (testType == Enum.class) {
 value = Enum.VALUES[0];
 } else if (testType == String.class) {
 value = "test";
 } else {
 value = testType;
 }

 PropertySerializerMap mapCopy = propertyMap.copy();
 JsonSerializer<Object> serializerFound = null;
 try {
 StdKeySerializers.Dynamic dynamic = (StdKeySerializers.Dynamic) serializer;
 serializerFound = dynamic.findAndAddDynamic(mapCopy, testType, config);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }

 gen.writeStartObject();
 try {
 serializer.serialize(value, gen, config);
 fail("Expected StdKeySerializers.Dynamic to return a new serializer");
 } catch (IOException e) {
 // Expected
 }

 gen.writeFieldName("serializedValue");
 mapCopy.serializerFor(testType).serialize(value, gen, config);
 gen.writeEndObject();

 assertSame(serializerFound, mapCopy.serializerFor(testType));
 }

 // Test with types not implemented by StdKeySerializers
 Class<?> notImplementedTypes[] = {boolean.class, Object[].class};
 for (Class<?> testType : notImplementedTypes) {
 Class<?> rawKeyType = testType;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 Object value = null;
 if (testType == Enum.class) {
 value = Enum.VALUES[0];
 } else if (testType == String.class) {
 value = "test";
 } else {
 value = testType;
 }

 PropertySerializerMap mapCopy = propertyMap.copy();
 JsonSerializer<Object> serializerFound = null;
 try {
 StdKeySerializers.Dynamic dynamic = (StdKeySerializers.Dynamic) serializer;
 serializerFound = dynamic.findAndAddDynamic(mapCopy, testType, config);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }

 gen.writeStartObject();
 try {
 serializer.serialize(value, gen, config);
 fail("Expected StdKeySerializers.Dynamic to return a new serializer");
 } catch (IOException e) {
 // Expected
 }

 gen.writeFieldName("serializedValue");
 ObjectNode valueNode = ObjectMapper.getDefaultInstance().createObjectNode();
 valueNode.put("value", value.toString());
 valueNode.writeValue(gen);
 gen.writeEndObject();

 assertNull("Unexpected serializer found for type " + testType.getName(), serializerFound);
 }

 gen.close();
 }
}