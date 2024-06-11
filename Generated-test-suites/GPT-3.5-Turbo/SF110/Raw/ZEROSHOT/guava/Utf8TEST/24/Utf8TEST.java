import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test ASCII characters
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(4, Utf8.encodedLength("test"));
        
        // Test characters beyond ASCII
        assertEquals(5, Utf8.encodedLength("日本語"));
        assertEquals(6, Utf8.encodedLength("سلام"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthIllegalArgumentException() {
        Utf8.encodedLength("Some very long string that exceeds the maximum int value");
    }
    
    @Test
    public void testIsWellFormed() {
        // Test valid well-formed bytes
        byte[] bytes1 = "test".getBytes();
        assertTrue(Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = {'\u00E6', '\u0074', '\u0074', '\u0065', '\u0073', '\u0074'};
        assertTrue(Utf8.isWellFormed(bytes2));
        
        // Test invalid well-formed bytes
        byte[] bytes3 = {(byte) 0x80};
        assertFalse(Utf8.isWellFormed(bytes3));
        
        byte[] bytes4 = {(byte) 0xE6, (byte) 0xFF};
        assertFalse(Utf8.isWellFormed(bytes4));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test valid well-formed bytes with offset and length
        byte[] bytes1 = {'t', 'e', 's', 't', 't', 'e', 's', 't'};
        assertTrue(Utf8.isWellFormed(bytes1, 1, 6));
        
        byte[] bytes2 = {(byte) 0xE6, (byte) 0x74, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74};
        assertTrue(Utf8.isWellFormed(bytes2, 0, 6));
        
        // Test invalid well-formed bytes with offset and length
        byte[] bytes3 = {(byte) 0xE6, (byte) 0xFF};
        assertFalse(Utf8.isWellFormed(bytes3, 0, 2));
        
        byte[] bytes4 = {(byte) 0xE6, (byte) 0x74, (byte) 0x74, (byte) 0xE6, (byte) 0x73, (byte) 0x74};
        assertFalse(Utf8.isWellFormed(bytes4, 0, 6));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Test valid well-formed slow path bytes
        byte[] bytes1 = {0};
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, 1));
        
        byte[] bytes2 = {(byte) 0xE6, (byte) 0x74, (byte) 0xE6, (byte) 0x73, (byte) 0x74};
        assertTrue(Utf8.isWellFormedSlowPath(bytes2, 0, 5));
        
        // Test invalid well-formed slow path bytes
        byte[] bytes3 = {(byte) 0xFF};
        assertFalse(Utf8.isWellFormedSlowPath(bytes3, 0, 1));
        
        byte[] bytes4 = {(byte) 0xE6};
        assertFalse(Utf8.isWellFormedSlowPath(bytes4, 0, 1));
        
        byte[] bytes5 = {(byte) 0xE6, (byte) 0xFF};
        assertFalse(Utf8.isWellFormedSlowPath(bytes5, 0, 2));
    }
}