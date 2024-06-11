import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // Typical use case
        assertEquals(123, NumberInput.parseInt(new char[]{'1','2','3'}, 0, 3));
        // Edge case
        assertEquals(0, NumberInput.parseInt(new char[]{'0'}, 0, 1));
        // Error scenario
        try {
            NumberInput.parseInt(new char[]{'a','b','c'}, 0, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Success
        }
    }

    @Test
    public void testParseLong() {
        // Typical use case
        assertEquals(123L, NumberInput.parseLong(new char[]{'1','2','3'}, 0, 3));
        // Edge case
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        // Error scenario
        try {
            NumberInput.parseLong("123abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Success
        }
    }

    @Test
    public void testParseIntString() {
        // Typical use case
        assertEquals(123, NumberInput.parseInt("123"));
        // Edge case
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        // Error scenario
        try {
            NumberInput.parseInt("abc123");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Success
        }
    }

    // Repeat the above pattern for other methods

}