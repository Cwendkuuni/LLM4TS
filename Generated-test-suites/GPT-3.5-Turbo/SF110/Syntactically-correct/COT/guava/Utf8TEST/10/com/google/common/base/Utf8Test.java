
import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(4, Utf8.encodedLength("😀"));
    }
    
    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0x41, (byte)0x42}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0x41, (byte)0xC3, (byte)0xA9}));
    }
}
