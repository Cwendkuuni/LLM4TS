
package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(true, jsonPointer.matches());
        jsonPointer = new JsonPointer("/foo");
        assertEquals(false, jsonPointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.getMatchingProperty());
        jsonPointer = new JsonPointer("/foo");
        assertEquals("foo", jsonPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(-1, jsonPointer.getMatchingIndex());
        jsonPointer = new JsonPointer("/0");
        assertEquals(0, jsonPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(false, jsonPointer.mayMatchProperty());
        jsonPointer = new JsonPointer("/foo");
        assertEquals(true, jsonPointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(false, jsonPointer.mayMatchElement());
        jsonPointer = new JsonPointer("/0");
        assertEquals(true, jsonPointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("/foo");
        assertNull(jsonPointer.matchProperty("bar"));
        assertNull(jsonPointer.matchProperty("baz"));
        assertEquals(JsonPointer.EMPTY, jsonPointer.matchProperty("foo"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("/0");
        assertNull(jsonPointer.matchElement(1));
        assertEquals(JsonPointer.EMPTY, jsonPointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        jsonPointer = new JsonPointer("/foo");
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.toString());
        jsonPointer = new JsonPointer("/foo");
        assertEquals("/foo", jsonPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = JsonPointer.EMPTY;
        JsonPointer jsonPointer2 = JsonPointer.EMPTY;
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        jsonPointer1 = new JsonPointer("/foo");
        jsonPointer2 = new JsonPointer("/foo");
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.EMPTY;
        JsonPointer jsonPointer2 = JsonPointer.EMPTY;
        assertEquals(true, jsonPointer1.equals(jsonPointer2));
        assertEquals(true, jsonPointer1.equals(jsonPointer1));
        assertEquals(false, jsonPointer1.equals(null));
        assertEquals(false, jsonPointer1.equals("test"));
        jsonPointer1 = new JsonPointer("/foo");
        jsonPointer2 = new JsonPointer("/foo");
        assertEquals(true, jsonPointer1.equals(jsonPointer2));
        assertEquals(true, jsonPointer1.equals(jsonPointer1));
        jsonPointer2 = new JsonPointer("/bar");
        assertEquals(false, jsonPointer1.equals(jsonPointer2));
        jsonPointer2 = new JsonPointer("/foo/bar");
        assertEquals(false, jsonPointer1.equals(jsonPointer2));
    }
    
    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, jsonPointer);
        jsonPointer = JsonPointer.compile("/foo");
        assertEquals("/foo", jsonPointer.toString());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("");
        assertEquals(JsonPointer.EMPTY, jsonPointer);
        jsonPointer = JsonPointer.valueOf("/foo");
        assertEquals("/foo", jsonPointer.toString());
    }
}
