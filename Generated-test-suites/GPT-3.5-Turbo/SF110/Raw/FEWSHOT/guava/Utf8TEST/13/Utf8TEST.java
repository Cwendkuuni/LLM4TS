import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Utf8;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(9, Utf8.encodedLength("你好世界"));
        assertEquals(1, Utf8.encodedLength("A"));
        assertEquals(2, Utf8.encodedLength("Ω"));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {65, 66, 67};
        assertTrue(Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = {(byte) 0xE4, (byte) 0xBD, (byte) 0xA0, (byte) 0xE5, (byte) 0xA5, (byte) 0xBD};
        assertTrue(Utf8.isWellFormed(bytes2));
        
        byte[] bytes3 = {(byte) 0xF0, (byte) 0xA0, (byte) 0x9C, (byte) 0x8E};
        assertTrue(Utf8.isWellFormed(bytes3));
        
        byte[] bytes4 = {(byte) 0xC2, (byte) 0xA9};
        assertTrue(Utf8.isWellFormed(bytes4));
    }

    @Test
    public void testIsWellFormed_InvalidSequence() {
        byte[] bytes1 = {(byte) 0xC2, (byte) 0x90};
        assertFalse(Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = {(byte) 0xE0, (byte) 0x90, (byte) 0x80, (byte) 0xC2};
        assertFalse(Utf8.isWellFormed(bytes2));
        
        byte[] bytes3 = {(byte) 0xF0, (byte) 0x90, (byte) 0x80, (byte) 0x80, (byte) 0xC2};
        assertFalse(Utf8.isWellFormed(bytes3));
        
        byte[] bytes4 = {(byte) 0xF0, (byte) 0x90, (byte) 0x80, (byte) 0xC2, (byte) 0xE0};
        assertFalse(Utf8.isWellFormed(bytes4));
    }
}