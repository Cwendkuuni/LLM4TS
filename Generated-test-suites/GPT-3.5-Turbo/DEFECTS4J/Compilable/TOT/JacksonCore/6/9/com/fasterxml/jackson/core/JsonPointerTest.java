package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testJsonPointerCompile() {
        // Test Empty Pointer
        JsonPointer emptyPointer = JsonPointer.compile("");
        assertTrue(emptyPointer.matches());

        // Test Non-Empty Pointer
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertFalse(pointer.matches());
    }

    @Test
    public void testJsonPointerValueOf() {
        // Test Empty Pointer
        JsonPointer emptyPointer = JsonPointer.valueOf("");
        assertTrue(emptyPointer.matches());

        // Test Non-Empty Pointer
        JsonPointer pointer = JsonPointer.valueOf("/test/property");
        assertFalse(pointer.matches());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        
        // Test Matching Property
        JsonPointer matchedPointer = pointer.matchProperty("property");
        assertNotNull(matchedPointer);

        // Test Non-Matching Property
        JsonPointer nonMatchedPointer = pointer.matchProperty("nonMatchingProperty");
        assertNull(nonMatchedPointer);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/test/2");
        
        // Test Matching Element
        JsonPointer matchedPointer = pointer.matchElement(2);
        assertNotNull(matchedPointer);

        // Test Non-Matching Element
        JsonPointer nonMatchedPointer = pointer.matchElement(5);
        assertNull(nonMatchedPointer);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        
        // Test Tail
        JsonPointer tailPointer = pointer.tail();
        assertEquals("/property", tailPointer.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        
        // Test ToString
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/test/property");
        JsonPointer pointer2 = JsonPointer.compile("/test/property");
        JsonPointer pointer3 = JsonPointer.compile("/test/nonMatchingProperty");
        
        // Test Equals
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }
}