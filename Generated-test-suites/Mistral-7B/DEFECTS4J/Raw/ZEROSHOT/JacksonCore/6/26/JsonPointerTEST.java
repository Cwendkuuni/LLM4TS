package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testPropertyPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testElementPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertSame(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/0", pointer.toString());
    }

    @Test
    public void testComplexPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar/baz[1]/quux");
        assertFalse(pointer.matches());
        assertEquals("quux", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/0/bar/baz[1]/quux", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~/foo");
    }

    @Test
    public void testComposePointer() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/bar");
        JsonPointer pointer3 = JsonPointer.compile("/quux");
        JsonPointer pointer4 = JsonPointer.compile("/quux/quux");

        JsonPointer pointer5 = JsonPointer.fromSegment(pointer1, pointer2, pointer3);
        JsonPointer pointer6 = JsonPointer.fromSegment(pointer1, pointer2, pointer4);

        assertEquals(pointer5.toString(), "/foo/bar/quux");
        assertEquals(pointer6.toString(), "/foo/bar/quux/quux");
    }

    @Test
    public void testMatchingPointer() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();

        jsonNode.putPOJO("foo", new Foo());
        jsonNode.put("bar", 7);
        jsonNode.put("baz", new Object[] { "quux" });

        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/baz/0");

        JsonNode node1 = mapper.treeTraversingParser().parse(jsonNode.toString());
        JsonNode node2 = JsonPointer.compile("/").match(node1, pointer1);
        JsonNode node3 = JsonPointer.compile("/").match(node2, pointer2);

        assertEquals(node3.getClass().getName(), "class [Ljava.lang.Object;");
        assertEquals(node3.toString(), "[Ljava.lang.Object;@7852e87d");
    }
}

class Foo {
}