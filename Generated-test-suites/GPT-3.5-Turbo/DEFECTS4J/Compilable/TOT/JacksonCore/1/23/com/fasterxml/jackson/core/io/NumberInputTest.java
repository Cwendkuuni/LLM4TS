package com.fasterxml.jackson.core.io;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] testInput = {'1', '2', '3'};
        int expectedOutput = 123;
        int actualOutput = NumberInput.parseInt(testInput, 0, testInput.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithNegativeInput() {
        char[] testInput = {'-', '9'};
        int expectedOutput = -9;
        int actualOutput = NumberInput.parseInt(testInput, 0, testInput.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithLeadingZeros() {
        char[] testInput = {'0', '0', '1', '0'};
        int expectedOutput = 10;
        int actualOutput = NumberInput.parseInt(testInput, 0, testInput.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithEmptyInput() {
        char[] testInput = {};
        int expectedOutput = 0;
        int actualOutput = NumberInput.parseInt(testInput, 0, testInput.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithInvalidCharacter() {
        char[] testInput = {'1', '2', 'a'};
        int expectedOutput = 0;
        int actualOutput = NumberInput.parseInt(testInput, 0, testInput.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithStringInput() {
        String testInput = "123";
        int expectedOutput = 123;
        int actualOutput = NumberInput.parseInt(testInput);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithNegativeStringInput() {
        String testInput = "-9";
        int expectedOutput = -9;
        int actualOutput = NumberInput.parseInt(testInput);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithLeadingZerosStringInput() {
        String testInput = "0010";
        int expectedOutput = 10;
        int actualOutput = NumberInput.parseInt(testInput);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseIntWithInvalidCharacterStringInput() {
        String testInput = "12a";
        int expectedOutput = 0;
        int actualOutput = NumberInput.parseInt(testInput);
        assertEquals(expectedOutput, actualOutput);
    }

    // Similar tests for other methods...

}