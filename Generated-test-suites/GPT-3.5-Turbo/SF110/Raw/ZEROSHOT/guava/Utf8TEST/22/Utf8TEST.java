import org.junit.Test;

import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test sequence with only ASCII characters
        assertEquals(7, Utf8.encodedLength("Hello!"));

        // Test sequence with non-ASCII characters
        assertEquals(8, Utf8.encodedLength("你好"));

        // Test sequence with surrogate pairs
        assertEquals(6, Utf8.encodedLength("𝄞𝅥"));

        // Test sequence with invalid UTF-8 characters
        try {
            Utf8.encodedLength("Hello\uFFFD");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[]{72, 101, 108, 108, 111}));

        // Test malformed bytes
        assertFalse(Utf8.isWellFormed(new byte[]{72, -101, 108, 108, 111}));

        // Test well-formed bytes with offset and length
        assertTrue(Utf8.isWellFormed(new byte[]{72, -61, -80, 101, 108, 108, 111}, 1, 3));

        // Test malformed bytes with offset and length
        assertFalse(Utf8.isWellFormed(new byte[]{72, -61, -100, 101, 108, 108, 111}, 1, 3));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test well-formed slow path bytes
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{-61, -80, -62, -96}));

        // Test malformed slow path bytes
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{-61, -100, -62, -96}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthOverflow() {
        // Test encodedLength with UTF-8 length that does not fit in int
        Utf8.encodedLength("hello\u0104\u0105\u0106\u0107");
    }
    
}