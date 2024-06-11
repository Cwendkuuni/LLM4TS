package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("test");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode("test");
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        POJONode node = new POJONode(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    // Add more test cases for other methods...

}