package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private AsWrapperTypeDeserializer deserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(), "_type", true, String.class);
 }

 @Test
 public void testDeserializeTypedFromObject_ValidWrapperObject() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("_type", "java.lang.String");
 node.put("value", "test");
 Object result = mapper.readValue(node.toString(), new TypeReference<Object>() {});
 assertInstanceOf(String.class, result);
 assertEquals("test", result.toString());
 }

 @Test
 public void testDeserializeTypedFromObject_InvalidWrapperObject() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("_type", "java.lang.Integer");
 node.put("value", "test");
 try {
 mapper.readValue(node.toString(), deserializer.getType());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeTypedFromObject_MissingTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("value", "test");
 try {
 mapper.readValue(node.toString(), deserializer.getType());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeTypedFromObject_NullValue() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("_type", "java.lang.String");
 node.put("value", null);
 Object result = mapper.readValue(node.toString(), new TypeReference<Object>() {});
 assertNull(result);
 }
}