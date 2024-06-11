package com.fasterxml.jackson.core.io;
import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] input = {'1', '2', '3'};
        int result = NumberInput.parseInt(input, 0, 3);
        
        assertEquals(123, result);
    }
    
    @Test
    public void testParseIntWithEdgeCaseInput() {
        char[] input = {'2', '1', '4', '7', '4', '8', '3', '6', '4', '7'};
        int result = NumberInput.parseInt(input, 0, 10);
        
        assertEquals(Integer.MAX_VALUE, result);
    }
    
    @Test
    public void testParseIntWithInvalidInput() {
        char[] input = {'a', 'b', 'c'};
        int result = NumberInput.parseInt(input, 0, 3);
        
        assertEquals(0, result);
    }

    // Add more test cases for other methods

}