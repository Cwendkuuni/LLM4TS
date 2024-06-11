package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

 private Object createTestPOJO() {
 return new TestPOJO("Test", 123, 45.6);
 }

 @Test
 public void testGetNodeType() {
 POJONode pojoNode = new POJONode(new TestPOJO("Test", 123, 45.6));
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode(new TestPOJO("Test", 123, 45.6));
 assertEquals("Test{name='Test', intValue=123, doubleValue=45.6}", pojoNode.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode(null);
 assertEquals("null", pojoNode.asText("Default Value"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNodeTrue = new POJONode(true);
 assertTrue(pojoNodeTrue.asBoolean(false));

 POJONode pojoNodeFalse = new POJONode(false);
 assertFalse(pojoNodeFalse.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNodeInt = new POJONode(123);
 assertEquals(123, pojoNodeInt.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeLong = new POJONode(123L);
 assertEquals(123L, pojoNodeLong.asLong(0));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeDouble = new POJONode(123.123);
 assertEquals(123.123, pojoNodeDouble.asDouble(0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNode = new POJONode(new TestPOJO("Test", 123, 45.6));
 String json = mapper.writeValueAsString(pojoNode);
 assertEquals("{\"name\":\"Test\",\"intValue\":123,\"doubleValue\":45.6}", json);
 }

 @Test(expected = IOException.class)
 public void testBinaryValueWithInvalidType() throws IOException {
 POJONode pojoNode = new POJONode("Invalid Type");
 pojoNode.binaryValue();
 }

 @Test(expected = IOException.class)
 public void testSerializeWithNullValue() throws IOException {
 POJONode pojoNode = new POJONode(null);
 String json = mapper.writeValueAsString(pojoNode);
 fail("Expected IOException when trying to serialize null value");
 }

 @Test
 public void testEquals() {
 TestPOJO pojo1 = new TestPOJO("Test1", 123, 45.6);
 TestPOJO pojo2 = new TestPOJO("Test2", 123, 45.6);
 POJONode pojoNode1 = new POJONode(pojo1);
 POJONode pojoNode2 = new POJONode(pojo2);
 assertTrue(pojoNode1.equals(pojoNode2));
 }

 @Test
 public void testHashCode() {
 TestPOJO pojo1 = new TestPOJO("Test1", 123, 45.6);
 TestPOJO pojo2 = new TestPOJO("Test2", 123, 45.6);
 POJONode pojoNode1 = new POJONode(pojo1);
 POJONode pojoNode2 = new POJONode(pojo2);
 assertEquals(pojo1.hashCode(), pojoNode1.hashCode());
 assertEquals(pojo2.hashCode(), pojoNode2.hashCode());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new TestPOJO("Test", 123, 45.6));
 assertEquals("Test{name='Test', intValue=123, doubleValue=45.6}", pojoNode.toString());
 }

 private static ObjectMapper mapper = new ObjectMapper();

 static class TestPOJO implements JsonSerializable {
 String name;
 int intValue;
 double doubleValue;

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("name");
 gen.writeString(name);
 gen.writeNumberField("intValue", intValue);
 gen.writeNumberField("doubleValue", doubleValue);
 }
 }
}