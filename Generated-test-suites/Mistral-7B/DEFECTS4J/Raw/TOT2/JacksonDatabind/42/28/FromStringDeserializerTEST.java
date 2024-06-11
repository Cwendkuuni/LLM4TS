package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;

public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testFileDeserialization() throws Exception {
 File file = new File("test.txt");
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "\"test.txt\"";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 T result = deserializer.deserialize(jp, ctxt);
 assertEquals(file, result);
 }

 @Test
 public void testUrlDeserialization() throws Exception {
 URL url = new URL("http://www.example.com");
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "\"http://www.example.com\"";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 T result = deserializer.deserialize(jp, ctxt);
 assertEquals(url, result);
 }

 @Test
 public void testUriDeserialization() throws Exception {
 URI uri = new URI("http://www.example.com/path/to/resource");
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "\"http://www.example.com/path/to/resource\"";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 T result = deserializer.deserialize(jp, ctxt);
 assertEquals(uri, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeDeserialization() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "\"File\"";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 deserializer.deserialize(jp, ctxt);
 fail("Should have thrown a JsonMappingException");
 }

 @Test(expected = InvalidFormatException.class)
 public void testInvalidUrlDeserialization() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "\"invalid:url\"";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 deserializer.deserialize(jp, ctxt);
 fail("Should have thrown an InvalidFormatException");
 }

 @Test(expected = InvalidFormatException.class)
 public void testInvalidUriDeserialization() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "\"invalid:uri\"";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 deserializer.deserialize(jp, ctxt);
 fail("Should have thrown an InvalidFormatException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyStringDeserialization() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 deserializer.deserialize(jp, ctxt);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testEmbeddedObjectDeserialization() throws Exception {
 Object obj = new URL("http://www.example.com");
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext ctxt = MAPPER.getDeserializationContext();
 JsonFactory jsonFactory = MAPPER.getFactory();
 String jsonString = "{\"url\":{\"class\":\"java.net.URL\",\"value\":\"http://www.example.com\"}}";
 JsonParser jp = jsonFactory.createParser(MAPPER.writeValueAsString(jsonString).getBytes());
 jp.nextToken(); // skip [
 T result = deserializer.deserialize(jp, ctxt);
 assertEquals(obj, result);
 }

 @After
 public void afterEachTest() throws Exception {
 MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

}