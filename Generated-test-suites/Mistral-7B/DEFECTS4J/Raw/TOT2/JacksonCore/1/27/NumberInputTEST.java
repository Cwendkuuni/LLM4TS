import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray_ValidInput() {
        char[] input = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(input, 0, input.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromString_ValidInput() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromCharArray_ValidInput() {
        char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        long actual = NumberInput.parseLong(input, 0, input.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromString_ValidInput() {
        String input = "123456789";
        long expected = 123456789;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromCharArray_InvalidInput() {
        char[] input = new char[]{'a', 'b', 'c', 'd'};
        NumberInput.parseInt(input, 0, input.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromString_InvalidInput() {
        String input = "abcd";
        NumberInput.parseInt(input);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromCharArray_InvalidInput() {
        char[] input = new char[]{'1', '2', '3', 'a', 'b'};
        NumberInput.parseLong(input, 0, input.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromString_InvalidInput() {
        String input = "123abcd";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseAsIntFromString_DefaultValue() {
        String input = null;
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntFromString_ValidInput() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(input, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntFromString_NegativeNumber() {
        String input = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseAsInt(input, expected);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntFromString_InvalidInput() {
        String input = "abcd";
        NumberInput.parseAsInt(input, 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongFromString_DefaultValue() {
        String input = null;
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromString_ValidInput() {
        String input = "123456789";
        long expected = 123456789;
        long actual = NumberInput.parseAsLong(input, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromString_NegativeNumber() {
        String input = "-123456789";
        long expected = -123456789;
        long actual = NumberInput.parseAsLong(input, expected);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongFromString_InvalidInput() {
        String input = "abcd";
        NumberInput.parseAsLong(input, 0);
    }

    @Test
    public void testParseAsDoubleFromString_DefaultValue() {
        String input = null;
        double expected = 0.0;
        double actual = NumberInput.parseAsDouble(input, expected);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testParseAsDoubleFromString_ValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, expected);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testParseAsDoubleFromString_NegativeNumber() {
        String input = "-123.45";
        double expected = -123.45;
        double actual = NumberInput.parseAsDouble(input, expected);
        assertEquals(expected, actual, 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleFromString_InvalidInput() {
        String input = "abcd";
        NumberInput.parseAsDouble(input, 0.0);
    }
}