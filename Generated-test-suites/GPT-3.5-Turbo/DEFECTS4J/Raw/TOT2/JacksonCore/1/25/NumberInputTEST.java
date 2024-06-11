import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {
  
    // Test for parseInt(char[] digitChars, int offset, int len)
    @Test
    public void testParseIntCharArray() {
        char[] digits = {'1', '2', '3'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(123, result);
    }

    // Test for parseInt(String str)
    @Test
    public void testParseIntString() {
        int result = NumberInput.parseInt("456");
        assertEquals(456, result);
    }
  
    // Test for parseLong(char[] digitChars, int offset, int len)
    @Test
    public void testParseLongCharArray() {
        char[] digits = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        long result = NumberInput.parseLong(digits, 0, 10);
        assertEquals(9876543210L, result);
    }

    // Test for parseLong(String str)
    @Test
    public void testParseLongString() {
        long result = NumberInput.parseLong("1234567890");
        assertEquals(1234567890L, result);
    }

    // Test for inLongRange(char[] digitChars, int offset, int len, boolean negative)
    @Test
    public void testInLongRangeCharArray() {
        assertFalse(NumberInput.inLongRange(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}, 0, 10, false));
        assertTrue(NumberInput.inLongRange(new char[]{'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'}, 0, 10, false));
    }

    // Test for inLongRange(String numberStr, boolean negative)
    @Test
    public void testInLongRangeString() {
        assertFalse(NumberInput.inLongRange("1234567890", false));
        assertTrue(NumberInput.inLongRange("9876543210", false));
    }

    // Test for parseAsInt(String input, int defaultValue)
    @Test
    public void testParseAsInt() {
        int result = NumberInput.parseAsInt("123", 0);
        assertEquals(123, result);
    }

    // Test for parseAsLong(String input, long defaultValue)
    @Test
    public void testParseAsLong() {
        long result = NumberInput.parseAsLong("9876543210", 0L);
        assertEquals(9876543210L, result);
    }

    // Test for parseAsDouble(String input, double defaultValue)
    @Test
    public void testParseAsDouble() {
        double result = NumberInput.parseAsDouble("3.14", 0.0);
        assertEquals(3.14, result, 0.0);
    }

    // Test for parseDouble(String numStr) with NumberFormatException
    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithNumberFormatException() {
        NumberInput.parseDouble("not a number");
    }

    // Test for parseBigDecimal(String numStr) with NumberFormatException
    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithNumberFormatException() {
        NumberInput.parseBigDecimal("not a number");
    }
  
    // Test for parseBigDecimal(char[] buffer) with NumberFormatException
    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalCharArrayWithNumberFormatException() {
        NumberInput.parseBigDecimal(new char[]{'a', 'b', 'c'});
    }

}