import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntCharArray() {
        char[] input = {'1', '2', '3'};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntString() {
        String input = "456";
        int result = NumberInput.parseInt(input);
        assertEquals(456, result);
    }

    @Test
    public void testParseLongCharArray() {
        char[] input = {'1', '2', '3'};
        long result = NumberInput.parseLong(input, 0, input.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseLongString() {
        String input = "456";
        long result = NumberInput.parseLong(input);
        assertEquals(456, result);
    }

    @Test
    public void testInLongRangeCharArray() {
        char[] input = {'9', '2', '2', '3', '3', '7', '8', '5', '8', '5', '0', '7', '2', '0', '1', '2', 'e', '-', '3', '1', '0', '8'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, false);
        assertTrue(result);
    }

    @Test
    public void testParseAsInt() {
        String input = "-789";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(-789, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "-12345";
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals(-12345, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "3.14";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(3.14, result, 0.0001);
    }

    @Test
    public void testParseDouble() {
        String input = "3.14";
        double result = NumberInput.parseDouble(input);
        assertEquals(3.14, result, 0.0001);
    }

    @Test
    public void testParseBigDecimalString() {
        String input = "12345678901234567890";
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals("12345678901234567890", result.toString());
    }

    @Test
    public void testParseBigDecimalCharArray() {
        char[] input = {'1', '2', '3', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals("12345", result.toString());
    }

}