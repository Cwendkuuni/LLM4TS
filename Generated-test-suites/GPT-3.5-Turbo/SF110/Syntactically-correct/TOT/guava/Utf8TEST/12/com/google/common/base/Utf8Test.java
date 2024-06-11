import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_typical() {
        // typical use case
        String input = "hello world";
        int expected = 11;
        int result = Utf8.encodedLength(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodedLength_edge() {
        // edge case: empty string
        String input = "";
        int expected = 0;
        int result = Utf8.encodedLength(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_error() {
        // error scenario: UTF-8 length does not fit in int
        String input = "\uD83D\uDE00"; // emoji
        Utf8.encodedLength(input);
    }

    @Test
    public void testIsWellFormed_typical() {
        // typical use case
        byte[] input = {(byte) 104, (byte) 101, (byte) 108, (byte) 108, (byte) 111};
        boolean expected = true;
        boolean result = Utf8.isWellFormed(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormed_edge() {
        // edge case: empty byte array
        byte[] input = {};
        boolean expected = true;
        boolean result = Utf8.isWellFormed(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormed_error() {
        // error scenario: invalid UTF-8 byte sequence
        byte[] input = {(byte) 104, (byte) 229};
        boolean expected = false;
        boolean result = Utf8.isWellFormed(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_typical() {
        // typical use case
        byte[] input = {(byte) 104, (byte) -61, (byte) -87, (byte) 111}; // "håo"
        int off = 1;
        int len = 2;
        boolean expected = true;
        boolean result = Utf8.isWellFormedSlowPath(input, off, len);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_edge() {
        // edge case: off and end are equal
        byte[] input = {(byte) 104, (byte) 229, (byte) 111}; // "håo"
        int off = 2;
        int len = 0;
        boolean expected = true;
        boolean result = Utf8.isWellFormedSlowPath(input, off, len);
        assertEquals(expected, result);
    }

    @Test
    public void testIsWellFormedSlowPath_error() {
        // error scenario: invalid UTF-8 byte sequence
        byte[] input = {(byte) 104, (byte) -61, (byte) 111};
        int off = 1;
        int len = 2;
        boolean expected = false;
        boolean result = Utf8.isWellFormedSlowPath(input, off, len);
        assertEquals(expected, result);
    }

}