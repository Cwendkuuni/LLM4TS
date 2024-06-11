
import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        CharSequence sequence = "Hello, World!";
        int expectedResult = 13;
        
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testEncodedLength_emptySequence() {
        CharSequence sequence = "";
        int expectedResult = 0;
        
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testEncodedLength_nonAsciiCharacters() {
        CharSequence sequence = "こんにちは";
        int expectedResult = 15;
        
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expectedResult, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_utf8LengthGreaterThanInt() {
        CharSequence sequence = "✌️";
        
        Utf8.encodedLength(sequence);
    }
    
    @Test
    public void testIsWellFormed_typicalCase() {
        byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33 };
        boolean expectedResult = true;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_emptyArray() {
        byte[] bytes = new byte[0];
        boolean expectedResult = true;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_invalidLeadingByte() {
        byte[] bytes = new byte[] { -96, 65, 98, 99, 100 };
        boolean expectedResult = false;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_invalidContinuationByte() {
        byte[] bytes = new byte[] { 97, -1, 98, 99, 100 };
        boolean expectedResult = false;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_invalidContinuationByte_lastByte() {
        byte[] bytes = new byte[] { 97, 98, 99, -1 };
        boolean expectedResult = false;
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath_typicalCase() {
        byte[] bytes = new byte[] { -61, -99 }; // "こんにちは" in UTF-8
        int off = 0;
        int len = 2;
        boolean expectedResult = true;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath_emptyArray() {
        byte[] bytes = new byte[0];
        int off = 0;
        int len = 0;
        boolean expectedResult = true;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath_invalidLeadingByte() {
        byte[] bytes = new byte[] { -96, 65, 98, 99, 100 };
        int off = 0;
        int len = 5;
        boolean expectedResult = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath_invalidContinuationByte() {
        byte[] bytes = new byte[] { 97, -1, 98, 99, 100 };
        int off = 0;
        int len = 5;
        boolean expectedResult = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWellFormed_slowPath_invalidContinuationByte_lastByte() {
        byte[] bytes = new byte[] { 97, 98, 99, -1 };
        int off = 0;
        int len = 4;
        boolean expectedResult = false;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertEquals(expectedResult, result);
    }

}
