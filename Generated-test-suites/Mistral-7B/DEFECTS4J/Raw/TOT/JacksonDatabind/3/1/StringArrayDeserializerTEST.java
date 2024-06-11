package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testEmptyArrayDeserialization() throws JsonProcessingException {
 String[] expected = new String[0];
 String[] actual = mapper.readValue("[ ]", String[].class);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testSingleStringDeserialization() throws JsonProcessingException {
 String[] expected = {"test"};
 String[] actual = mapper.readValue("[\"" + "test" + "\"]", String[].class);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testNullElementDeserialization() throws JsonProcessingException {
 String[] expected = {null};
 JsonNode node = mapper.readTree("[\tnull\t]");
 String[] actual = mapper.readValue(node.toString(), String[].class);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testNonEmptyArrayDeserialization() throws JsonProcessingException {
 String[] expected = {"test1", "test2", "test3"};
 String[] actual = mapper.readValue("[\"test1\", \"test2\", \"test3\"]", String[].class);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJsonDeserialization() throws JsonProcessingException {
 String[] expected = null;
 String json = "{\"invalid_json\":[]}";
 mapper.readValue(json, String[].class);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testNullJsonDeserialization() throws JsonProcessingException {
 JsonNode node = null;
 String[] expected = null;
 String[] actual = mapper.readValue(node.toString(), String[].class);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCustomDeserializer() throws JsonProcessingException {
 String[] expected = {"custom1", "custom2", "custom3"};
 JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {};
 StringArrayDeserializer serializerWithCustomDeserializer = new StringArrayDeserializer(customDeserializer);
 String[] actual = mapper.readValue("[\"custom1\", \"custom2\", \"custom3\"]", String[].class, serializerWithCustomDeserializer);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testContextualDeserializer() throws JsonProcessingException {
 ObjectMapper contextualMapper = new ObjectMapper();
 contextualMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String[] expected = {"contextual1", "contextual2"};
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonDeserializer<String> contextualDeserializer = new StdDeserializer<String>(String.class) {};
 deserializer = deserializer.withResolved(contextualDeserializer);
 ObjectNode node = contextualMapper.createObjectNode();
 node.put("val", "[\"contextual1\", \"contextual2\"]");
 String[] actual = contextualMapper.reader(String[].class).readValue(node);
 assertArrayEquals(expected, actual);
 }
}