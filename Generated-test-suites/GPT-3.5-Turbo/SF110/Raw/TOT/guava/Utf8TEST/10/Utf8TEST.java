import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        // Typical case: input sequence with length = 10
        CharSequence sequence = "HelloWorld";
        int expectedLength = 10;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLength_emptyString() {
        // Edge case: empty string
        CharSequence sequence = "";
        int expectedLength = 0;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLength_extendedAsciiChars() {
        // Edge case: input sequence with extended ASCII characters
        CharSequence sequence = "€";
        int expectedLength = 3;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_largeUtf8Length() {
        // Error scenario: expected UTF-8 length larger than int
        CharSequence sequence = "a\u0800";
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_typicalCase() {
        // Typical case: well-formed byte array
        byte[] bytes = {72, 101, 108, 108, 111};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertTrue(isWellFormed);
    }

    @Test
    public void testIsWellFormed_emptyByteArray() {
        // Edge case: empty byte array
        byte[] bytes = {};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertTrue(isWellFormed);
    }

    @Test
    public void testIsWellFormed_invalidByte() {
        // Error scenario: invalid byte in byte array
        byte[] bytes = {-1, 0, 5};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);
    }

    @Test
    public void testIsWellFormed_slowPath() {
        // Edge case: byte array triggering slow path in isWellFormed method
        byte[] bytes = {72, -64};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);
    }

    @Test
    public void testIsWellFormed_slowPath_extendedCase() {
        // Edge case: byte array triggering extended slow path in isWellFormed method
        byte[] bytes = {72, -30, -120, -106};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsWellFormed_slowPath_indexOutOfBounds() {
        // Error scenario: index out of bounds in slow path
        byte[] bytes = {72, -64};
        Utf8.isWellFormedSlowPath(bytes, 2, 4);
    }
}