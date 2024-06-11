package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt_basic() {
        char[] input = {'1', '2', '3'};
        int result = NumberInput.parseInt(input, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_empty() {
        char[] input = {};
        int result = NumberInput.parseInt(input, 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseInt_edgeCase() {
        char[] input = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '0'};
        int result = NumberInput.parseInt(input, 0, 10);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseInt_exception() {
        char[] input = {'a'};
        try {
            NumberInput.parseInt(input, 0, 1);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // Other test cases for the remaining methods in NumberInput class would go here

}