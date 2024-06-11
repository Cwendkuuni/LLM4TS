import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {
    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    // Test for edge cases and exceptions:
    @Test(expected = IllegalArgumentException.class)
    public void testParseIntFromCharArrayWithInvalidLength() {
        char[] digitChars = new char[]{'1', '2'};
        NumberInput.parseInt(digitChars, 0, digitChars.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromCharArrayWithInvalidDigits() {
        char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
        NumberInput.parseInt(digitChars, 0, digitChars.length);
    }
}