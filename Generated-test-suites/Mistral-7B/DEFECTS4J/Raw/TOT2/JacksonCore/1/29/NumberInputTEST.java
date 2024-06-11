import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntFromSmallArray() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromString() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromSmallArray() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromString() {
        String input = "123456789";
        long expected = 123456789;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromArrayOutOfBounds() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        try {
            NumberInput.parseLong(digitChars, 0, 6);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromStringWithInvalidFormat() {
        String input = "abcd";
        try {
            NumberInput.parseLong(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseIntWithNegativeSignFromString() {
        String input = "-123";
        int expected = -123;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithNegativeSignFromString() {
        String input = "-123456789";
        long expected = -123456789;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }
}