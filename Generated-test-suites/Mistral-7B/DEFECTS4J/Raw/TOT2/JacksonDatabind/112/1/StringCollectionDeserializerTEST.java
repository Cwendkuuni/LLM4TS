package com.example.junit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StringCollectionDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @After
 public void teardown() throws IOException {
 objectMapper.close();
 }

 @Test
 public void testEmptyCollectionDeserialization() throws IOException {
 JsonParser parser = JsonFactory.instance.createParser("[]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 TypeFactory.defaultInstance.constructCollectionType(List.class, String.class),
 null, null, null, null, null);
 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testSingleElementCollectionDeserialization() throws IOException {
 JsonParser parser = JsonFactory.instance.createParser("[\"" + "test" + "\"]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 TypeFactory.defaultInstance.constructCollectionType(List.class, String.class),
 null, null, null, null, null);
 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Collections.singletonList("test"), result);
 }

 @Test
 public void testNullCollectionDeserialization() throws IOException {
 JsonParser parser = JsonFactory.instance.createParser("null");
 DeserializationContext context = objectMapper.getDeserializationContext();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 TypeFactory.defaultInstance.constructCollectionType(List.class, String.class),
 null, null, null, null, null);
 List<String> result = deserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test
 public void testMultipleElementsCollectionDeserialization() throws IOException {
 JsonParser parser = JsonFactory.instance.createParser("[\"" + "test1" + "\",\"" + "test2" + "\"]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 TypeFactory.defaultInstance.constructCollectionType(List.class, String.class),
 null, null, null, null, null);
 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Arrays.asList("test1", "test2"), result);
 }

 @Test
 public void testEmptyStringElementCollectionDeserialization() throws IOException {
 JsonParser parser = JsonFactory.instance.createParser("[\"\"]");
 DeserializationContext context = objectMapper.getDeserializationContext();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 TypeFactory.defaultInstance.constructCollectionType(List.class, String.class),
 null, null, null, null, null);
 List<String> result = deserializer.deserialize(parser, context);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testInvalidJsonCollectionDeserialization() throws IOException {
 JsonParser parser = JsonFactory.instance.createParser("{}\"test\"");
 DeserializationContext context = objectMapper.getDeserializationContext();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 TypeFactory.defaultInstance.constructCollectionType(List.class, String.class),
 null, null, null, null, null);
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, context));
 }
}