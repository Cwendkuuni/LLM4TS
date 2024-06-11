import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(6, Utf8.encodedLength("中文"));
        assertEquals(9, Utf8.encodedLength("😀😀😀"));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));
        assertEquals(0, Utf8.encodedLengthGeneral("abc", 3));
        assertEquals(2, Utf8.encodedLengthGeneral("中文", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("😀😀😀", 0));
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = new byte[]{};
        assertTrue(Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = new byte[]{65, 66, 67};
        assertTrue(Utf8.isWellFormed(bytes2));
        
        byte[] bytes3 = new byte[]{-27, -91, -67};
        assertTrue(Utf8.isWellFormed(bytes3));
        
        byte[] bytes4 = new byte[]{-61, -128};
        assertFalse(Utf8.isWellFormed(bytes4));
        
        byte[] bytes5 = new byte[]{-19, -65, -95};
        assertFalse(Utf8.isWellFormed(bytes5));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = new byte[]{};
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, 0));
        
        byte[] bytes2 = new byte[]{-61, -128};
        assertTrue(Utf8.isWellFormedSlowPath(bytes2, 0, 2));
        
        byte[] bytes3 = new byte[]{-27, -91, -67};
        assertTrue(Utf8.isWellFormedSlowPath(bytes3, 0, 3));
        
        byte[] bytes4 = new byte[]{-19, -65, -95};
        assertFalse(Utf8.isWellFormedSlowPath(bytes4, 0, 3));
    }
}