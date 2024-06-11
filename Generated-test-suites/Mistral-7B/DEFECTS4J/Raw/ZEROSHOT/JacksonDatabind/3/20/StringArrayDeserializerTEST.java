package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeStandardStringArray() throws Exception {
 String[] expected = new String[]{"test1", "test2", null};
 JsonParser parser = objectMapper.getFactory().createParser("[\"test1\", \"test2\", null]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeCustomStringArray() throws Exception {
 JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString() + "_custom";
 }
 };
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] expected = new String[]{"test1_custom", "test2_custom", null};
 JsonParser parser = objectMapper.getFactory().createParser("[\"test1\", \"test2\", null]");
 String[] result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeEmptyStringArray() throws Exception {
 String[] expected = new String[0];
 JsonParser parser = objectMapper.getFactory().createParser("[]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeSingleValueAsArray() throws Exception {
 if (!objectMapper.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
 objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
 }
 String[] expected = new String[]{"singleValue"};
 JsonParser parser = objectMapper.getFactory().createParser("\"singleValue\"");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNonArray() throws Exception {
 JsonParser parser = objectMapper.getFactory().createParser("\"test\"");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 deserializer.deserialize(parser, objectMapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }
}