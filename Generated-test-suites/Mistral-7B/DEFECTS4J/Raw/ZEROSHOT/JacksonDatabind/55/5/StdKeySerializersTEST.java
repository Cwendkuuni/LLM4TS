package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.test.BaseMapTest;

public class StdKeySerializersTest extends BaseMapTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDefaultKeySerializer() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(getSerializationConfig(), null, false);
 assertNotNull(serializer);
 Class<?> testClass = String.class;
 Object key = "test";
 String expected = "test";

 JsonGenerator jsonGenerator = getJsonGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(key, jsonGenerator, serializerProvider);
 String actual = jsonGenerator.getOutputContent().toString();
 assertEquals("Expected 'test' but got: " + actual, expected, actual);
 }

 @Test
 public void testStringKeySerializer() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStringKeySerializer();
 assertNotNull(serializer);
 Class<?> testClass = String.class;
 Object key = "test";
 String expected = "test";

 JsonGenerator jsonGenerator = getJsonGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(key, jsonGenerator, serializerProvider);
 String actual = jsonGenerator.getOutputContent().toString();
 assertEquals("Expected 'test' but got: " + actual, expected, actual);
 }

 @Test
 public void testGetStdKeySerializerWithClass() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(getSerializationConfig(), Date.class, false);
 assertNotNull(serializer);
 Date date = new Date();
 serializer.serialize(date, getJsonGenerator(), getSerializerProvider());
 String jsonString = getJsonGenerator().getOutputContent().toString();
 assertEquals("Expected a JSON string representing a date but got: " + jsonString,
 "\"2023-03-28T13:56:40.352Z\"", jsonString);
 }

 @Test
 public void testGetStdKeySerializerWithNull() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(getSerializationConfig(), null, false);
 assertNull(serializer);
 }

 @Test
 public void testGetStdKeySerializerWithUnknownClass() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(getSerializationConfig(), Integer.class, false);
 assertNotNull(serializer);
 Integer integer = 42;
 serializer.serialize(integer, getJsonGenerator(), getSerializerProvider());
 String jsonString = getJsonGenerator().getOutputContent().toString();
 assertEquals("Expected a JSON string representing an integer but got: " + jsonString,
 "42", jsonString);
 }

 @Test
 public void testGetFallbackKeySerializerWithClass() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(getSerializationConfig(), Date.class);
 assertNotNull(serializer);
 Date date = new Date();
 serializer.serialize(date, getJsonGenerator(), getSerializerProvider());
 String jsonString = getJsonGenerator().getOutputContent().toString();
 assertEquals("Expected a JSON string representing a date but got: " + jsonString,
 "\"2023-03-28T13:56:40.352Z\"", jsonString);
 }

 @Test
 public void testGetFallbackKeySerializerWithNull() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(getSerializationConfig(), null);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerWithUnknownClass() throws JsonProcessingException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(getSerializationConfig(), Integer.class);
 assertNotNull(serializer);
 Integer integer = 42;
 serializer.serialize(integer, getJsonGenerator(), getSerializerProvider());
 String jsonString = getJsonGenerator().getOutputContent().toString();
 assertEquals("Expected a JSON string representing an integer but got: " + jsonString,
 "42", jsonString);
 }

 @Test
 public void testDynamicKeySerializer() throws JsonProcessingException, IOException {
 Class<?> testClass = new Class<MyTestClass>() {}.getName();
 MyTestClass myTestClass = new MyTestClass();
 PropertySerializerMap serializerMap = new PropertySerializerMap(Arrays.asList(myTestClass.getClass().getDeclaredFields()));
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(getSerializationConfig(), testClass, false);
 assertNotNull(serializer);
 JsonGenerator jsonGenerator = getJsonGenerator();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(myTestClass, jsonGenerator, serializerProvider);
 String jsonString = jsonGenerator.getOutputContent().toString();
 assertEquals("Expected a JSON string representing 'MyTestClass' but got: " + jsonString,
 testClass, jsonString.substring(1, jsonString.length() - 1));
 }

 static class MyTestClass {
 private String field;
 }
}