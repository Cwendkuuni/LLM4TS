package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class StdKeySerializersTest {

 private static final ObjectNode TEST_JSON;
 static {
 try {
 TEST_JSON = ObjectMapperBuilder.json().build().createObjectNode();
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 // Test with null input
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, true);
 assertNull(serializer);

 // Test with String input
 serializer = StdKeySerializers.getStdKeySerializer(null, String.class, true);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 // Test with int input
 serializer = StdKeySerializers.getStdKeySerializer(null, int.class, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 // Test with Date input
 Date date = new Date();
 serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

 // Test with Calendar input
 Calendar calendar = Calendar.getInstance();
 serializer = StdKeySerializers.getStdKeySerializer(null, Calendar.class, true);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 // Test with null input
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

 // Test with Enum input
 Class<MyEnum> enumClass = MyEnum.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(null, enumClass);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);
 }

 @Test
 public void testGetDefault() {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 @Test
 public void testDynamicKeySerializer() throws JsonProcessingException {
 PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
 JsonSerializer<Object> serializer = map.getSerializer(Object.class);
 assertNull(serializer);

 Object value = new Object();
 serializer = StdKeySerializers.getStdKeySerializer(null, value.getClass(), true);
 map = PropertySerializerMap.emptyForProperties();
 map.addSerializerBinding(value.getClass(), serializer);
 serializer = map.getSerializer(value.getClass());
 assertEquals(serializer.getClass(), StdKeySerializers.Dynamic.class);

 TEST_JSON.put("test", value);
 ObjectMapper mapper = ObjectMapperBuilder.json().build();
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(TEST_JSON);
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
 parser.nextValue();
 assertEquals(value.getClass(), parser.getValueAsToken().getValueClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDynamicKeySerializer_UnknownType() throws JsonProcessingException {
 PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
 map.findAndAddKeySerializer(new Object(), null);
 }

 @Test
 public void testStringKeySerializer() throws JsonProcessingException {
 String key = "testKey";
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
 TEST_JSON.put(key, new Object());
 ObjectMapper mapper = ObjectMapperBuilder.json().build();
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(TEST_JSON);
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
 assertEquals(key, parser.nextFieldName());
 }
}

 enum MyEnum {
 TEST
}