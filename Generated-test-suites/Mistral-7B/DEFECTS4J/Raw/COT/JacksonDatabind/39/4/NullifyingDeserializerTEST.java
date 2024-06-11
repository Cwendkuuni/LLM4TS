package com.example.junit.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeObject() throws IOException {
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(MAPPER.getFactory().createParser("{}"), MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeObjectWithType() throws IOException {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("[]");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, MAPPER.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeObjectWithInvalidToken() throws IOException {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("123");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 deserializer.deserializeWithType(parser, MAPPER.getDeserializationContext(), null);
 fail("Should have thrown an IOException");
 }

 @Test
 public void testDeserializeArray() throws IOException {
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(MAPPER.getFactory().createParser("[1, 2, 3]"), MAPPER.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test
 public void testDeserializeObjectWithStartObjectToken() throws IOException {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("{\"name\": \"John\", \"age\": 30}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, MAPPER.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test
 public void testDeserializeObjectWithFieldNameToken() throws IOException {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("{\"name\": null}");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, MAPPER.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test
 public void testDeserializeObjectWithArrayToken() throws IOException {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.createParser("[1, 2, 3]");
 NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, MAPPER.getDeserializationContext(), null);
 assertNull(result);
 }
}