import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int offset = 0, len = 5;
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, actual);

        String str = "12345";
        int num = NumberInput.parseInt(str);
        assertEquals(expected, num);
    }

    @Test
    public void testParseIntWithEdgeCases() {
        char[] digitChars1 = new char[]{'1', '2', '3', '4', '5', '6'};
        int offset1 = 0, len1 = 6;
        int expected1 = 123456;
        int actual1 = NumberInput.parseInt(digitChars1, offset1, len1);
        assertEquals(expected1, actual1);

        String str1 = "123456";
        int num1 = NumberInput.parseInt(str1);
        assertEquals(expected1, num1);

        char[] digitChars2 = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int offset2 = 0, len2 = 10;
        long expected2 = Long.parseLong("999999999999");
        long actual2 = NumberInput.parseLong(digitChars2, offset2, len2);
        assertEquals(expected2, actual2);

        String str2 = "999999999999";
        long num2 = NumberInput.parseLong(str2);
        assertEquals(expected2, num2);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInputs() {
        char[] digitChars = new char[]{'1', '2', 'x', '4', '5'};
        int offset = 0, len = 5;
        NumberInput.parseInt(digitChars, offset, len);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNegativeInputs() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int offset = 0, len = 6;
        NumberInput.parseInt(digitChars, offset, len);
    }

    @Test
    public void testParseLongWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 9;
        long expected = 123456789;
        long actual = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expected, actual);

        String str = "123456789";
        long num = NumberInput.parseLong(str);
        assertEquals(expected, num);
    }

    @Test
    public void testParseLongWithEdgeCases() {
        char[] digitChars1 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int offset1 = 0, len1 = 10;
        long expected1 = 1234567890L;
        long actual1 = NumberInput.parseLong(digitChars1, offset1, len1);
        assertEquals(expected1, actual1);

        String str1 = "1234567890";
        long num1 = NumberInput.parseLong(str1);
        assertEquals(expected1, num1);

        char[] digitChars2 = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int offset2 = 0, len2 = 11;
        long expected2 = Long.MAX_VALUE;
        long actual2 = NumberInput.parseLong(digitChars2, offset2, len2);
        assertEquals(expected2, actual2);

        String str2 = "9999999999999";
        long num2 = NumberInput.parseLong(str2);
        assertEquals(expected2, num2);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInputs() {
        char[] digitChars = new char[]{'1', '2', 'x', '4', '5', '6'};
        int offset = 0, len = 6;
        NumberInput.parseLong(digitChars, offset, len);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithNegativeInputs() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int offset = 0, len = 6;
        NumberInput.parseLong(digitChars, offset, len);
    }

    @Test
    public void testParseLongWithOutOfRangeInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9'};
        int offset = 0, len = 13;
        assertFalse(NumberInput.inLongRange(digitChars, offset, len, false));

        String str = "123456789999999999999";
        assertFalse(NumberInput.inLongRange(str, false));
    }

    @Test
    public void testParseAsIntWithValidInputs() {
        String input = "12345";
        int defaultValue = 0;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(12345, actual);
    }

    @Test
    public void testParseAsIntWithEdgeCases() {
        String input1 = "123456";
        int defaultValue = 0;
        int actual = NumberInput.parseAsInt(input1, defaultValue);
        assertEquals(123456, actual);

        String input2 = "9999999999999";
        int defaultValue1 = Integer.MIN_VALUE;
        int actual1 = NumberInput.parseAsInt(input2, defaultValue1);
        assertEquals(Integer.MIN_VALUE, actual1);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntWithInvalidInputs() {
        String input = "12x45";
        int defaultValue = 0;
        NumberInput.parseAsInt(input, defaultValue);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntWithNegativeInputs() {
        String input = "-12345";
        int defaultValue = 0;
        NumberInput.parseAsInt(input, defaultValue);
    }

    @Test
    public void testParseAsLongWithValidInputs() {
        String input = "1234567890";
        long defaultValue = 0L;
        long actual = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(1234567890L, actual);
    }

    @Test
    public void testParseAsLongWithEdgeCases() {
        String input1 = "12345678901234567890";
        long defaultValue = 0L;
        long actual = NumberInput.parseAsLong(input1, defaultValue);
        assertEquals(12345678901234567890L, actual);

        String input2 = "9999999999999999999999999999";
        long defaultValue1 = Long.MAX_VALUE;
        long actual1 = NumberInput.parseAsLong(input2, defaultValue1);
        assertEquals(Long.MAX_VALUE, actual1);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithInvalidInputs() {
        String input = "12x45";
        long defaultValue = 0L;
        NumberInput.parseAsLong(input, defaultValue);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithNegativeInputs() {
        String input = "-1234567890";
        long defaultValue = 0L;
        NumberInput.parseAsLong(input, defaultValue);
    }

    @Test
    public void testParseAsDoubleWithValidInputs() {
        String input = "123.45";
        double defaultValue = 0.0D;
        double actual = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45D, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithEdgeCases() {
        String input1 = "123.456";
        double defaultValue = 0.0D;
        double actual = NumberInput.parseAsDouble(input1, defaultValue);
        assertEquals(123.456D, actual, 0.001);

        String input2 = "-123.456";
        double defaultValue1 = 0.0D;
        double actual1 = NumberInput.parseAsDouble(input2, defaultValue1);
        assertEquals(-123.456D, actual1, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidInputs() {
        String input = "12x.45";
        double defaultValue = 0.0D;
        NumberInput.parseAsDouble(input, defaultValue);
    }

    @Test
    public void testParseDoubleWithValidInputs() {
        String numStr = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseDouble(numStr);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInputs() {
        String numStr = "12x.45";
        NumberInput.parseDouble(numStr);
    }

    @Test
    public void testParseBigDecimalWithValidInputs() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithInvalidInputs() {
        char[] buffer = new char[]{'1', '2', 'x', '4', '5'};
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(buffer, 0, buffer.length));
    }
}