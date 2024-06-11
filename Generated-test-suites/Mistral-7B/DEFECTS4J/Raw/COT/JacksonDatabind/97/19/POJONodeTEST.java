package com.example.pojontest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

public class POJONodeTest {

 private Object createTestPojo() {
 return new TestPojo("Test", 1, 3.14);
 }

 @Test
 public void testConstructor() {
 POJONode node = new POJONode(createTestPojo());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testGetNodeType() {
 POJONode node = new POJONode(createTestPojo());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsText() {
 POJONode node = new POJONode(createTestPojo());
 assertEquals("Test", node.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode node = new POJONode(createTestPojo());
 assertEquals("Test", node.asText());
 assertEquals("defaultValue", node.asText("defaultValue"));
 }

 @Test
 public void testAsBoolean() {
 POJONode node = new POJONode(true);
 assertTrue(node.asBoolean(false));
 POJONode nodeFalse = new POJONode(false);
 assertFalse(nodeFalse.asBoolean(false));
 }

 @Test
 public void testAsInt() {
 POJONode nodeOne = new POJONode(1);
 assertEquals(1, nodeOne.asInt(0));
 POJONode nodeNegativeOne = new POJONode(-1);
 assertEquals(-1, nodeNegativeOne.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode nodeLongOne = new POJONode(1L);
 assertEquals(1L, nodeLongOne.asLong(0L));
 POJONode nodeLongNegativeOne = new POJONode(-1L);
 assertEquals(-1L, nodeLongNegativeOne.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode nodeDoubleOne = new POJONode(1.0);
 assertEquals(1.0, nodeDoubleOne.asDouble(0.0), 0.001);
 POJONode nodeDoubleNegativeOne = new POJONode(-1.0);
 assertEquals(-1.0, nodeDoubleNegativeOne.asDouble(0.0), 0.001);
 }

 @Test(expected = IOException.class)
 public void testBinaryValueNormal() throws IOException {
 POJONode node = new POJONode("Test");
 node.binaryValue();
 fail("Expected IOException");
 }

 @Test
 public void testBinaryValuePojo() throws IOException {
 POJONode node = new POJONode(createTestPojo());
 byte[] binaryData = node.binaryValue();
 assertNotNull(binaryData);
 }

 @Test(expected = IOException.class)
 public void testSerializeNull() throws IOException {
 POJONode node = new POJONode(null);
 node.serialize(null, null);
 fail("Expected IOException");
 }

 @Test
 public void testSerializeJsonSerializable() throws IOException {
 TestSerializable serializableObject = new TestSerializable();
 POJONode node = new POJONode(serializableObject);
 byte[] serializedData = new byte[1024];
 node.serialize(new TestJsonGenerator(), new TestSerializerProvider(serializedData, null));
 assertNotNull(serializedData);
 }

 @Test
 public void testGetPojo() {
 POJONode node = new POJONode(createTestPojo());
 assertEquals(createTestPojo(), node.getPojo());
 }

 @Test
 public void testEquals() {
 TestPojo pojo1 = new TestPojo("Test1", 1, 3.14);
 TestPojo pojo2 = new TestPojo("Test2", 1, 3.14);
 POJONode node1 = new POJONode(pojo1);
 POJONode node2 = new POJONode(pojo2);
 assertTrue(node1.equals(node1));
 assertTrue(node1.equals(node2));
 assertFalse(node1.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 TestPojo pojo1 = new TestPojo("Test1", 1, 3.14);
 TestPojo pojo2 = new TestPojo("Test2", 1, 3.14);
 POJONode node1 = new POJONode(pojo1);
 POJONode node2 = new POJONode(pojo2);
 int hashCode1 = node1.hashCode();
 int hashCode2 = node2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 TestPojo pojo = new TestPojo("Test", 1, 3.14);
 POJONode node = new POJONode(pojo);
 assertEquals("Test{name='Test', intValue=1, doubleValue=3.14}", node.toString());
 }

 @Test
 public void testToStringBinaryValue() {
 POJONode node = new POJONode(new byte[10]);
 assertEquals("(binary value of 10 bytes)", node.toString());
 }

 @Test
 public void testToStringRawValue() {
 POJONode node = new POJONode(new RawValue("Test"));
 assertEquals("(raw value 'Test')", node.toString());
 }
}

class TestJsonGenerator extends JsonGenerator {
 // Empty implementation for testing
}

class TestSerializerProvider extends SerializerProvider {
 // Empty implementation for testing
}

class TestPojo {
 private String name;
 private int intValue;
 private double doubleValue;

 public TestPojo(String name, int intValue, double doubleValue) {
 this.name = name;
 this.intValue = intValue;
 this.doubleValue = doubleValue;
 }

 @Override
 public boolean equals(Object o) {
 return super.equals(o);
 }

 @Override
 public int hashCode() {
 return super.hashCode();
 }

 @Override
 public String toString() {
 return "TestPojo{name='" + name + "', intValue=" + intValue + ", doubleValue=" + doubleValue + '}';
 }
}

class TestSerializable implements JsonSerializable {
 // Empty implementation for testing
 @Override
 public void serialize(JsonGenerator generat, SerializerProvider serializers) throws IOException {
 // Empty implementation for testing
 }
}