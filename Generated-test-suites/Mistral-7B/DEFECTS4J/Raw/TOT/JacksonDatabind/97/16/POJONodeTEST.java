package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 POJONode pojoNode = new POJONode(new String("Hello World"));
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertEquals(pojoNode.getPojo(), deserializedNode.getPojo());
 }

 @Test
 public void testPOJONodeWithNullValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(null);
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertNull(deserializedNode.getPojo());
 }

 @Test
 public void testPOJONodeWithBooleanValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(true);
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertTrue(deserializedNode.asBoolean(false));
 }

 @Test
 public void testPOJONodeWithNumberValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(123);
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertEquals(123, deserializedNode.asInt(0));
 }

 @Test
 public void testPOJONodeWithLongValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(1234567890L);
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertEquals(1234567890L, deserializedNode.asLong(0));
 }

 @Test
 public void testPOJONodeWithDoubleValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(123.45);
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertEquals(123.45, deserializedNode.asDouble(0), 0.01);
 }

 @Test
 public void testPOJONodeWithCustomClassValue() throws JsonProcessingException {
 public static class CustomClass {
 String name;

 public CustomClass(String name) {
 this.name = name;
 }

 @Override
 public String toString() {
 return name;
 }
 }

 POJONode pojoNode = new POJONode(new CustomClass("Test"));
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 POJONode deserializedNode = mapper.readValue(jsonString, POJONode.class);
 assertEquals("Test", deserializedNode.getPojo().toString());
 }

 @Test(expected = IOException.class)
 public void testPOJONodeWithInvalidValue() throws IOException {
 POJONode pojoNode = new POJONode(new IOException("Test Error"));
 ObjectWriter writer = mapper.writer();
 String jsonString = writer.writeValueAsString(pojoNode);
 // This test should throw an IOException
 }
}