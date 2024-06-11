import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int result = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntWithNegative() {
        char[] digitChars = {'-', '5', '0', '0'};
        int result = NumberInput.parseInt(digitChars, 0, 4);
        assertEquals(-500, result);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLongWithNegative() {
        char[] digitChars = {'-', '9', '8', '7', '6', '5', '4', '3', '2', '1'};
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(-987654321L, result);
    }

    @Test
    public void testParseBigDecimal() {
        String numStr = "12345.6789";
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("12345.6789"), result);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] buffer = {'1', '2', '3', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(new BigDecimal("12345"), result);
    }

}