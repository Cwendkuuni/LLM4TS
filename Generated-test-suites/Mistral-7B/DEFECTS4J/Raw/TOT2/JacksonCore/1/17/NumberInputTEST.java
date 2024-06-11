import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expectedResult = 12345;
        int actualResult = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseIntWithEdgeCases() {
        char[] digitCharsMin = new char[]{'-', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        char[] digitCharsMax = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int expectedResultMin = Long.MIN_VALUE;
        int expectedResultMax = Long.MAX_VALUE;
        int actualResultMin = NumberInput.parseInt(digitCharsMin, 0, 10);
        int actualResultMax = NumberInput.parseInt(digitCharsMax, 0, 10);
        assertEquals(expectedResultMin, actualResultMin);
        assertEquals(expectedResultMax, actualResultMax);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitCharsInvalid = new char[]{'a', '2', '3', '4', '5'};
        NumberInput.parseInt(digitCharsInvalid, 0, 5);
    }
}