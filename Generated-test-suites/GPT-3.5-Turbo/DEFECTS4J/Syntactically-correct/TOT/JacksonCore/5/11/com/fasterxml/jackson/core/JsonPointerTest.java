package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testCompile_EmptyInput_ReturnsEmptyPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testCompile_InvalidInput_ThrowsIllegalArgumentException() {
        try {
            JsonPointer.compile("invalid");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
        }
    }

    @Test
    public void testCompile_ValidInput_ReturnsValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testValueOf_ValidInput_ReturnsValidPointer() {
        JsonPointer pointer = JsonPointer.valueOf("/property/0");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testMatches_MatchingProperty_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testMatches_MatchingIndex_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertTrue(pointer.matches());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMatches_NonMatchingProperty_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertFalse(pointer.matchProperty("nonMatching"));
    }

    @Test
    public void testMatches_NonMatchingIndex_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertFalse(pointer.matchElement(1));
    }

    @Test
    public void testMatchProperty_MatchingProperty_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertEquals(pointer.tail(), pointer.matchProperty("property"));
    }

    @Test
    public void testMatchProperty_NonMatchingProperty_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertNull(pointer.matchProperty("nonMatching"));
    }

    @Test
    public void testMatchElement_MatchingIndex_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(pointer.tail(), pointer.matchElement(0));
    }

    @Test
    public void testMatchElement_NonMatchingIndex_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertNull(pointer.matchElement(1));
    }

    @Test
    public void testTail_Success_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertEquals(pointer.tail(), pointer.tail());
    }

    @Test
    public void testToString_Success_ReturnsStringRepresentation() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertEquals("/property/0", pointer.toString());
    }

    @Test
    public void testHashCode_Success_ReturnsHashCode() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertEquals("/property/0".hashCode(), pointer.hashCode());
    }

    @Test
    public void testEquals_Null_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertFalse(pointer.equals(null));
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertTrue(pointer.equals(pointer));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        assertFalse(pointer.equals("pointer"));
    }

    @Test
    public void testEquals_DifferentPointer_ReturnsFalse() {
        JsonPointer pointer1 = JsonPointer.compile("/property/0");
        JsonPointer pointer2 = JsonPointer.compile("/property/1");
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testEquals_SamePointer_ReturnsTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/property/0");
        JsonPointer pointer2 = JsonPointer.compile("/property/0");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testParseIndex_ValidIndex_ReturnsParsedIndex() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseIndex", String.class);
        method.setAccessible(true);

        // Test valid indexes
        int index0 = (int) method.invoke(null, "0");
        int index1 = (int) method.invoke(null, "1");
        int index9 = (int) method.invoke(null, "9");
        int index10 = (int) method.invoke(null, "10");
        int index100 = (int) method.invoke(null, "100");

        assertEquals(0, index0);
        assertEquals(1, index1);
        assertEquals(9, index9);
        assertEquals(10, index10);
        assertEquals(100, index100);
    }

    @Test
    public void testParseIndex_InvalidIndex_ReturnsMinusOne() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseIndex", String.class);
        method.setAccessible(true);

        // Test invalid indexes
        int indexEmpty = (int) method.invoke(null, "");
        int indexNegative = (int) method.invoke(null, "-1");
        int indexFloat = (int) method.invoke(null, "1.5");
        int indexInvalidChar = (int) method.invoke(null, "a");
        int indexTooLong = (int) method.invoke(null, "12345678901");

        assertEquals(-1, indexEmpty);
        assertEquals(-1, indexNegative);
        assertEquals(-1, indexFloat);
        assertEquals(-1, indexInvalidChar);
        assertEquals(-1, indexTooLong);
    }

    @Test
    public void testParseTail_NoEscapes_ReturnsValidPointer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseTail", String.class);
        method.setAccessible(true);

        // Test tail without escapes
        JsonPointer pointer = (JsonPointer) method.invoke(null, "/property/0");

        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testParseTail_EscapedCharacters_ReturnsValidPointer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseTail", String.class);
        method.setAccessible(true);

        // Test tail with escaped characters
        JsonPointer pointer = (JsonPointer) method.invoke(null, "/property~0name~1with~0escapes");

        assertFalse(pointer.matches());
        assertEquals("property~0name~1with~0escapes", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testParseTail_InvalidTail_ThrowsIllegalArgumentException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseTail", String.class);
        method.setAccessible(true);

        try {
            // Test tail with invalid characters
            JsonPointer pointer = (JsonPointer) method.invoke(null, "/invalid~tail");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalArgumentException);
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"/invalid~tail\"", cause.getMessage());
        }
    }

    @Test
    public void testParseQuotedTail_EscapedCharacters_ReturnsValidPointer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseQuotedTail", String.class, int.class);
        method.setAccessible(true);

        // Test tail with escaped characters
        JsonPointer pointer = (JsonPointer) method.invoke(null, "/property~0name~1with~0escapes", 10);

        assertFalse(pointer.matches());
        assertEquals("property~0name~1with~0escapes", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testParseQuotedTail_InvalidTail_ThrowsIllegalArgumentException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private method
        Method method = JsonPointer.class.getDeclaredMethod("_parseQuotedTail", String.class, int.class);
        method.setAccessible(true);

        try {
            // Test tail with invalid characters
            JsonPointer pointer = (JsonPointer) method.invoke(null, "/invalid~tail", 10);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalArgumentException);
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"/invalid~tail\"", cause.getMessage());
        }
    }

    private void assertThrows(Class<? extends Throwable> expectedType, Runnable code) {
        try {
            code.run();
            fail("Expected " + expectedType.getSimpleName() + " to be thrown");
        } catch (Throwable e) {
            assertTrue("Expected " + expectedType.getSimpleName() + " to be thrown, but got " + e.getClass().getSimpleName(), expectedType.isInstance(e));
        }
    }
}