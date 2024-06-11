import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Basic functionality test
        assertEquals(3, Utf8.encodedLength("abc"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Basic functionality test
        assertEquals(2, Utf8.encodedLengthGeneral("哈", 0));
    }

    @Test
    public void testIsWellFormed() {
        // Basic functionality test
        byte[] bytes = new byte[] { 65, 66, 67 };
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithOffsets() {
        // Basic functionality test
        byte[] bytes = new byte[] { 65, 66, 67 };
        assertTrue(Utf8.isWellFormed(bytes, 1, 2));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Basic functionality test
        byte[] bytes = new byte[] { 65, -112, 66, 67 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 1, 3));
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testEncodedLengthWithEmptyString() {
        // Testing with an empty string
        assertEquals(0, Utf8.encodedLength(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithLongerLength() {
        // Testing when the UTF-8 length does not fit in int
        Utf8.encodedLength("abcdefghijklmnop");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthGeneralWithUnpairedSurrogate() {
        // Testing when an unpaired surrogate is encountered
        Utf8.encodedLengthGeneral("abc\uD800def", 3);
    }

    @Test
    public void testIsWellFormedWithInvalidByte() {
        // Testing with a byte value less than 0
        byte[] bytes = new byte[] { -65, 66, 67 };
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithInvalidAsciiBytes() {
        // Testing with ASCII bytes followed by an invalid byte
        byte[] bytes = new byte[] { 65, 66, 127 };
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPathWithOverlongUtf8Sequence() {
        // Testing with an overlong UTF-8 sequence
        byte[] bytes = new byte[] { -64, -128, 66, 67 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, 4));
    }

    @Test
    public void testIsWellFormedSlowPathWithInvalidContinuationByte() {
        // Testing with an invalid continuation byte
        byte[] bytes = new byte[] { -32, -96, 66, 127 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, 4));
    }

    @Test
    public void testIsWellFormedSlowPathWithInvalidUtf8Sequence() {
        // Testing with invalid UTF-8 sequence
        byte[] bytes = new byte[] { -16, -96, -96, 127 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, 4));
    }

}