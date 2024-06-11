import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(4, Utf8.encodedLength("hello"));
        assertEquals(6, Utf8.encodedLength("こんにちは"));
        assertEquals(12, Utf8.encodedLength("世界"));
        assertEquals(9, Utf8.encodedLength("😊"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));
        assertEquals(2, Utf8.encodedLengthGeneral("世界", 0));
        assertEquals(3, Utf8.encodedLengthGeneral("😊", 0));
        assertEquals(0, Utf8.encodedLengthGeneral("hello", 5));
        assertEquals(2, Utf8.encodedLengthGeneral("こんにちは", 3));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[] {}));
        assertTrue(Utf8.isWellFormed(new byte[] { 72, 101, 108, 108, 111 }));
        assertTrue(Utf8.isWellFormed(new byte[] { -27, -81, -95, -27, -70, -125 })); // 'こんにちは' in UTF-8
        assertTrue(Utf8.isWellFormed(new byte[] { -29, -127, -111, -24, -74, -66 })); // '世界' in UTF-8
        assertTrue(Utf8.isWellFormed(new byte[] { -16, -97, -104, -128 })); // '😊' in UTF-8
        
        assertFalse(Utf8.isWellFormed(new byte[] { -62, -75, -32, -96, -79 })); // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(new byte[] { 72, 101, -101, 108, 108, 111 })); // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(new byte[] { -27, -81, -95, -27 })); // Incomplete UTF-8 sequence
    }

    @Test
    public void testIsWellFormedSlowPath() {
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {}, 0, 0));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] { 72, 101, 108, 108, 111 }, 0, 5));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] { -27, -81, -95, -27, -70, -125 }, 0, 6)); // 'こんにちは' in UTF-8
        
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { -62, -75, -32, -96, -79 }, 0, 5)); // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { 72, 101, -101, 108, 108, 111 }, 0, 6)); // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { -27, -81, -95, -27 }, 0, 4)); // Incomplete UTF-8 sequence
    }
}