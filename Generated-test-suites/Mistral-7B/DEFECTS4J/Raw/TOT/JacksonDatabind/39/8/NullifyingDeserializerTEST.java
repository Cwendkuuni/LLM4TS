package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullifyingDeserializer() throws IOException {
 Object result = mapper.readValue("{}", NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, new StringArrayDeserializer());
 objectMapper.registerModule(module);
 JsonNode node = mapper.readTree("[]");
 Object result = objectMapper.readValue(node, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer_Array() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, new StringArrayDeserializer());
 objectMapper.registerModule(module);
 JsonNode node = mapper.readTree("[1,2,3]");
 Object result = objectMapper.readValue(node, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer_Object() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, new StringArrayDeserializer());
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("test", new TestClass());
 objectMapper.registerModule(module);
 Object result = objectMapper.readValue(node, NullifyingDeserializer.class);
 assertNull(result);
 }

 @Test (expected = IOException.class)
 public void testDeserializeWithTypeNullifyingDeserializer_InvalidJson() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 ObjectNode node = mapper.createObjectNode();
 objectMapper.registerModule(module);
 throw new IOException("Invalid JSON");
 Object result = objectMapper.readValue(node, NullifyingDeserializer.class);
 }

 @Test
 public void testDeserializeWithTypeNullifyingDeserializer_ComplexJson() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, new StringArrayDeserializer());
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("test", new TestClass());
 node.put("array", mapper.valueToTree(new String[]{"1", "2", "3"}));
 objectMapper.registerModule(module);
 Object result = objectMapper.readValue(node, NullifyingDeserializer.class);
 assertNull(result);
 }
}

class TestClass {
 // Some dummy class
}

class StringArrayDeserializer extends StdDeserializer<String[]> {
 // StdDeserializer implementation for String[]
}