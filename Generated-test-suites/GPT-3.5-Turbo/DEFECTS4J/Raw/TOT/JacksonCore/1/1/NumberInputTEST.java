import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // Typical use case
        assertEquals(123, NumberInput.parseInt("123"));

        // Edge cases
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));

        // Error scenarios
        // Empty string
        try {
            NumberInput.parseInt("");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }

        // Non-digit character
        try {
            NumberInput.parseInt("123a");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }

        // Number too large
        try {
            NumberInput.parseInt("10000000000");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseLong() {
        // Typical use case
        assertEquals(12345L, NumberInput.parseLong("12345"));

        // Edge cases
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));

        // Error scenarios
        // Empty string
        try {
            NumberInput.parseLong("");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }

        // Non-digit character
        try {
            NumberInput.parseLong("12345a");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }

        // Number too large
        try {
            NumberInput.parseLong("10000000000000000000");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseAsInt() {
        // Typical use case
        assertEquals(123, NumberInput.parseAsInt("123", 0));

        // Edge cases
        assertEquals(Integer.MAX_VALUE, NumberInput.parseAsInt(String.valueOf(Integer.MAX_VALUE), 0));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseAsInt(String.valueOf(Integer.MIN_VALUE), 0));

        // Error scenarios
        // Empty string
        assertEquals(0, NumberInput.parseAsInt("", 0));

        // Non-digit character
        try {
            NumberInput.parseAsInt("123a", 0);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseAsLong() {
        // Typical use case
        assertEquals(12345L, NumberInput.parseAsLong("12345", 0L));

        // Edge cases
        assertEquals(Long.MAX_VALUE, NumberInput.parseAsLong(String.valueOf(Long.MAX_VALUE), 0L));
        assertEquals(Long.MIN_VALUE, NumberInput.parseAsLong(String.valueOf(Long.MIN_VALUE), 0L));

        // Error scenarios
        // Empty string
        assertEquals(0L, NumberInput.parseAsLong("", 0L));

        // Non-digit character
        try {
            NumberInput.parseAsLong("12345a", 0L);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParseAsDouble() {
        // Typical use case
        assertEquals(12.34, NumberInput.parseAsDouble("12.34", 0.0), 0.001);

        // Edge cases
        assertEquals(Double.MAX_VALUE, NumberInput.parseAsDouble(String.valueOf(Double.MAX_VALUE), 0.0), 0.001);

        // Error scenarios
        // Empty string
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.001);

        // Non-digit character
        try {
            NumberInput.parseAsDouble("12.34a", 0.0);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBigDecimal() {
        // Typical use case
        assertEquals(new BigDecimal("12.34"), NumberInput.parseBigDecimal("12.34"));

        // Error scenarios
        // Empty string
        try {
            NumberInput.parseBigDecimal("");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }

        // Non-digit character
        try {
            NumberInput.parseBigDecimal("12.34a");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

}