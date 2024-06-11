import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical Case
        assertEquals(5, Utf8.encodedLength("hello"));

        // Edge Case
        assertEquals(0, Utf8.encodedLength(""));

        // Error Scenario
        try {
            Utf8.encodedLength(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        // Typical Case
        byte[] bytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        assertTrue(Utf8.isWellFormed(bytes));

        // Edge Case
        byte[] emptyBytes = {};
        assertTrue(Utf8.isWellFormed(emptyBytes));

        // Error Scenario
        byte[] invalidBytes = {(byte) 0xEF, (byte) 0xBB};
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }

    @Test
    public void testIsWellFormedWithOffset() {
        // Typical Case
        byte[] bytes = {(byte) 0x41, (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0x42};
        assertTrue(Utf8.isWellFormed(bytes, 1, 3));

        // Edge Case
        byte[] emptyBytes = {};
        assertTrue(Utf8.isWellFormed(emptyBytes, 0, 0));

        // Error Scenario
        byte[] invalidBytes = {(byte) 0xEF, (byte) 0xBB};
        assertFalse(Utf8.isWellFormed(invalidBytes, 0, 2));
    }
}