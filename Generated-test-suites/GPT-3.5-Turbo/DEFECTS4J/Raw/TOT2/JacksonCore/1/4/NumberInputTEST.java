import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digits = {'1', '2', '3'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_string() {
        int result = NumberInput.parseInt("456");
        assertEquals(456, result);
    }

    @Test
    public void testParseLong_charArray() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        long result = NumberInput.parseLong(digits, 0, 12);
        assertEquals(123456789012L, result);
    }

    @Test
    public void testParseLong_string() {
        long result = NumberInput.parseLong("1234567890");
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseAsInt() {
        int result = NumberInput.parseAsInt("abc123", 0);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsLong() {
        long result = NumberInput.parseAsLong("xyz789", 0);
        assertEquals(789, result);
    }

    @Test
    public void testParseAsDouble() {
        double result = NumberInput.parseAsDouble("3.14", 0.0);
        assertEquals(3.14, result, 0.0001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDouble_exception() {
        NumberInput.parseDouble("abc");
    }

    @Test
    public void testParseBigDecimal_string() {
        BigDecimal result = NumberInput.parseBigDecimal("123.456");
        assertEquals(new BigDecimal("123.456"), result);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] digits = {'1', '2', '3', '4', '5', '6'};
        BigDecimal result = NumberInput.parseBigDecimal(digits);
        assertEquals(new BigDecimal("123456"), result);
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digits = {'9', '2', '2', '3', '3', '8', '5', '8', '5', '0', '7', '2', '0', '1', '2'};
        boolean result = NumberInput.inLongRange(digits, 0, 15, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_string() {
        boolean result = NumberInput.inLongRange("9223372036854775807", true);
        assertTrue(result);
    }

}