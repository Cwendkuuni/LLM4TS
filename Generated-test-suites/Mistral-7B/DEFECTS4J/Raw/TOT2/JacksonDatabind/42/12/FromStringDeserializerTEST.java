package com.example;

import org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class FromStringDeserializerTest {

@Test
public void testDeserialize() throws JsonProcessingException {
ObjectMapper mapper = new ObjectMapper();
FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);

String jsonString = "\"Hello World\"";
String result = mapper.readValue(jsonString, String.class, deserializer);
assertEquals("Hello World", result);
}

@Test
public void testDeserializeEmbedded() throws JsonProcessingException {
ObjectMapper mapper = new ObjectMapper();

FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
String jsonString = "{\"path\":\"test.txt\"}";
File result = mapper.readValue(jsonString, File.class, deserializer);

assertTrue(result.exists()); // This assumes there is a file named "test.txt" in the project's directory
assertEquals("test.txt", result.getName());
}

@Test
public void testDeserializeFromEmptyString() throws JsonProcessingException {
ObjectMapper mapper = new ObjectMapper();
FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);

String jsonString = "";
URL result = mapper.readValue(jsonString, URL.class, deserializer);
assertNull(result);
}
}