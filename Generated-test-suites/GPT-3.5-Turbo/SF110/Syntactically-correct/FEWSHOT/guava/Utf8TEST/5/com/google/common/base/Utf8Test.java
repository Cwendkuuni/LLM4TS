import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(6, Utf8.encodedLength("abc123"));
        assertEquals(1, Utf8.encodedLength("a"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(3, Utf8.encodedLengthGeneral("abc123", 0));
        assertEquals(1, Utf8.encodedLengthGeneral("a", 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = { 97, 98, 99 };
        byte[] bytes2 = { -61, -75, -30, -128, -72 };
        byte[] bytes3 = { -16, -97, -105 };
        byte[] bytes4 = { -61, -75, -30, -128 };

        assertTrue(Utf8.isWellFormed(bytes1));
        assertFalse(Utf8.isWellFormed(bytes2));
        assertFalse(Utf8.isWellFormed(bytes3));
        assertFalse(Utf8.isWellFormed(bytes4));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes = { 97, 98, 99, -61, -75, -30, -128, -72, -16, -97 };

        assertTrue(Utf8.isWellFormed(bytes, 0, 3));
        assertFalse(Utf8.isWellFormed(bytes, 3, 6));
        assertFalse(Utf8.isWellFormed(bytes, 6, 3));
        assertFalse(Utf8.isWellFormed(bytes, 9, 1));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = { -61, -75 };
        byte[] bytes2 = { -16, -97, -105 };
        byte[] bytes3 = { -16, -97 };

        assertFalse(Utf8.isWellFormedSlowPath(bytes1, 0, 2));
        assertTrue(Utf8.isWellFormedSlowPath(bytes2, 0, 3));
        assertFalse(Utf8.isWellFormedSlowPath(bytes3, 0, 2));
    }
}