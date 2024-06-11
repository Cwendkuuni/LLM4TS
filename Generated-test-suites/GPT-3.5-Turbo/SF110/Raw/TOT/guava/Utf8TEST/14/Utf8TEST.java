import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_withEmptyString() {
        CharSequence sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_withAsciiCharacters() {
        CharSequence sequence = "Hello, world!";
        int expected = 13;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_withNonAsciiCharacters() {
        CharSequence sequence = "üüüüü";
        int expected = 10;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthGeneral_withEmptyString() {
        CharSequence sequence = "";
        int start = 0;
        int expected = 0;
        int actual = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthGeneral_withAsciiCharacters() {
        CharSequence sequence = "Hello, world!";
        int start = 0;
        int expected = 13;
        int actual = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthGeneral_withNonAsciiCharacters() {
        CharSequence sequence = "üüüüü";
        int start = 0;
        int expected = 10;
        int actual = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_withValidBytes() {
        byte[] bytes = {72, 101, 108, 108, 111};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_withInvalidBytes() {
        byte[] bytes = {72, -128, 108, 108, 111};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_withInvalidBytesAndOffset() {
        byte[] bytes = {72, -128, 108, 108, 111};
        int off = 0;
        int len = 3;
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_withValidBytesAndOffset() {
        byte[] bytes = {72, 101, 108, 108, 111};
        int off = 0;
        int len = 3;
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormedSlowPath_withInvalidBytes() {
        byte[] bytes = {72, -128, 108, 108, 111};
        int off = 0;
        int end = bytes.length;
        boolean expected = false;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormedSlowPath_withValidBytes() {
        byte[] bytes = {72, 101, 108, 108, 111};
        int off = 0;
        int end = bytes.length;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
    }

}