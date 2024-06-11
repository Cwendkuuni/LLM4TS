
package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {
    @Test
    public void testCompile() {
        JsonPointer empty = JsonPointer.compile("");
        assertNull(empty.tail());

        JsonPointer pointer1 = JsonPointer.compile("/a");
        assertEquals("a", pointer1.getMatchingProperty());
        assertNull(pointer1.getMatchingIndex());
        assertNull(pointer1.tail());

        JsonPointer pointer2 = JsonPointer.compile("/a/0");
        assertEquals("a", pointer2.getMatchingProperty());
        assertEquals(Integer.valueOf(0), pointer2.getMatchingIndex());

        JsonPointer pointer3 = JsonPointer.compile("/a/0/b");
        assertEquals("a", pointer3.getMatchingProperty());
        assertEquals(Integer.valueOf(0), pointer3.getMatchingIndex());
        assertEquals("b", pointer3.tail().getMatchingProperty());
        assertNull(pointer3.tail().getMatchingIndex());
        assertNull(pointer3.tail().tail());

        JsonPointer pointer4 = JsonPointer.compile("/a/0/b/c");
        assertEquals("a", pointer4.getMatchingProperty());
        assertEquals(Integer.valueOf(0), pointer4.getMatchingIndex());
        assertEquals("b", pointer4.tail().getMatchingProperty());
        assertNull(pointer4.tail().getMatchingIndex());
        assertEquals("c", pointer4.tail().tail().getMatchingProperty());
        assertNull(pointer4.tail().tail().getMatchingIndex());
        assertNull(pointer4.tail().tail().tail());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer1 = JsonPointer.valueOf("/a");
        assertEquals("a", pointer1.getMatchingProperty());
        assertNull(pointer1.getMatchingIndex());
        assertNull(pointer1.tail());

        JsonPointer pointer2 = JsonPointer.valueOf("/a/0");
        assertEquals("a", pointer2.getMatchingProperty());
        assertEquals(Integer.valueOf(0), pointer2.getMatchingIndex());

        JsonPointer pointer3 = JsonPointer.valueOf("/a/0/b");
        assertEquals("a", pointer3.getMatchingProperty());
        assertEquals(Integer.valueOf(0), pointer3.getMatchingIndex());
        assertEquals("b", pointer3.tail().getMatchingProperty());
        assertNull(pointer3.tail().getMatchingIndex());
        assertNull(pointer3.tail().tail());

        JsonPointer pointer4 = JsonPointer.valueOf("/a/0/b/c");
        assertEquals("a", pointer4.getMatchingProperty());
        assertEquals(Integer.valueOf(0), pointer4.getMatchingIndex());
        assertEquals("b", pointer4.tail().getMatchingProperty());
        assertNull(pointer4.tail().getMatchingIndex());
        assertEquals("c", pointer4.tail().tail().getMatchingProperty());
        assertNull(pointer4.tail().tail().getMatchingIndex());
        assertNull(pointer4.tail().tail().tail());
    }
    
    @Test
    public void testMatches() {
        JsonPointer empty = JsonPointer.compile("");
        assertTrue(empty.matches());

        JsonPointer pointer1 = JsonPointer.compile("/a");
        assertFalse(pointer1.matches());

        JsonPointer pointer2 = JsonPointer.compile("/a/0");
        assertFalse(pointer2.matches());

        JsonPointer pointer3 = JsonPointer.compile("/a/0/b");
        assertFalse(pointer3.matches());

        JsonPointer pointer4 = JsonPointer.compile("/a/0/b/c");
        assertFalse(pointer4.matches());

        JsonPointer pointer5 = JsonPointer.compile("/a/0/b/c/");
        assertFalse(pointer5.matches());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer empty = JsonPointer.compile("");
        JsonPointer result1 = empty.matchProperty("a");
        assertNull(result1);

        JsonPointer pointer1 = JsonPointer.compile("/a");
        JsonPointer result2 = pointer1.matchProperty("a");
        assertNull(result2);
        JsonPointer result3 = pointer1.matchProperty("b");
        assertNull(result3);

        JsonPointer pointer2 = JsonPointer.compile("/a/0");
        JsonPointer result4 = pointer2.matchProperty("a");
        assertNull(result4);
        JsonPointer result5 = pointer2.matchProperty("0");
        assertNull(result5);
        JsonPointer result6 = pointer2.matchProperty("b");
        assertNull(result6);

        JsonPointer pointer3 = JsonPointer.compile("/a/0/b");
        JsonPointer result7 = pointer3.matchProperty("a");
        assertNull(result7);
        JsonPointer result8 = pointer3.matchProperty("b");
        assertNull(result8);
        JsonPointer result9 = pointer3.matchProperty("c");
        assertNull(result9);

        JsonPointer pointer4 = JsonPointer.compile("/a/0/b/c");
        JsonPointer result10 = pointer4.matchProperty("a");
        assertNull(result10);
        JsonPointer result11 = pointer4.matchProperty("b");
        assertNull(result11);
        JsonPointer result12 = pointer4.matchProperty("c");
        assertNull(result12);
        JsonPointer result13 = pointer4.matchProperty("d");
        assertNull(result13);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer empty = JsonPointer.compile("");
        JsonPointer result1 = empty.matchElement(0);
        assertNull(result1);

        JsonPointer pointer1 = JsonPointer.compile("/a");
        JsonPointer result2 = pointer1.matchElement(0);
        assertNull(result2);
        JsonPointer result3 = pointer1.matchElement(1);
        assertNull(result3);

        JsonPointer pointer2 = JsonPointer.compile("/0");
        JsonPointer result4 = pointer2.matchElement(0);
        assertNull(result4);
        JsonPointer result5 = pointer2.matchElement(1);
        assertNull(result5);

        JsonPointer pointer3 = JsonPointer.compile("/a/0");
        JsonPointer result6 = pointer3.matchElement(0);
        assertNull(result6);
        JsonPointer result7 = pointer3.matchElement(1);
        assertNull(result7);
        JsonPointer result8 = pointer3.matchElement(2);
        assertNull(result8);

        JsonPointer pointer4 = JsonPointer.compile("/a/0/b");
        JsonPointer result9 = pointer4.matchElement(0);
        assertNull(result9);
        JsonPointer result10 = pointer4.matchElement(1);
        assertNull(result10);
        JsonPointer result11 = pointer4.matchElement(2);
        assertNull(result11);
        JsonPointer result12 = pointer4.matchElement(3);
        assertNull(result12);

        JsonPointer pointer5 = JsonPointer.compile("/a/0/b/1");
        JsonPointer result13 = pointer5.matchElement(0);
        assertNull(result13);
        JsonPointer result14 = pointer5.matchElement(1);
        assertNull(result14);
        JsonPointer result15 = pointer5.matchElement(2);
        assertNull(result15);
        JsonPointer result16 = pointer5.matchElement(3);
        assertNull(result16);
        JsonPointer result17 = pointer5.matchElement(4);
        assertNull(result17);
    }
    
    @Test
    public void testToString() {
        JsonPointer empty = JsonPointer.compile("");
        assertEquals("", empty.toString());

        JsonPointer pointer1 = JsonPointer.compile("/a");
        assertEquals("/a", pointer1.toString());

        JsonPointer pointer2 = JsonPointer.compile("//");
        assertEquals("//", pointer2.toString());

        JsonPointer pointer3 = JsonPointer.compile("/a/0");
        assertEquals("/a/0", pointer3.toString());

        JsonPointer pointer4 = JsonPointer.compile("/a/0/b");
        assertEquals("/a/0/b", pointer4.toString());

        JsonPointer pointer5 = JsonPointer.compile("/a/0/b/c");
        assertEquals("/a/0/b/c", pointer5.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/a");
        JsonPointer pointer2 = JsonPointer.compile("/a");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/a");
        JsonPointer pointer2 = JsonPointer.compile("/a");
        assertTrue(pointer1.equals(pointer2));
    }
}
