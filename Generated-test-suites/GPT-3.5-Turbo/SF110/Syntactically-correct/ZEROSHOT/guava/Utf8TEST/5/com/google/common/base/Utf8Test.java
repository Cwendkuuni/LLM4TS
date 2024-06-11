import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    @Test
    public void testEncodedLength_oneByteCharacters_returnsLength() {
        assertEquals(4, Utf8.encodedLength("test"));
        assertEquals(7, Utf8.encodedLength("utf8日本語"));
    }
    
    @Test
    public void testEncodedLength_invalidCharacter_throwsIllegalArgumentException() {
        try {
            Utf8.encodedLength("unpaired\uFFFF surrogate");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 8", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed_validBytes_returnsTrue() {
        assertTrue(Utf8.isWellFormed(new byte[]{0x74, 0x65, 0x73, 0x74}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x75, 0x74, 0x66, 0x38, -29, -127, -94, -27, -91, -67, -27, -113, -98}));
    }
    
    @Test
    public void testIsWellFormed_invalidBytes_returnsFalse() {
        assertFalse(Utf8.isWellFormed(new byte[]{0x74, 0x65, -70, 0x74}));
        assertFalse(Utf8.isWellFormed(new byte[]{0x75, 0x74, 0x66, 0x38, -29, -127}));
    }
}