
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(6, Utf8.encodedLength("Hello!"));
        assertEquals(9, Utf8.encodedLength("123456789"));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(2, Utf8.encodedLengthGeneral("∫", 0));
        assertEquals(2, Utf8.encodedLengthGeneral("ˣ", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("𐐷", 0));
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] validBytes = {72, 101, 108, 108, 111};
        byte[] invalidBytes = {72, -1, 108, 108, 111};
        
        assertTrue(Utf8.isWellFormed(validBytes));
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] validBytes = {72, 101, 108, 108, 111};
        byte[] invalidBytes = {72, -1, 108, 108, 111};
        
        assertTrue(Utf8.isWellFormed(validBytes, 0, validBytes.length));
        assertFalse(Utf8.isWellFormed(invalidBytes, 0, invalidBytes.length));
    }
    
}
