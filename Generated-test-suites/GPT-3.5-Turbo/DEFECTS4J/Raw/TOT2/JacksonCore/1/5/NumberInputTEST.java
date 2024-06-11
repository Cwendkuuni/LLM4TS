import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digits = {'1', '2', '3', '4'};
        assertEquals(1234, NumberInput.parseInt(digits, 0, 4));
    }

    @Test
    public void testParseInt_String() {
        assertEquals(5678, NumberInput.parseInt("5678"));
    }

    @Test
    public void testParseLong_charArray() {
        char[] digits = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        assertEquals(9876543210L, NumberInput.parseLong(digits, 0, 10));
    }

    @Test
    public void testParseLong_String() {
        assertEquals(1234567890123456789L, NumberInput.parseLong("1234567890123456789"));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(100, NumberInput.parseAsInt("100", -1));
        assertEquals(-1, NumberInput.parseAsInt("1.23", -1));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(500L, NumberInput.parseAsLong("500", -1L));
        assertEquals(-1L, NumberInput.parseAsLong("abc", -1L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(3.14159, NumberInput.parseAsDouble("3.14159", -1.0), 0);
        assertEquals(-1.0, NumberInput.parseAsDouble("2a", -1.0), 0);
    }

    @Test
    public void testParseDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble("2.2250738585072012e-308"), 0);
        assertEquals(10.5, NumberInput.parseDouble("10.5"), 0);
    }

    @Test
    public void testParseBigDecimal_String() {
        assertEquals(new BigDecimal("1234567890123456789"), NumberInput.parseBigDecimal("1234567890123456789"));
        assertEquals(new BigDecimal("3.14159265359"), NumberInput.parseBigDecimal("3.14159265359"));
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] digits = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        assertEquals(new BigDecimal("9876543210"), NumberInput.parseBigDecimal(digits));
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digits1 = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        char[] digits2 = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '1'};
        assertTrue(NumberInput.inLongRange(digits1, 0, 10, false));
        assertFalse(NumberInput.inLongRange(digits2, 0, 10, false));
    }

    @Test
    public void testInLongRange_String() {
        assertFalse(NumberInput.inLongRange("9876543211", false));
        assertTrue(NumberInput.inLongRange("9876543210", false));
    }
}