
package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class POJONodeTest {

    private POJONode pojoNode;

    @Before
    public void setUp() {
        pojoNode = new POJONode("test");
    }

    @After
    public void tearDown() {
        pojoNode = null;
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        assertNull(pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        assertEquals("test", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        assertFalse(pojoNode.asBoolean());
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        assertFalse(pojoNode.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        assertEquals(0, pojoNode.asInt());
    }

    @Test
    public void testAsIntWithDefaultValue() {
        assertEquals(0, pojoNode.asInt(1));
    }

    @Test
    public void testAsLong() {
        assertEquals(0L, pojoNode.asLong());
    }

    @Test
    public void testAsLongWithDefaultValue() {
        assertEquals(0L, pojoNode.asLong(1L));
    }

    @Test
    public void testAsDouble() {
        assertEquals(0.0, pojoNode.asDouble(), 0.0001);
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        assertEquals(0.0, pojoNode.asDouble(1.0), 0.0001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(pojoNode);
        assertEquals("\"test\"", json);
    }

    @Test
    public void testGetPojo() {
        assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        assertTrue(pojoNode.equals(pojoNode));
        assertFalse(pojoNode.equals(null));

        POJONode otherNode = new POJONode("test");
        assertTrue(pojoNode.equals(otherNode));

        otherNode = new POJONode("different");
        assertFalse(pojoNode.equals(otherNode));
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", pojoNode.toString());
    }
}
