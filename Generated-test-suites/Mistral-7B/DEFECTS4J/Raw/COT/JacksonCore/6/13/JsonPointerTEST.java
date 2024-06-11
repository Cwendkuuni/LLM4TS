package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.IllegalArgumentException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

    /**
     * Test constructor with empty input.
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
        assertEquals(empty, empty.tail());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertFalse(empty.matches());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstructor() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertEquals("/a/b/c", pointer.toString());
        assertEquals(null, pointer.tail());
        assertEquals("c", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    /**
     * Test parseTail method with valid input.
     */
    @Test
    public void testParseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/a/b/c");
        assertEquals("/a/b", pointer.toString());
        assertSame(null, pointer.tail());
    }

    /**
     * Test parseTail method with '/' as first character.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseTailWithSlashAsFirstCharacter() {
        JsonPointer._parseTail("/");
    }

    /**
     * Test parseTail method with empty input.
     */
    @Test
    public void testParseTailWithEmptyInput() {
        JsonPointer pointer = JsonPointer._parseTail("");
        assertEquals("", pointer.toString());
        assertSame(JsonPointer.EMPTY, pointer);
    }

    /**
     * Test compile method with valid input.
     */
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/a/b/c");
        assertEquals("/a/b/c", pointer.toString());
        assertEquals(null, pointer.tail());
        assertEquals("c", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    /**
     * Test compile method with empty input.
     */
    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    /**
     * Test compile method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("ab");
    }

    /**
     * Test fromSegment method with valid input.
     */
    /*
     * TODO: Implement fromSegment method and add test case here
     */

    /**
     * Test matches method with empty pointer.
     */
    @Test
    public void testMatchesWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
    }

    /**
     * Test matches method with non-empty pointer.
     */
    @Test
    public void testMatchesWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertFalse(pointer.matches());
    }

    /**
     * Test getMatchingProperty method with empty pointer.
     */
    @Test
    public void testGetMatchingPropertyWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertNull(empty.getMatchingProperty());
    }

    /**
     * Test getMatchingProperty method with non-empty pointer.
     */
    @Test
    public void testGetMatchingPropertyWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertEquals("c", pointer.getMatchingProperty());
    }

    /**
     * Test getMatchingIndex method with empty pointer.
     */
    @Test
    public void testGetMatchingIndexWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals(-1, empty.getMatchingIndex());
    }

    /**
     * Test getMatchingIndex method with non-empty pointer.
     */
    @Test
    public void testGetMatchingIndexWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/0", "0", null);
        assertEquals(0, pointer.getMatchingIndex());
    }

    /**
     * Test mayMatchProperty method with empty pointer.
     */
    @Test
    public void testMayMatchPropertyWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(empty.mayMatchProperty());
    }

    /**
     * Test mayMatchProperty method with non-empty pointer.
     */
    @Test
    public void testMayMatchPropertyWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertTrue(pointer.mayMatchProperty());
    }

    /**
     * Test mayMatchElement method with empty pointer.
     */
    @Test
    public void testMayMatchElementWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(empty.mayMatchElement());
    }

    /**
     * Test mayMatchElement method with non-empty pointer.
     */
    @Test
    public void testMayMatchElementWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/0", null, null);
        assertTrue(pointer.mayMatchElement());
    }

    /**
     * Test matchProperty method with empty pointer.
     */
    @Test
    public void testMatchPropertyWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertNull(empty.matchProperty(""));
        assertNull(empty.matchProperty("a"));
    }

    /**
     * Test matchProperty method with non-empty pointer.
     */
    @Test
    public void testMatchPropertyWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertSame(pointer, pointer.matchProperty("c"));
        assertNull(pointer.matchProperty("a"));
    }

    /**
     * Test matchElement method with empty pointer.
     */
    @Test
    public void testMatchElementWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertNull(empty.matchElement(-1));
        assertNull(empty.matchElement(0));
    }

    /**
     * Test matchElement method with non-empty pointer.
     */
    @Test
    public void testMatchElementWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/0", null, null);
        assertSame(pointer, pointer.matchElement(0));
        assertNull(pointer.matchElement(-1));
    }

    /**
     * Test tail method with empty pointer.
     */
    @Test
    public void testTailWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertNull(empty.tail());
    }

    /**
     * Test tail method with non-empty pointer.
     */
    @Test
    public void testTailWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertSame(pointer, pointer.tail());
    }

    /**
     * Test toString method with empty pointer.
     */
    @Test
    public void testToStringWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
    }

    /**
     * Test toString method with non-empty pointer.
     */
    @Test
    public void testToStringWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertEquals("/a/b/c", pointer.toString());
    }

    /**
     * Test hashCode method with empty pointer.
     */
    @Test
    public void testHashCodeWithEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals(0, empty.hashCode());
    }

    /**
     * Test hashCode method with non-empty pointer.
     */
    @Test
    public void testHashCodeWithNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        int hashCode = pointer.hashCode();
        JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
        assertEquals(hashCode, pointer2.hashCode());
    }

    /**
     * Test equals method with empty pointer and null.
     */
    @Test
    public void testEqualsWithEmptyPointerAndNull() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(empty.equals(null));
    }

    /**
     * Test equals method with empty pointer and another empty pointer.
     */
    @Test
    public void testEqualsWithEmptyPointerAndAnotherEmptyPointer() {
        JsonPointer empty1 = JsonPointer.EMPTY;
        JsonPointer empty2 = JsonPointer.EMPTY;
        assertTrue(empty1.equals(empty2));
    }

    /**
     * Test equals method with empty pointer and non-empty pointer.
     */
    @Test
    public void testEqualsWithEmptyPointerAndNonEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        assertFalse(empty.equals(pointer));
    }

    /**
     * Test equals method with non-empty pointer and another non-empty pointer.
     */
    @Test
    public void testEqualsWithNonEmptyPointerAndAnotherNonEmptyPointer() {
        JsonPointer pointer1 = new JsonPointer("/a/b/c", "c", null);
        JsonPointer pointer2 = new JsonPointer("/a/b/c", "c", null);
        assertTrue(pointer1.equals(pointer2));
    }

    /**
     * Test equals method with non-empty pointer and empty pointer.
     */
    @Test
    public void testEqualsWithNonEmptyPointerAndEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/a/b/c", "c", null);
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(pointer.equals(empty));
    }
}