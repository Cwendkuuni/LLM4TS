import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_validInput() {
        // positive input
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        // negative input
        assertEquals(-987654321, NumberInput.parseInt("-987654321"));
        // leading plus sign
        assertEquals(456789012, NumberInput.parseInt("+456789012"));
    }

    @Test
    public void testParseInt_invalidInput() {
        // empty input
        assertEquals(0, NumberInput.parseInt(""));
        // non-numeric input
        try {
            NumberInput.parseInt("123abc");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // pass
        }
        // input with too many digits
        try {
            NumberInput.parseInt("1234567890");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // pass
        }
    }

    @Test
    public void testParseLong_validInput() {
        // positive input
        assertEquals(1234567890123456789L, NumberInput.parseLong("1234567890123456789"));
        // negative input
        assertEquals(-987654321098765432L, NumberInput.parseLong("-987654321098765432"));
        // leading plus sign
        assertEquals(9876543210987654321L, NumberInput.parseLong("+9876543210987654321"));
    }

    @Test
    public void testParseLong_invalidInput() {
        // empty input
        assertEquals(0L, NumberInput.parseLong(""));
        // non-numeric input
        try {
            NumberInput.parseLong("123abc");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // pass
        }
        // input with too many digits
        try {
            NumberInput.parseLong("12345678901234567890");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // pass
        }
    }

    @Test
    public void testInLongRange_validInput() {
        // input that fits in range
        assertTrue(NumberInput.inLongRange("123456789", false));
        assertTrue(NumberInput.inLongRange("9876543210", false));
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
    }

    @Test
    public void testInLongRange_invalidInput() {
        // input that doesn't fit in range
        assertFalse(NumberInput.inLongRange("12345678901", false));
        assertFalse(NumberInput.inLongRange("98765432109876543210", false));
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
    }

    @Test
    public void testParseAsInt_validInput() {
        // valid positive input
        assertEquals(123456789, NumberInput.parseAsInt("123456789", 0));
        // valid negative input
        assertEquals(-987654321, NumberInput.parseAsInt("-987654321", 0));
    }

    @Test
    public void testParseAsInt_invalidInput() {
        // empty input
        assertEquals(0, NumberInput.parseAsInt("", 0));
        // non-numeric input
        assertEquals(0, NumberInput.parseAsInt("123abc", 0));
    }

    @Test
    public void testParseAsLong_validInput() {
        // valid positive input
        assertEquals(1234567890123456789L, NumberInput.parseAsLong("1234567890123456789", 0L));
        // valid negative input
        assertEquals(-987654321098765432L, NumberInput.parseAsLong("-987654321098765432", 0L));
    }

    @Test
    public void testParseAsLong_invalidInput() {
        // empty input
        assertEquals(0L, NumberInput.parseAsLong("", 0L));
        // non-numeric input
        assertEquals(0L, NumberInput.parseAsLong("123abc", 0L));
    }

    @Test
    public void testParseAsDouble_validInput() {
        // valid positive input
        assertEquals(1234.56789, NumberInput.parseAsDouble("1234.56789", 0.0), 0.0001);
        // valid negative input
        assertEquals(-9876.54321, NumberInput.parseAsDouble("-9876.54321", 0.0), 0.0001);
    }

    @Test
    public void testParseAsDouble_invalidInput() {
        // empty input
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.0001);
        // non-numeric input
        assertEquals(0.0, NumberInput.parseAsDouble("123abc", 0.0), 0.0001);
    }

    @Test
    public void testParseBigDecimal_validInput() {
        // valid input
        assertEquals(new BigDecimal("1234567890.0987654321"), NumberInput.parseBigDecimal("1234567890.0987654321"));
    }

    @Test
    public void testParseBigDecimal_invalidInput() {
        // empty input
        try {
            NumberInput.parseBigDecimal("");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // pass
        }
        // non-numeric input
        try {
            NumberInput.parseBigDecimal("123abc");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // pass
        }
    }
}