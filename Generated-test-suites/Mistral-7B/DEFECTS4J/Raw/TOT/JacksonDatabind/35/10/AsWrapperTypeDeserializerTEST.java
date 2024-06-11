package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 ObjectType.class, objectMapper.getTypeIdResolver(), "type", true, String.class);
 }

 /**
 * Test deserializing a valid JSON string using the AsWrapperTypeDeserializer
 */
 @Test
 public void testValidDeserialization() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("type", "java.lang.String");
 jsonNode.put("value", "Hello World");

 String expected = "Hello World";
 Object actual = objectMapper.readValue(jsonNode, asWrapperTypeDeserializer.getType());

 assertEquals(expected, actual);
 }

 /**
 * Test deserializing a JSON string with a missing type property
 */
 @Test(expected = IOException.class)
 public void testMissingTypeProperty() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", "Hello World");

 objectMapper.readValue(jsonNode, asWrapperTypeDeserializer.getType());
 }

 /**
 * Test deserializing a JSON string with an invalid type property
 */
 @Test(expected = IOException.class)
 public void testInvalidTypeProperty() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("type", "invalidType");
 jsonNode.put("value", "Hello World");

 objectMapper.readValue(jsonNode, asWrapperTypeDeserializer.getType());
 }

 /**
 * Test deserializing a JSON string with an unknown type
 */
 @Test(expected = IOException.class)
 public void testUnknownType() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("type", "com.unknown.UnknownType");
 jsonNode.put("value", "Hello World");

 objectMapper.readValue(jsonNode, asWrapperTypeDeserializer.getType());
 }

 /**
 * Test deserializing a JSON string with a missing value property
 */
 @Test(expected = IOException.class)
 public void testMissingValueProperty() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("type", "java.lang.String");

 objectMapper.readValue(jsonNode, asWrapperTypeDeserializer.getType());
 }
}