package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] input = {'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(input, 0, 5);
        assertEquals(12345, result);
    }

    @Test
    public void testParseInt_String() {
        String input = "6789";
        int result = NumberInput.parseInt(input);
        assertEquals(6789, result);
    }

    // Add more test cases for other methods in NumberInput class

}