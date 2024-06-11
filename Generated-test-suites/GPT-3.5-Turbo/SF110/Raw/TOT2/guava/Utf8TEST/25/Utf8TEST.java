package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(6, Utf8.encodedLength("hello")); // Typical case
        assertEquals(9, Utf8.encodedLength("çø∑")); // Edge case with special characters
        // Add more test cases here for edge cases and error scenarios
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80}; // Valid emoji byte sequence
        assertTrue(Utf8.isWellFormed(bytes1)); // Typical case
        byte[] bytes2 = {(byte) 0xC0}; // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(bytes2)); // Error scenario
        // Add more test cases here for edge cases and error scenarios
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes = {(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80}; // Valid emoji byte sequence
        assertTrue(Utf8.isWellFormed(bytes, 0, bytes.length)); // Typical case
        // Add more test cases here for edge cases and error scenarios
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = {(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80}; // Valid emoji byte sequence
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, bytes1.length)); // Typical case
        byte[] bytes2 = {(byte) 0xC0}; // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, 0, bytes2.length)); // Error scenario
        // Add more test cases here for edge cases and error scenarios
    }

    // Additional test cases can be added here for any remaining methods in the Utf8 class

}