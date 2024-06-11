import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test when sequence is empty
        assertEquals(0, Utf8.encodedLength(""));

        // Test when sequence contains only ASCII characters
        assertEquals(5, Utf8.encodedLength("hello"));

        // Test when sequence contains non-ASCII characters
        assertEquals(11, Utf8.encodedLength("你好世界"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_MaxLengthExceeded() {
        // Test when the encoded length exceeds the maximum int value
        Utf8.encodedLength("hello" + new String(new char[Integer.MAX_VALUE]));
    }

    @Test
    public void testIsWellFormed() {
        // Test when byte array is empty
        assertTrue(Utf8.isWellFormed(new byte[0]));

        // Test when byte array contains only well-formed UTF-8 characters
        assertTrue(Utf8.isWellFormed(new byte[]{104, 101, 108, 108, 111}));

        // Test when byte array contains ill-formed UTF-8 characters
        assertFalse(Utf8.isWellFormed(new byte[]{104, -100, 108, 108, 111}));
    }

    @Test
    public void testIsWellFormedOffLen() {
        // Test when byte array is empty with offset and length
        assertTrue(Utf8.isWellFormed(new byte[0], 0, 0));

        // Test when byte array contains only well-formed UTF-8 characters with offset and length
        assertTrue(Utf8.isWellFormed(new byte[]{104, 101, 108, 108, 111}, 0, 5));

        // Test when byte array contains ill-formed UTF-8 characters with offset and length
        assertFalse(Utf8.isWellFormed(new byte[]{104, -100, 108, 108, 111}, 0, 5));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test when byte array is empty with offset and end
        assertTrue(Utf8.isWellFormed(new byte[0], 0, 0));

        // Test when byte array contains only well-formed UTF-8 characters with offset and end
        assertTrue(Utf8.isWellFormed(new byte[]{104, 101, 108, 108, 111}, 0, 5));

        // Test when byte array contains ill-formed UTF-8 characters with offset and end
        assertFalse(Utf8.isWellFormed(new byte[]{104, -100, 108, 108, 111}, 0, 5));
    }
    
}
