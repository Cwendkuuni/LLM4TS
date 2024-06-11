package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    private POJONode node;

    @Before
    public void setup() {
        node = new POJONode("test");
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() {
        try {
            assertNull(node.binaryValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAsText() {
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        assertEquals("test", node.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        assertEquals(0.0, node.asDouble(0.0), 0.0001);
    }

    @Test
    public void testSerialize() {
        // TODO: Implement test case
    }

    @Test
    public void testGetPojo() {
        assertEquals("test", node.getPojo());
    }

    @Test
    public void testEquals() {
        assertFalse(node.equals(null));

        assertTrue(node.equals(node));
        assertTrue(node.equals(new POJONode("test")));

        assertFalse(node.equals(new POJONode("other")));
        assertFalse(node.equals(new TextNode("test")));
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", node.toString());

        // TODO: Implement additional test cases
    }
}