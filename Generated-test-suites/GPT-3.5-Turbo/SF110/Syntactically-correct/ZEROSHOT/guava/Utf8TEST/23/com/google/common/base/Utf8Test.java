import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Utf8;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test ASCII characters
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(3, Utf8.encodedLength("abc"));

        // Test characters with encoding length > 1
        assertEquals(2, Utf8.encodedLength("ü"));

        // Test characters with encoding length of 3
        assertEquals(3, Utf8.encodedLength("€"));

        // Test characters with encoding length of 4
        assertEquals(4, Utf8.encodedLength("𐍈"));

        // Test surrogate characters
        try {
            Utf8.encodedLength("𐍈𐍈");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 2", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Test empty byte array
        assertTrue(Utf8.isWellFormed(new byte[] {}));

        // Test well-formed byte array
        assertTrue(Utf8.isWellFormed(new byte[] { 0x41, 0x42, 0x43 }));

        // Test byte array with invalid encoding
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xC3, (byte) 0x28 }));

        // Test byte array with well-formed and invalid encoding
        assertFalse(Utf8.isWellFormed(new byte[] { 0x41, (byte) 0xC3, (byte) 0x28 }));

        // Test byte array with surrogate characters
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xED, (byte) 0xA0, (byte) 0x80 }));
    }
}