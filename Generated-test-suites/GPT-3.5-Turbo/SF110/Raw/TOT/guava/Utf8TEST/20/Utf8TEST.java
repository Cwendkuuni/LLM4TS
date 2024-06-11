package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalUseCase() {
        String input = "Hello World";
        int expected = 11;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEncodedLength_emptyString() {
        String input = "";
        int expected = 0;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEncodedLength_edgeCase_longString() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec nec odio.";
        int expected = 75;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_errorCase_overflow() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec nec odio.";
        // Length greater than maximum int value
        int expected = 4294967369;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_typicalUseCase() {
        byte[] bytes = {(byte) 72, (byte) 101, (byte) 108, (byte) 108, (byte) 111, (byte) 32, (byte) 87, (byte) 111, (byte) 114, (byte) 108, (byte) 100};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormed_emptyArray() {
        byte[] bytes = {};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormed_edgeCase_maximumBytes() {
        byte[] bytes = {(byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 127, (byte) 79};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormed_errorCase_invalidBytes() {
        byte[] bytes = {(byte) 72, (byte) 101, (byte) 108, (byte) 108, (byte) 111, (byte) -1, (byte) 87, (byte) 111, (byte) 114, (byte) 108, (byte) 100};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormed_errorCase_invalidBytes_multiple() {
        byte[] bytes = {(byte) 72, (byte) 101, (byte) 108, (byte) 108, (byte) 111, (byte) -1, (byte) -1, (byte) 87, (byte) 111, (byte) 114, (byte) 108, (byte) 100};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

}