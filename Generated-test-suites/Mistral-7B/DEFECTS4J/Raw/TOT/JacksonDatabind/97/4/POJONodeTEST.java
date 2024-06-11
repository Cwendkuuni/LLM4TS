package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class POJONodeTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.setDefaultTyping(DefaultTyping.OBJECT_ARRAY);
 }

 @After
 public void cleanup() throws Exception {
 mapper.close();
 }

 /**
 * Test for default constructor
 */
 @Test
 public void testDefaultConstructor() {
 POJONode node = new POJONode(null);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertNull(node.asText());
 assertNull(node.getPojo());
 }

 /**
 * Test for constructor with POJO object
 */
 @Test
 public void testConstructorWithPojo() throws JsonProcessingException {
 Object pojo = new Object() {};
 POJONode node = new POJONode(pojo);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals(pojo.toString(), node.asText());
 assertEquals(pojo, node.getPojo());
 }

 /**
 * Test for serialization of POJO
 */
 @Test
 public void testSerialization() throws JsonProcessingException {
 String json = "{\"name\":\"John\",\"age\":30}";
 POJONode node = mapper.readValue(json, POJONode.class);
 Object pojo = node.getPojo();
 assertEquals("John", pojo.getClass().getDeclaredField("name").get(pojo));
 assertEquals(30, pojo.getClass().getDeclaredField("age").get(pojo));
 }

 /**
 * Test for serialization of null POJO
 */
 @Test
 public void testSerializationWithNullPojo() throws JsonProcessingException {
 POJONode node = new POJONode(null);
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 String expectedJson = "null";
 assertEquals(expectedJson, json);
 }

 /**
 * Test for binary data
 */
 @Test
 public void testBinaryData() throws IOException {
 byte[] binaryData = new byte[] { 1, 2, 3 };
 POJONode node = new POJONode(binaryData);
 byte[] result = node.binaryValue();
 assertArrayEquals(binaryData, result);
 }

 /**
 * Test for type coercions
 */
 @Test
 public void testTypeCoercions() {
 POJONode node = new POJONode("hello");
 assertTrue(node.asBoolean(false));
 assertEquals("hello", node.asText());
 assertEquals(4, node.asInt(0));
 assertEquals(4.0, node.asDouble(0.0), 0.001);
 }

 /**
 * Test for equals method
 */
 @Test
 public void testEqualsMethod() {
 Object pojo1 = new Object() {};
 Object pojo2 = new Object() {};
 POJONode node1 = new POJONode(pojo1);
 POJONode node2 = new POJONode(pojo2);
 assertFalse(node1.equals(node2));
 POJONode node3 = new POJONode(pojo1);
 assertTrue(node1.equals(node3));
 }
}