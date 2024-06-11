
package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
  
    @Test
    public void testMatches() {
         // Test empty JsonPointer
        assertTrue(JsonPointer.EMPTY.matches());

        // Test non-empty JsonPointer
        assertFalse(new JsonPointer("/test").matches());
    }
  
    @Test
    public void testGetMatchingProperty() {
        // Test empty JsonPointer
        assertEquals("", JsonPointer.EMPTY.getMatchingProperty());

        // Test JsonPointer with matching property name
        assertEquals("test", new JsonPointer("/test").getMatchingProperty());

        // Test JsonPointer with non-matching property name
        assertEquals("other", new JsonPointer("/test").getMatchingProperty());
    }
  
    @Test
    public void testGetMatchingIndex() {
        // Test empty JsonPointer
        assertEquals(-1, JsonPointer.EMPTY.getMatchingIndex());

        // Test JsonPointer with matching index
        assertEquals(0, new JsonPointer("/0").getMatchingIndex());

        // Test JsonPointer with non-matching index
        assertEquals(1, new JsonPointer("/1").getMatchingIndex());
    }
  
    @Test
    public void testMayMatchProperty() {
        // Test empty JsonPointer
        assertTrue(JsonPointer.EMPTY.mayMatchProperty());

        // Test JsonPointer with matching property name
        assertTrue(new JsonPointer("/test").mayMatchProperty());

        // Test JsonPointer with non-matching property name
        assertFalse(new JsonPointer("/test").mayMatchProperty());
    }
  
    @Test
    public void testMayMatchElement() {
        // Test empty JsonPointer
        assertFalse(JsonPointer.EMPTY.mayMatchElement());

        // Test JsonPointer with matching index
        assertTrue(new JsonPointer("/0").mayMatchElement());

        // Test JsonPointer with non-matching index
        assertFalse(new JsonPointer("/1").mayMatchElement());
    }
  
    @Test
    public void testMatchProperty() {
        // Test empty JsonPointer
        assertNull(JsonPointer.EMPTY.matchProperty("test"));

        // Test JsonPointer with matching property name
        assertNotNull(new JsonPointer("/test").matchProperty("test"));

        // Test JsonPointer with non-matching property name
        assertNull(new JsonPointer("/test").matchProperty("other"));
    }
  
    @Test
    public void testMatchElement() {
        // Test empty JsonPointer
        assertNull(JsonPointer.EMPTY.matchElement(0));

        // Test JsonPointer with matching index
        assertNotNull(new JsonPointer("/0").matchElement(0));

        // Test JsonPointer with non-matching index
        assertNull(new JsonPointer("/1").matchElement(0));
    }
  
    @Test
    public void testTail() {
        // Test empty JsonPointer
        assertNull(JsonPointer.EMPTY.tail());

        // Test JsonPointer with multiple segments
        JsonPointer pointer = new JsonPointer("/test1/test2/test3");
        assertNotNull(pointer.tail());
        assertEquals("/test2/test3", pointer.tail().toString());
    }
  
    @Test
    public void testToString() {
        // Test empty JsonPointer
        assertEquals("", JsonPointer.EMPTY.toString());

        // Test JsonPointer with single segment
        assertEquals("/test", new JsonPointer("/test").toString());

        // Test JsonPointer with multiple segments
        assertEquals("/test1/test2/test3", new JsonPointer("/test1/test2/test3").toString());
    }
  
    @Test
    public void testEquals() {
        // Test empty JsonPointers
        assertTrue(JsonPointer.EMPTY.equals(JsonPointer.EMPTY));

        // Test JsonPointer with single segment, same segment names
        assertTrue(new JsonPointer("/test1").equals(new JsonPointer("/test1")));

        // Test JsonPointer with single segment, different segment names
        assertFalse(new JsonPointer("/test1").equals(new JsonPointer("/test2")));

        // Test JsonPointer with multiple segments, same segment names
        assertTrue(new JsonPointer("/test1/test2/test3").equals(new JsonPointer("/test1/test2/test3")));

        // Test JsonPointer with multiple segments, different segment names
        assertFalse(new JsonPointer("/test1/test2/test3").equals(new JsonPointer("/test1/test2/test4")));
    }
}
