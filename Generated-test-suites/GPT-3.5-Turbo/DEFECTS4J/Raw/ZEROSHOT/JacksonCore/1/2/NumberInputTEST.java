import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;

        int result = NumberInput.parseInt(digitChars, offset, len);

        assertEquals(123, result);
    }

    @Test
    public void testParseIntWithString() {
        String str = "456";

        int result = NumberInput.parseInt(str);

        assertEquals(456, result);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = {'7', '8', '9'};
        int offset = 0;
        int len = 3;

        long result = NumberInput.parseLong(digitChars, offset, len);

        assertEquals(789, result);
    }

    @Test
    public void testParseLongWithString() {
        String str = "987654321";

        long result = NumberInput.parseLong(str);

        assertEquals(987654321, result);
    }

    @Test
    public void testInLongRangeDigitChars() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int offset = 0;
        int len = 5;
        boolean negative = false;

        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);

        assertTrue(result);
    }

    @Test
    public void testInLongRangeNumberStr() {
        String numberStr = "987654321";
        boolean negative = false;

        boolean result = NumberInput.inLongRange(numberStr, negative);

        assertTrue(result);
    }

    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = 0;

        int result = NumberInput.parseAsInt(input, defaultValue);

        assertEquals(123, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "987654321";
        long defaultValue = 0;

        long result = NumberInput.parseAsLong(input, defaultValue);

        assertEquals(987654321, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "3.14";
        double defaultValue = 0.0;

        double result = NumberInput.parseAsDouble(input, defaultValue);

        assertEquals(3.14, result, 0.001);
    }

    @Test
    public void testParseDouble() {
        String numStr = "2.2250738585072012e-308";

        try {
            double result = NumberInput.parseDouble(numStr);

            assertEquals(Double.MIN_VALUE, result, 0.001);
        } catch (NumberFormatException e) {
            fail("NumberFormatException should not be thrown");
        }
    }

    @Test
    public void testParseBigDecimalWithString() {
        String numStr = "9.87654321";

        try {
            BigDecimal result = NumberInput.parseBigDecimal(numStr);

            assertEquals(new BigDecimal("9.87654321"), result);
        } catch (NumberFormatException e) {
            fail("NumberFormatException should not be thrown");
        }
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = {'1', '2', '3', '4', '5'};
        int offset = 0;
        int len = 5;

        try {
            BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);

            assertEquals(new BigDecimal("12345"), result);
        } catch (NumberFormatException e) {
            fail("NumberFormatException should not be thrown");
        }
    }
}