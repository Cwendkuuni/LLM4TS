
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digits = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digits, 0, 3));
    }

    @Test
    public void testParseLong() {
        char[] digits = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        assertEquals(1000000000L, NumberInput.parseLong(digits, 0, 10));
    }

    @Test
    public void testParseIntFromString() {
        assertEquals(456, NumberInput.parseInt("456"));
    }

    @Test
    public void testParseLongFromString() {
        assertEquals(1000000000L, NumberInput.parseLong("1000000000"));
    }

    @Test
    public void testInLongRangeTrue() {
        char[] digits = {'1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digits, 0, 3, false));
    }

    @Test
    public void testInLongRangeFalse() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertFalse(NumberInput.inLongRange(digits, 0, 10, false));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(789, NumberInput.parseAsInt("789", 0));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(1000000000L, NumberInput.parseAsLong("1000000000", 0));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(3.14, NumberInput.parseAsDouble("3.14", 0.0), 0.01);
    }

    @Test
    public void testParseDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble("2.2250738585072012e-308"), 0.0);
    }

    @Test
    public void testParseBigDecimalFromString() {
        assertEquals(new BigDecimal("123.456"), NumberInput.parseBigDecimal("123.456"));
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(new BigDecimal("1234567890"), NumberInput.parseBigDecimal(digits));
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithOffset() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(new BigDecimal("67890"), NumberInput.parseBigDecimal(digits, 5, 5));
    }
}
