import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typical() {
        CharSequence sequence = "hello world";
        int expected = 11;
        int result = Utf8.encodedLength(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodedLength_emptyString() {
        CharSequence sequence = "";
        int expected = 0;
        int result = Utf8.encodedLength(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodedLength_singleCharacter() {
        CharSequence sequence = "a";
        int expected = 1;
        int result = Utf8.encodedLength(sequence);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodedLength_specialCharacter() {
        CharSequence sequence = "你好";
        int expected = 6;
        int result = Utf8.encodedLength(sequence);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_overflow() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_typical() {
        byte[] bytes = { 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        boolean expected = true;
        boolean result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormed_emptyArray() {
        byte[] bytes = {};
        boolean expected = true;
        boolean result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormed_invalidBytes() {
        byte[] bytes = { -1, 0, 127 };
        boolean expected = false;
        boolean result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormed_slowPath() {
        byte[] bytes = { -61, -92, -62, -96 };
        boolean expected = true;
        boolean result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormed_slowPath_invalidBytes() {
        byte[] bytes = { -61, -92, -128 };
        boolean expected = false;
        boolean result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_typical() {
        byte[] bytes = { -61, -92, -62, -96 };
        int off = 0;
        int len = 4;
        boolean expected = true;
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_emptyArray() {
        byte[] bytes = {};
        int off = 0;
        int len = 0;
        boolean expected = true;
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_invalidBytes() {
        byte[] bytes = { -61, -92, -128 };
        int off = 0;
        int len = 3;
        boolean expected = false;
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_invalidSurrogatePair() {
        byte[] bytes = { -16, -65, -93, -16, -65, -93 };
        int off = 0;
        int len = 6;
        boolean expected = false;
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, result);
    }
}