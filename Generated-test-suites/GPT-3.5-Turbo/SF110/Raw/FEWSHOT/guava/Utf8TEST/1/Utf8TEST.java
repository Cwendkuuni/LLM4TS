import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(4, Utf8.encodedLength("foo"));
        assertEquals(6, Utf8.encodedLength("bar"));
        assertEquals(8, Utf8.encodedLength("baz"));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(10, Utf8.encodedLengthGeneral("foo", 0));
        assertEquals(12, Utf8.encodedLengthGeneral("bar", 1));
        assertEquals(14, Utf8.encodedLengthGeneral("baz", 2));
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = "foo".getBytes();
        byte[] bytes2 = "bar".getBytes();
        byte[] bytes3 = "baz".getBytes();
        
        assertTrue(Utf8.isWellFormed(bytes1));
        assertTrue(Utf8.isWellFormed(bytes2));
        assertTrue(Utf8.isWellFormed(bytes3));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes1 = "foo".getBytes();
        byte[] bytes2 = "bar".getBytes();
        byte[] bytes3 = "baz".getBytes();
        
        assertTrue(Utf8.isWellFormed(bytes1, 0, bytes1.length));
        assertTrue(Utf8.isWellFormed(bytes2, 0, bytes2.length));
        assertTrue(Utf8.isWellFormed(bytes3, 0, bytes3.length));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = "foo".getBytes();
        byte[] bytes2 = "bar".getBytes();
        byte[] bytes3 = "baz".getBytes();
        
        assertFalse(Utf8.isWellFormedSlowPath(bytes1, 0, bytes1.length));
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, 0, bytes2.length));
        assertFalse(Utf8.isWellFormedSlowPath(bytes3, 0, bytes3.length));
    }
}