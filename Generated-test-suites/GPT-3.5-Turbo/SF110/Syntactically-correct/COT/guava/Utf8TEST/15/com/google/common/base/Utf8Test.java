
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Basic functionality test
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(5, Utf8.encodedLength("áéíóú"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthException() {
        // Testing exception handling for UTF-8 length more than int can handle
        Utf8.encodedLength("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
    }
    
    @Test
    public void testIsWellFormed() {
        // Basic functionality test
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98, 99}));
        assertTrue(Utf8.isWellFormed(new byte[]{97, (byte) 195, (byte) 161, 98}));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormedException() {
        // Testing exception handling for invalid UTF-8 sequence
        Utf8.isWellFormed(new byte[]{97, (byte) 195, (byte) 300});
    }
    
    @Test
    public void testIsWellFormedSpecificCases() {
        // Testing edge cases for UTF-8 sequences
        assertTrue(Utf8.isWellFormed(new byte[]{0}));
        assertTrue(Utf8.isWellFormed(new byte[]{-64, -128}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte)0xFF}));
        assertFalse(Utf8.isWellFormed(new byte[]{-1}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte)0xC0, 0}));
    }
}
