package com.fasterxml.jackson.core.io;
import org.junit.Assert;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_withValidInput() {
        char[] digitChars = {'1', '2', '3'};
        int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
        Assert.assertEquals(123, result);
    }

    @Test
    public void testParseInt_withEmptyArray() {
        char[] digitChars = {};
        int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testParseInt_withNegativeNumber() {
        char[] digitChars = {'-', '5', '0'};
        int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
        Assert.assertEquals(-50, result);
    }

    @Test
    public void testParseInt_withInvalidInput() {
        char[] digitChars = {'1', '2', 'a'};
        try {
            NumberInput.parseInt(digitChars, 0, digitChars.length);
            Assert.fail("NumberFormatException expected");
        } catch (NumberFormatException e) {
            // expected
        }
    }
    
    // More test cases for other public methods
    
    // Helper methods for edge cases and exception handling scenarios
    // ...
}