import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;
        int expected = 123;

        int actual = NumberInput.parseInt(digitChars, offset, len);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_withLeadingZeroes() {
        char[] digitChars = {'0', '1', '2', '3'};
        int offset = 0;
        int len = 4;
        int expected = 123;

        int actual = NumberInput.parseInt(digitChars, offset, len);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_withNegativeNumber() {
        char[] digitChars = {'-', '1', '2', '3'};
        int offset = 0;
        int len = 4;
        int expected = -123;

        int actual = NumberInput.parseInt(digitChars, offset, len);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_withEmptyArray() {
        char[] digitChars = {};
        int offset = 0;
        int len = 0;
        int expected = 0;

        int actual = NumberInput.parseInt(digitChars, offset, len);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_withSingleDigit() {
        char[] digitChars = {'7'};
        int offset = 0;
        int len = 1;
        int expected = 7;

        int actual = NumberInput.parseInt(digitChars, offset, len);

        assertEquals(expected, actual);
    }

    // Similar test cases for other public methods in NumberInput class...

}