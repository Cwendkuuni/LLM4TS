import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // typical use case
        assertEquals(4, Utf8.encodedLength("test")); 
        // edge case - empty string
        assertEquals(0, Utf8.encodedLength(""));
        // error scenario - input contains unpaired surrogates
        assertThrows(IllegalArgumentException.class, () -> {
            Utf8.encodedLength("test\ud800");
        });
    }

    @Test
    public void testIsWellFormed() {
        // typical use case
        assertTrue(Utf8.isWellFormed(new byte[]{0, 1, 2, 3, 4}));
        // edge case - single negative byte
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) -128}));
        // error scenario - invalid UTF-8 byte sequence
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) -32, (byte) -128}));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // typical use case
        assertTrue(Utf8.isWellFormed(new byte[]{0, 1, 2, 3, 4}, 0, 5));
        // edge case - empty input array
        assertTrue(Utf8.isWellFormed(new byte[]{}, 0, 0));
        // error scenario - offset and length out of range
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Utf8.isWellFormed(new byte[]{0, 1, 2}, 1, 3);
        });
    }
}