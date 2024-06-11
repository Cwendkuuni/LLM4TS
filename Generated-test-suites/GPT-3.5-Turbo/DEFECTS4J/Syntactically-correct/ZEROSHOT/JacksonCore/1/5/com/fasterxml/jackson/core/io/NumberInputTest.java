import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digitChars = {'1', '2', '3', '4'};
        int expected = 1234;
        int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInt_String() {
        String str = "1234";
        int expected = 1234;
        int result = NumberInput.parseInt(str);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_charArray() {
        char[] digitChars = {'1', '2', '3', '4'};
        int offset = 0;
        int len = 4;
        long expected = 1234L;
        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_String() {
        String str = "1234";
        long expected = 1234L;
        long result = NumberInput.parseLong(str);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digitChars = {'1', '2', '3', '4'};
        int offset = 0;
        int len = 4;
        boolean negative = false;
        boolean expected = true;
        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_String() {
        String numberStr = "123456789";
        boolean negative = false;
        boolean expected = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsInt() {
        String input = "1234";
        int defaultValue = 0;
        int expected = 1234;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "123456789";
        long defaultValue = 0L;
        long expected = 123456789L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "1.234";
        double defaultValue = 0.0;
        double expected = 1.234;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testParseDouble() {
        String numStr = "1.234";
        double expected = 1.234;
        double result = NumberInput.parseDouble(numStr);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testParseBigDecimal_String() {
        String numStr = "1.234";
        BigDecimal expected = new BigDecimal(numStr);
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] buffer = {'1', '2', '3', '4'};
        BigDecimal expected = new BigDecimal("1234");
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray_offset_len() {
        char[] buffer = {'1', '2', '3', '4'};
        int offset = 0;
        int len = 4;
        BigDecimal expected = new BigDecimal("1234");
        BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(expected, result);
    }
}