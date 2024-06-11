import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digits = {'1', '2', '3'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_string() {
        String number = "456";
        int result = NumberInput.parseInt(number);
        assertEquals(456, result);
    }

    @Test
    public void testParseLong_charArray() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(digits, 0, 10);
        assertEquals(1234567890, result);
    }

    @Test
    public void testParseLong_string() {
        String number = "9876543210";
        long result = NumberInput.parseLong(number);
        assertEquals(9876543210L, result);
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digits = {'9', '2', '2', '3', '5', '0', '7', '3', '8', '5', '8', '5', '0', '7', '2', '0', '1', '2', 'e', '-', '8', '3', '0', '8'};
        boolean result = NumberInput.inLongRange(digits, 11, 17, true);
        assertEquals(true, result);
    }

    @Test
    public void testInLongRange_string() {
        String number = "9223372036854775807";
        boolean result = NumberInput.inLongRange(number, false);
        assertEquals(true, result);
    }

    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = -1;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "9876543210";
        long defaultValue = -1;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(9876543210L, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "3.14";
        double defaultValue = -1.0;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(3.14, result, 0.001);
    }

    @Test
    public void testParseDouble() {
        String numStr = "3.14";
        double result = NumberInput.parseDouble(numStr);
        assertEquals(3.14, result, 0.001);
    }

    @Test
    public void testParseBigDecimal_string() {
        String numStr = "123456789.987654321";
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123456789.987654321"), result);
    }

    @Test
    public void testParseBigDecimal_charArray_offset_length() {
        char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '9', '8', '7', '6', '5', '4', '3', '2', '1'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 18);
        assertEquals(new BigDecimal("123456789.98765432"), result);
    }

}