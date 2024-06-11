import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test input with no special characters
        String input1 = "Hello World";
        int expected1 = 11;
        int result1 = Utf8.encodedLength(input1);
        assertEquals(expected1, result1);

        // Test input with special characters
        String input2 = "Héllo wørld!";
        int expected2 = 14;
        int result2 = Utf8.encodedLength(input2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test input with no special characters
        String input1 = "Hello World";
        int start1 = 0;
        int expected1 = 11;
        int result1 = Utf8.encodedLengthGeneral(input1, start1);
        assertEquals(expected1, result1);

        // Test input with special characters
        String input2 = "Héllo wørld!";
        int start2 = 0;
        int expected2 = 14;
        int result2 = Utf8.encodedLengthGeneral(input2, start2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed input
        byte[] input1 = "Hello World".getBytes();
        boolean result1 = Utf8.isWellFormed(input1);
        assertTrue(result1);

        // Test malformed input
        byte[] input2 = {(byte) 0x80, (byte) 0xFF};
        boolean result2 = Utf8.isWellFormed(input2);
        assertFalse(result2);
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test well-formed input
        byte[] input1 = "Hello World".getBytes();
        int off1 = 0;
        int len1 = input1.length;
        boolean result1 = Utf8.isWellFormed(input1, off1, len1);
        assertTrue(result1);

        // Test malformed input
        byte[] input2 = {(byte) 0x80, (byte) 0xFF};
        int off2 = 0;
        int len2 = input2.length;
        boolean result2 = Utf8.isWellFormed(input2, off2, len2);
        assertFalse(result2);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test well-formed input
        byte[] input1 = "Hello World".getBytes();
        int off1 = 0;
        int end1 = input1.length;
        boolean result1 = Utf8.isWellFormedSlowPath(input1, off1, end1);
        assertTrue(result1);

        // Test malformed input
        byte[] input2 = {(byte) 0x80, (byte) 0xFF};
        int off2 = 0;
        int end2 = input2.length;
        boolean result2 = Utf8.isWellFormedSlowPath(input2, off2, end2);
        assertFalse(result2);
    }
}