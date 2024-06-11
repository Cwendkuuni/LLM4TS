package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 @Test
 public void testDefaultStringArrayDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[\"hello\",\"world\",\"jackson\"]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = mapper.readValue(json, deserializer.getType());
 assertArrayEquals(new String[]{"hello", "world", "jackson"}, result);
 }

 @Test
 public void testNullArrayDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = mapper.readValue(json, deserializer.getType());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testEmptyStringDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[\"\",\"\",\"\"]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = mapper.readValue(json, deserializer.getType());
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJsonDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "\"invalid\"";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 mapper.readValue(json, deserializer.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testNonArrayJsonDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\":\"John\"}";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 mapper.readValue(json, deserializer.getType());
 }

 @Test
 public void testCustomElementDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
 String json = "[\"hello\",\"world\",\"jackson\"]";
 String[] result = mapper.readValue(json, StringArrayDeserializer.instance.withResolved(_elementDeserializer -> customDeserializer));
 assertArrayEquals(new String[]{"HELLO", "WORLD", "JACKSON"}, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testCustomElementDeserializerFailure() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
 String json = "[\"hello\",\"world\",\"jackson\"]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance.withResolved(_elementDeserializer -> customDeserializer);
 deserializer.setFailOnEmpty(true);
 mapper.readValue(json, deserializer.getType());
 }

 @Test
 public void testTypeDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 TypeReference<String[]> type = new TypeReference<String[]>() {};
 String json = "[\"hello\",\"world\",\"jackson\"]";
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = mapper.readValue(json, deserializer.getType());
 assertEquals(type.getType(), result.getClass().getComponentType());
 assertArrayEquals(new String[]{"hello", "world", "jackson"}, result);
 }

}

class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 String value = jp.getText();
 return value.toUpperCase();
 }
}