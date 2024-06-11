
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_basic() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(9, Utf8.encodedLength("こんにちは"));
    }
    
    @Test
    public void testEncodedLength_edgeCases() {
        // Testing empty input
        assertEquals(0, Utf8.encodedLength(""));
        
        // Testing input with single ASCII character
        assertEquals(1, Utf8.encodedLength("a"));
        
        // Testing input with single non-ASCII character
        assertEquals(3, Utf8.encodedLength("こんにちは"));
        
        // Testing input with surrogate pair
        assertEquals(4, Utf8.encodedLength("𠜎あ"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_exception() {
        Utf8.encodedLength(new StringBuilder().appendCodePoint(0x10FFFF));
    }
    
    @Test
    public void testIsWellFormed_basic() {
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0x61, (byte)0x62, (byte)0x63}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0xE3, (byte)0x81, (byte)0x93, (byte)0xE3, (byte)0x82, (byte)0x93, (byte)0xE3, (byte)0x81, (byte)0xAB}));
    }
    
    @Test
    public void testIsWellFormed_edgeCases() {
        // Testing empty input
        assertTrue(Utf8.isWellFormed(new byte[] {}));
        
        // Testing input with single valid ASCII character
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0x61}));
        
        // Testing input with single invalid ASCII character
        assertFalse(Utf8.isWellFormed(new byte[] {(byte)0x80}));
        
        // Testing input with single valid non-ASCII character
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0xE3, (byte)0x81, (byte)0x93}));
        
        // Testing input with single invalid non-ASCII character
        assertFalse(Utf8.isWellFormed(new byte[] {(byte)0xC0, (byte)0xAF}));
    }
    
    @Test
    public void testIsWellFormedSlowPath_basic() {
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {(byte)0xE3, (byte)0x81, (byte)0x93}, 0, 3));
    }
    
    @Test
    public void testIsWellFormedSlowPath_edgeCases() {
        // Testing input with single invalid byte
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {(byte)0x80}, 0, 1));
        
        // Testing input with invalid sequence of bytes
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {(byte)0xC0, (byte)0x80}, 0, 2));
        
        // Testing input with valid sequence of bytes
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {(byte)0xE3, (byte)0x81, (byte)0x93}, 0, 3));
    }
    
}
