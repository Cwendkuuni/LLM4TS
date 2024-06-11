import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntCharArrayIntInt() {
        char[] input = {'1', '2', '3'};
        int result = NumberInput.parseInt(input, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntString() {
        int result = NumberInput.parseInt("456");
        assertEquals(456, result);
    }

    @Test
    public void testParseLongCharArrayIntInt() {
        char[] input = {'1', '2', '3'};
        long result = NumberInput.parseLong(input, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseLongString() {
        long result = NumberInput.parseLong("456");
        assertEquals(456, result);
    }

    @Test
    public void testInLongRangeCharArrayIntIntBoolean() {
        char[] input = {'9', '2', '2', '3', '3', '0', '7', '1', '2'};
        boolean result = NumberInput.inLongRange(input, 0, 9, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRangeStringBoolean() {
        boolean result = NumberInput.inLongRange("922330712", false);
        assertTrue(result);
    }

    @Test
    public void testParseAsIntStringInt() {
        int result = NumberInput.parseAsInt("+123", 0);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsLongStringLong() {
        long result = NumberInput.parseAsLong("-456", 0);
        assertEquals(-456, result);
    }

    @Test
    public void testParseAsDoubleStringDouble() {
        double result = NumberInput.parseAsDouble("3.14", 0.0);
        assertEquals(3.14, result, 0.0);
    }

    @Test
    public void testParseDouble() {
        double result = NumberInput.parseDouble("3.14");
        assertEquals(3.14, result, 0.0);
    }

    @Test
    public void testParseBigDecimalString() {
        BigDecimal result = NumberInput.parseBigDecimal("3.14");
        assertEquals(new BigDecimal("3.14"), result);
    }

    @Test
    public void testParseBigDecimalCharArray() {
        char[] input = {'3', '.', '1', '4'};
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("3.14"), result);
    }

    @Test
    public void testParseBigDecimalCharArrayIntInt() {
        char[] input = {'3', '.', '1', '4'};
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, 4);
        assertEquals(new BigDecimal("3.14"), result);
    }

}