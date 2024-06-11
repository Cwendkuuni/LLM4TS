package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

    private POJONode pojoNode;

    @Before
    public void setup() {
        pojoNode = new POJONode("test");
    }

    @Test
    public void testGetNodeType() {
        Assert.assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        Assert.assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() {
        Assert.assertThrows(IOException.class, () -> {
            pojoNode.binaryValue();
        });
    }

    @Test
    public void testAsText() {
        Assert.assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        Assert.assertEquals("test", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        Assert.assertEquals(false, pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        Assert.assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        Assert.assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        Assert.assertEquals(0.0, pojoNode.asDouble(0.0), 0.00001);
    }

    @Test
    public void testSerialize() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testGetPojo() {
        Assert.assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(pojoNode.equals(pojoNode));
        Assert.assertFalse(pojoNode.equals(null));
        Assert.assertFalse(pojoNode.equals("test"));
        Assert.assertTrue(pojoNode.equals(new POJONode("test")));
        Assert.assertFalse(pojoNode.equals(new POJONode("test2")));
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("test", pojoNode.toString());
    }
}