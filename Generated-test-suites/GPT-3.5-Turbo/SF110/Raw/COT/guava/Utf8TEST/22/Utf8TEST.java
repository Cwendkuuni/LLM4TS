
import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Test case for a basic functionality
        String sequence = "hello";
        int expectedLength = 5;
        int actualLength = Utf8.encodedLength(sequence);
        Assert.assertEquals(expectedLength, actualLength);
        
        // Test case for edge case
        sequence = "\uD83D\uDE00"; // emoji
        expectedLength = 4;
        actualLength = Utf8.encodedLength(sequence);
        Assert.assertEquals(expectedLength, actualLength);
        
        // Test case for exception handling
        sequence = "hello\uDE00"; // unpaired surrogate
        try {
            Utf8.encodedLength(sequence);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Test case for a basic functionality
        byte[] bytes = { 0, 1, -1, 2, -2, 3, -3 };
        boolean expectedWellFormed = true;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expectedWellFormed, actualWellFormed);
        
        // Test case for edge case
        bytes = new byte[] { -61, -87 }; // UTF-8 encoding of 'ç'
        expectedWellFormed = true;
        actualWellFormed = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expectedWellFormed, actualWellFormed);
        
        // Test case for exception handling
        bytes = new byte[] { -61 }; // invalid UTF-8 encoding
        expectedWellFormed = false;
        actualWellFormed = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expectedWellFormed, actualWellFormed);
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test case for a basic functionality
        byte[] bytes = { 0, 1, -1, 2, -2, 3, -3 };
        int off = 1;
        int len = 5;
        boolean expectedWellFormed = true;
        boolean actualWellFormed = Utf8.isWellFormed(bytes, off, len);
        Assert.assertEquals(expectedWellFormed, actualWellFormed);
        
        // Test case for edge case
        bytes = new byte[] { -61, -87 }; // UTF-8 encoding of 'ç'
        off = 0;
        len = 2;
        expectedWellFormed = true;
        actualWellFormed = Utf8.isWellFormed(bytes, off, len);
        Assert.assertEquals(expectedWellFormed, actualWellFormed);
        
        // Test case for exception handling
        bytes = new byte[] { -61 }; // invalid UTF-8 encoding
        off = 0;
        len = 1;
        expectedWellFormed = false;
        actualWellFormed = Utf8.isWellFormed(bytes, off, len);
        Assert.assertEquals(expectedWellFormed, actualWellFormed);
    }
}
