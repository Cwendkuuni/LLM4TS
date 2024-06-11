import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    // Test cases for parseInt(char[] digitChars, int offset, int len)

    @Test
    public void testParseInt() {
        char[] digitChars = { '1', '2', '3' };
        int offset = 0;
        int len = 3;
        int expected = 123;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, result);
    }

    // More test cases for parseInt(char[] digitChars, int offset, int len) here...

    // Test cases for parseInt(String str)

    @Test
    public void testParseIntWithValidInput() {
        String str = "123";
        int expected = 123;
        int result = NumberInput.parseInt(str);
        assertEquals(expected, result);
    }

    // More test cases for parseInt(String str) here...

    // Test cases for parseLong(char[] digitChars, int offset, int len)

    @Test
    public void testParseLong() {
        char[] digitChars = { '1', '2', '3' };
        int offset = 0;
        int len = 3;
        long expected = 123;
        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expected, result);
    }

    // More test cases for parseLong(char[] digitChars, int offset, int len) here...

    // Test cases for parseLong(String str)

    @Test
    public void testParseLongWithValidInput() {
        String str = "123";
        long expected = 123;
        long result = NumberInput.parseLong(str);
        assertEquals(expected, result);
    }

    // More test cases for parseLong(String str) here...

    // Test cases for inLongRange(char[] digitChars, int offset, int len, boolean negative)

    @Test
    public void testInLongRangeWithValidInput() {
        char[] digitChars = { '1', '2', '3' };
        int offset = 0;
        int len = 3;
        boolean negative = false;
        boolean expected = true;
        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);
        assertEquals(expected, result);
    }

    // More test cases for inLongRange(char[] digitChars, int offset, int len, boolean negative) here...

    // Test cases for inLongRange(String numberStr, boolean negative)

    @Test
    public void testInLongRangeWithValidInput() {
        String numberStr = "123";
        boolean negative = false;
        boolean expected = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expected, result);
    }

    // More test cases for inLongRange(String numberStr, boolean negative) here...

    // Test cases for parseAsInt(String input, int defaultValue)

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "123";
        int defaultValue = 0;
        int expected = 123;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expected, result);
    }

    // More test cases for parseAsInt(String input, int defaultValue) here...

    // Test cases for parseAsLong(String input, long defaultValue)

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "123";
        long defaultValue = 0L;
        long expected = 123L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expected, result);
    }

    // More test cases for parseAsLong(String input, long defaultValue) here...

    // Test cases for parseAsDouble(String input, double defaultValue)

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double defaultValue = 0.0;
        double expected = 123.45;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expected, result, 0.001);
    }

    // More test cases for parseAsDouble(String input, double defaultValue) here...

    // Test cases for parseDouble(String numStr)

    @Test
    public void testParseDoubleWithValidInput() {
        String numStr = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseDouble(numStr);
        assertEquals(expected, result, 0.001);
    }

    // More test cases for parseDouble(String numStr) here...

    // Test cases for parseBigDecimal(String numStr)

    @Test
    public void testParseBigDecimalWithValidInput() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    // More test cases for parseBigDecimal(String numStr) here...

    // Test cases for parseBigDecimal(char[] buffer)

    @Test
    public void testParseBigDecimalWithValidInput() {
        char[] buffer = { '1', '2', '3', '.', '4', '5' };
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, result);
    }

    // More test cases for parseBigDecimal(char[] buffer) here...

    // Test cases for parseBigDecimal(char[] buffer, int offset, int len)

    @Test
    public void testParseBigDecimalWithValidInput() {
        char[] buffer = { '1', '2', '3', '.', '4', '5' };
        int offset = 0;
        int len = 6;
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(expected, result);
    }

    // More test cases for parseBigDecimal(char[] buffer, int offset, int len) here...

}