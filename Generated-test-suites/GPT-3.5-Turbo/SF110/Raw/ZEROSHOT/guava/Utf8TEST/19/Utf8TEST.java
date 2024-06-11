import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test sequence with characters < '\u0080'
        assertEquals(5, Utf8.encodedLength("abcde"));

        // Test sequence with characters >= '\u0800'
        assertEquals(9, Utf8.encodedLength("abcdeاbcde"));

        // Test sequence with unpaired surrogate
        try {
            Utf8.encodedLength("abcde\ud800");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        // Test sequence with UTF-8 length that does not fit in int
        try {
            Utf8.encodedLength("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[]{0x61}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xC2, (byte) 0xA9}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0xA4, (byte) 0xB9, (byte) 0xE0, (byte) 0xA4, (byte) 0xBF, (byte) 0xE0, (byte) 0xA4, (byte) 0xBE}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x82}));

        // Test ill-formed bytes
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xC2}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0xA4}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xF0, (byte) 0x9F}));

        // Test well-formed bytes with offset and length
        assertTrue(Utf8.isWellFormed(new byte[]{0x63, 0x61, 0x74, 0x73, 0x20, 0x65, 0x61, 0x74}, 1, 6));

        // Test ill-formed bytes with offset and length
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0x80, (byte) 0x91, (byte) 0xE0, (byte) 0xA4, (byte) 0x81}, 0, 6));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test well-formed bytes
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{0x61}, 0, 1));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0xC2, (byte) 0xA9}, 0, 2));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0xE0, (byte) 0xA4, (byte) 0xB9, (byte) 0xE0, (byte) 0xA4, (byte) 0xBF, (byte) 0xE0, (byte) 0xA4, (byte) 0xBE}, 0, 9));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x82}, 0, 4));

        // Test ill-formed bytes
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0x80}, 0, 1));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0xC2}, 0, 1));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0xE0, (byte) 0xA4}, 0, 2));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{(byte) 0xF0, (byte) 0x9F}, 0, 2));
    }
}