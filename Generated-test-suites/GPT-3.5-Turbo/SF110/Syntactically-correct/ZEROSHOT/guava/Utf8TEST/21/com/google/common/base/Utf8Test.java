import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test case 1
        assertEquals(4, Utf8.encodedLength("abcd"));

        // Test case 2
        assertEquals(6, Utf8.encodedLength("αβγδε"));

        // Test case 3
        assertEquals(9, Utf8.encodedLength("漢字テスト"));

        // Test case 4
        assertEquals(1, Utf8.encodedLength("a"));

        // Test case 5
        assertEquals(2, Utf8.encodedLength("あ"));

        // Test case 6
        assertEquals(3, Utf8.encodedLength("𠜎"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_Exception() {
        Utf8.encodedLength(new String(new char[Integer.MAX_VALUE / 3]));
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test case 1
        assertEquals(3, Utf8.encodedLengthGeneral("abcd", 0));

        // Test case 2
        assertEquals(2, Utf8.encodedLengthGeneral("αβγδε", 0));

        // Test case 3
        assertEquals(3, Utf8.encodedLengthGeneral("漢字テスト", 0));

        // Test case 4
        assertEquals(1, Utf8.encodedLengthGeneral("a", 0));

        // Test case 5
        assertEquals(3, Utf8.encodedLengthGeneral("あ", 0));

        // Test case 6
        assertEquals(4, Utf8.encodedLengthGeneral("𠜎", 0));
    }

    @Test
    public void testIsWellFormed() {
        // Test case 1
        byte[] bytes1 = {97, 98, 99, 100};
        assertTrue(Utf8.isWellFormed(bytes1));

        // Test case 2
        byte[] bytes2 = {(byte) -30, (byte) -120, (byte) -105, (byte) -30, (byte) -120, (byte) -104, (byte) -30, (byte) -120, (byte) -103, (byte) -30, (byte) -120, (byte) -102, (byte) -30, (byte) -120, (byte) -101};
        assertTrue(Utf8.isWellFormed(bytes2));

        // Test case 3
        byte[] bytes3 = {(byte) -24, (byte) -95, (byte) -100, (byte) -24, (byte) -95, (byte) -71, (byte) -24, (byte) -95, (byte) -80, (byte) -24, (byte) -95, (byte) -66, (byte) -24, (byte) -95, (byte) -82, (byte) -24, (byte) -95, (byte) -109};
        assertTrue(Utf8.isWellFormed(bytes3));

        // Test case 4
        byte[] bytes4 = {97};
        assertTrue(Utf8.isWellFormed(bytes4));

        // Test case 5
        byte[] bytes5 = {(byte) -61, (byte) -92};
        assertFalse(Utf8.isWellFormed(bytes5));

        // Test case 6
        byte[] bytes6 = {(byte) -49, (byte) -126, (byte) -49, (byte) -97};
        assertFalse(Utf8.isWellFormed(bytes6));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test case 1
        byte[] bytes1 = {(byte) -61, (byte) -92};
        assertFalse(Utf8.isWellFormedSlowPath(bytes1, 0, bytes1.length));

        // Test case 2
        byte[] bytes2 = {(byte) -49, (byte) -126, (byte) -49, (byte) -97};
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, 0, bytes2.length));

        // Test case 3
        byte[] bytes3 = {(byte) -49, (byte) -126, (byte) -49, (byte) -97};
        assertFalse(Utf8.isWellFormedSlowPath(bytes3, 2, bytes3.length));

        // Test case 4
        byte[] bytes4 = {(byte) -49, (byte) -126, (byte) -49};
        assertFalse(Utf8.isWellFormedSlowPath(bytes4, 0, bytes4.length));

        // Test case 5
        byte[] bytes5 = {(byte) -49, (byte) -126};
        assertFalse(Utf8.isWellFormedSlowPath(bytes5, 0, bytes5.length));
    }
}