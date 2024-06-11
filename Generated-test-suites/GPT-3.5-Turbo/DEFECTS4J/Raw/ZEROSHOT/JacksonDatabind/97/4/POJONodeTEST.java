package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] byteArray = new byte[]{1, 2, 3};
      
        POJONode node = new POJONode(byteArray);
        Assert.assertArrayEquals(byteArray, node.binaryValue());

        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        Assert.assertArrayEquals(stringValue.getBytes(), stringNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        Assert.assertEquals("null", node.asText());

        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        Assert.assertEquals(stringValue, stringNode.asText());
    }

    @Test
    public void testAsTextWithDefault() {
        POJONode node = new POJONode(null);
        Assert.assertEquals("default", node.asText("default"));

        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        Assert.assertEquals(stringValue, stringNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(false, node.asBoolean(false));

        boolean boolValue = true;
        POJONode boolNode = new POJONode(boolValue);
        Assert.assertEquals(boolValue, boolNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(1, node.asInt(1));

        int intValue = 10;
        POJONode intNode = new POJONode(intValue);
        Assert.assertEquals(intValue, intNode.asInt(1));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(1L, node.asLong(1L));

        long longValue = 100L;
        POJONode longNode = new POJONode(longValue);
        Assert.assertEquals(longValue, longNode.asLong(1L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(1.0, node.asDouble(1.0), 0);

        double doubleValue = 99.99;
        POJONode doubleNode = new POJONode(doubleValue);
        Assert.assertEquals(doubleValue, doubleNode.asDouble(1.0), 0);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        POJONode node = new POJONode(null);
        Assert.assertEquals("null", objectMapper.writeValueAsString(node));

        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        Assert.assertEquals("\"test\"", objectMapper.writeValueAsString(stringNode));
    }

    @Test
    public void testGetPojo() {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        Assert.assertEquals(pojo, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node = new POJONode(null);
        Assert.assertFalse(node.equals(null));
        Assert.assertTrue(node.equals(node));

        POJONode node1 = new POJONode(null);
        POJONode node2 = new POJONode(null);
        Assert.assertTrue(node1.equals(node2));

        POJONode stringNode1 = new POJONode("test");
        POJONode stringNode2 = new POJONode("test");
        Assert.assertTrue(stringNode1.equals(stringNode2));
        Assert.assertFalse(stringNode1.equals(node1));

    }

    @Test
    public void testHashCode() {
        POJONode node = new POJONode(null);
        Assert.assertEquals(0, node.hashCode());

        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        Assert.assertEquals(stringValue.hashCode(), stringNode.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(null);
        Assert.assertEquals("null", node.toString());

        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode byteArrayNode = new POJONode(byteArray);
        Assert.assertEquals("(binary value of 3 bytes)", byteArrayNode.toString());

        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        Assert.assertEquals(stringValue, stringNode.toString());
    }

}