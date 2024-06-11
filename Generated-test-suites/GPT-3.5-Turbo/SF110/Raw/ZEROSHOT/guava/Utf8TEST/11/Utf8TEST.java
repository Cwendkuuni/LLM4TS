import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_SingleByte() {
        CharSequence sequence = "Hello";
        int expected = 5;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_TwoBytes() {
        CharSequence sequence = "Γειά";
        int expected = 8;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_ThreeBytes() {
        CharSequence sequence = "こんにちは";
        int expected = 15;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_FourBytes() {
        CharSequence sequence = "🌍";
        int expected = 4;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_Exception() {
        CharSequence sequence = "This is a very long string that exceeds the maximum length";
        try {
            Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("UTF-8 length does not fit in int: 4294967370", e.getMessage());
        }
    }

    @Test
    public void testIsWellFormed_SingleByte() {
        byte[] bytes = {(byte)65, (byte)66, (byte)67};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_MultipleBytes() {
        byte[] bytes = {(byte)-30, (byte)-128, (byte)-100, (byte)-29, (byte)-127, (byte)-101};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_Exception() {
        byte[] bytes = {(byte)-30, (byte)-128, (byte)-100, (byte)-29, (byte)-127, (byte)100};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_SpecifiedRange() {
        byte[] bytes = {(byte)-30, (byte)-128, (byte)-100, (byte)-29, (byte)-127, (byte)-101};
        int off = 1;
        int len = 4;
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_SpecifiedRange_Exception() {
        byte[] bytes = {(byte)-30, (byte)-128, (byte)-100, (byte)-29, (byte)-127, (byte)100};
        int off = 1;
        int len = 5;
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_SingleByte() {
        byte[] bytes = {(byte)65, (byte)66, (byte)67};
        int off = 0;
        int end = 3;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_MultipleBytes() {
        byte[] bytes = {(byte)-30, (byte)-128, (byte)-100, (byte)-29, (byte)-127, (byte)-101};
        int off = 0;
        int end = 6;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_Exception() {
        byte[] bytes = {(byte)-30, (byte)-128, (byte)-100, (byte)-29, (byte)-127, (byte)100};
        int off = 0;
        int end = 6;
        boolean expected = false;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
    }

}