
import org.junit.Test;

import static org.junit.Assert.*;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        String sequence = "Hello";
        int expected = 5;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEncodedLength_EmptyString() {
        String sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_InvalidUtf8Length() {
        String sequence = "𐐷𤭢";
        Utf8.encodedLength(sequence);
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] bytes = {72, 101, 108, 108, 111};
        assertTrue(Utf8.isWellFormed(bytes));
    }
    
    @Test
    public void testIsWellFormed_EmptyArray() {
        byte[] bytes = {};
        assertTrue(Utf8.isWellFormed(bytes));
    }
    
    @Test
    public void testIsWellFormed_InvalidByteArray() {
        byte[] bytes = {-65, -66};
        assertFalse(Utf8.isWellFormed(bytes));
    }
    
    @Test
    public void testIsWellFormed_InvalidByteArrayWithLength() {
        byte[] bytes = {-65, -66};
        int off = 0;
        int len = 2;
        assertFalse(Utf8.isWellFormed(bytes, off, len));
    }
}