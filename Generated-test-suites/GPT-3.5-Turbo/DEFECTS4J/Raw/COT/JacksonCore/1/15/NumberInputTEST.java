import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // basic test case
        char[] digitChars = {'1', '2', '3', '4'};
        int offset = 0;
        int len = 4;
        int expected = 1234;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntEmptyString() {
        // test case for empty string
        char[] digitChars = {};
        int offset = 0;
        int len = 0;
        int expected = 0;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntSingleDigit() {
        // test case for single digit
        char[] digitChars = {'9'};
        int offset = 0;
        int len = 1;
        int expected = 9;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntMultipleDigits() {
        // test case for multiple digits
        char[] digitChars = {'1', '0', '0', '0'};
        int offset = 0;
        int len = 4;
        int expected = 1000;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, result);
    }
    
    // ... Repeat the above test structure for each method in the class ...

    @Test
    public void testParseBigDecimal() {
        // basic test case
        String numStr = "1234";
        BigDecimal expected = new BigDecimal(numStr);
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalEmptyString() {
        // test case for empty string
        String numStr = "";
        BigDecimal expected = new BigDecimal(numStr);
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalLongString() {
        // test case for long string
        String numStr = "12345678901234567890";
        BigDecimal expected = new BigDecimal(numStr);
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }
}