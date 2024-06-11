package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength_basic() {
        CharSequence sequence = "Hello";
        int expectedLength = 5;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_edgeCase() {
        CharSequence sequence = "\ud800";
        int expectedLength = 3;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_exception() {
        CharSequence sequence = "Test\uffff";
        try {
            Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testIsWellFormed_basic() {
        byte[] bytes = {65, 66, 67};
        assertTrue(Utf8.isWellFormed(bytes));
    }
    
    @Test
    public void testIsWellFormed_edgeCase() {
        byte[] bytes = {-32, -80, -128};
        assertTrue(Utf8.isWellFormed(bytes));
    }
    
    @Test
    public void testIsWellFormed_exception() {
        byte[] bytes = {65, 66, (byte)0xff};
        assertFalse(Utf8.isWellFormed(bytes));
    }

}