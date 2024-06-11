import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_basic() {
        assertEquals(6, Utf8.encodedLength("Hello"));
    }

    @Test
    public void testEncodedLength_emptyString() {
        assertEquals(0, Utf8.encodedLength(""));
    }

    @Test
    public void testEncodedLength_invalidUtf8() {
        try {
            Utf8.encodedLength("\uD801");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsWellFormed_true() {
        byte[] bytes = {(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormed_false() {
        byte[] bytes = {(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0xB1};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormed_invalidUtf8() {
        byte[] bytes = {(byte)0xC0, (byte)0x80};
        assertFalse(Utf8.isWellFormed(bytes));
    }
}