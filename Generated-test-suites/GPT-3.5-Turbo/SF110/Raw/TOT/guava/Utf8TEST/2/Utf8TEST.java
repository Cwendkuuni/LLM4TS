import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        CharSequence sequence = "Hello World";
        int expected = 11;
        
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodedLength_emptyString() {
        CharSequence sequence = "";
        int expected = 0;
        
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncodedLength_specialChars() {
        CharSequence sequence = "❤️";
        int expected = 4;
        
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_longString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1_000_000; i++) {
            sb.append("a");
        }
        CharSequence sequence = sb.toString();
        
        Utf8.encodedLength(sequence);
    }
    
    @Test
    public void testIsWellFormed_typicalCase() {
        byte[] bytes = {(byte) 72, (byte) 101, (byte) 108, (byte) 108, (byte) 111};
        boolean expected = true;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormed_emptyArray() {
        byte[] bytes = {};
        boolean expected = true;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormed_invalidBytes() {
        byte[] bytes = {(byte) -128, (byte) -127, (byte) 0};
        boolean expected = false;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormed_boundaryBytes() {
        byte[] bytes = {(byte) -127, (byte) -1, (byte) 0};
        boolean expected = true;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath() {
        byte[] bytes = {(byte) -30, (byte) -128, (byte) -128, (byte) -1};
        boolean expected = false;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath_boundaryBytes() {
        byte[] bytes = {(byte) -30, (byte) -128, (byte) -128, (byte) 0};
        boolean expected = false;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_typicalCase() {
        byte[] bytes = {(byte) -30, (byte) -128, (byte) -128};
        int off = 0;
        int len = 3;
        boolean expected = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_emptyArray() {
        byte[] bytes = {};
        int off = 0;
        int len = 0;
        boolean expected = true;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_invalidBytes() {
        byte[] bytes = {(byte) -128, (byte) -127, (byte) 0};
        int off = 0;
        int len = 3;
        boolean expected = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_boundaryBytes() {
        byte[] bytes = {(byte) -127, (byte) -1, (byte) 0};
        int off = 0;
        int len = 3;
        boolean expected = true;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_slowPath() {
        byte[] bytes = {(byte) -30, (byte) -128, (byte) -128, (byte) -1};
        int off = 0;
        int len = 4;
        boolean expected = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_slowPath_boundaryBytes() {
        byte[] bytes = {(byte) -30, (byte) -128, (byte) -128, (byte) 0};
        int off = 0;
        int len = 4;
        boolean expected = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expected, result);
    }
    
}