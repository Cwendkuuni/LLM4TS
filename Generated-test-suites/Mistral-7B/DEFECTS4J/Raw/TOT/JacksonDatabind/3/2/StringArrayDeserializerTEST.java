package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper(new JsonFactoryBuilder()
 .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS));
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 String[] expected = new String[0];
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeArrayWithStrings() throws Exception {
 String[] expected = {"test1", "test2", "test3"};
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\",\"test3\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeArrayWithNullValues() throws Exception {
 String[] expected = {"test1", null, "test3"};
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",null,\"test3\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeArrayWithEmptyString() throws Exception {
 String[] expected = {"", "test1", "test2"};
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"\",\"test1\",\"test2\"]");
 String[] result = deserializer.deserialize(jsonParser, new DeserializationContext.RootContext());
 assertArrayEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 JsonParser jsonParser = objectMapper.getFactory().createParser("{test: 1}");
 deserializer.deserialize(jsonParser, new DeserializationContext.RootContext());
 }
}