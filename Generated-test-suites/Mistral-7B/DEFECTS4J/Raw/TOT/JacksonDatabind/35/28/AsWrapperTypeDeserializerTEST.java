package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @BeforeEach
 void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 void testDeserializeTypedFromObject_ValidTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 Map.of("baseType", String.class), mapper.getTypeIdResolver(),
 "_type", true, String.class);

 ObjectNode input = mapper.createObjectNode();
 input.put("_type", "com.example.Person");
 input.put("name", "John Doe");
 JsonNode result = mapper.readTree(mapper.writeValueAsString(deserializer) + " " + input);
 assertEquals("John Doe", result.get("name"));
 }

 @Test
 void testDeserializeTypedFromObject_MissingTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 Map.of("baseType", String.class), mapper.getTypeIdResolver(),
 "_type", true, String.class);

 ObjectNode input = mapper.createObjectNode();
 input.put("name", "John Doe");
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(mapper.writeValueAsString(deserializer) + " " + input, String.class));
 assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + String.class.getName(), e.getMessage());
 }

 @Test
 void testDeserializeTypedFromObject_InvalidTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 Map.of("baseType", String.class), mapper.getTypeIdResolver(),
 "_type", true, String.class);

 ObjectNode input = mapper.createObjectNode();
 input.put("_type", "com.example.InvalidType");
 input.put("name", "John Doe");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertThrows(ClassNotFoundException.class, () -> deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(mapper.writeValueAsString(deserializer) + " " + input), ctxt));
 }

 @Test
 void testDeserializeTypedFromArray_ValidTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 Map.of("baseType", String[].class), mapper.getTypeIdResolver(),
 "_type", true, String[].class);

 ObjectNode input = mapper.createObjectNode();
 input.put("_type", "com.example.Person[]");
 input.put("[0]");
 input.put("0.name", "John Doe");
 JsonNode result = mapper.readTree(mapper.writeValueAsString(deserializer) + " " + input);
 String[] expected = new String[]{"John Doe"};
 assertEquals(expected, result.path("0"));
 }

 @Test
 void testDeserializeTypedFromArray_InvalidTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 Map.of("baseType", String[].class), mapper.getTypeIdResolver(),
 "_type", true, String[].class);

 ObjectNode input = mapper.createObjectNode();
 input.put("_type", "com.example.InvalidType[]");
 input.put("[0]");
 input.put("0.name", "John Doe");
 JsonProcessingException e = assertThrows(JsonProcessingException.class, () -> mapper.readValue(mapper.writeValueAsString(deserializer) + " " + input, String[].class));
 assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class [Ljava.lang.String;", e.getMessage());
 }
}