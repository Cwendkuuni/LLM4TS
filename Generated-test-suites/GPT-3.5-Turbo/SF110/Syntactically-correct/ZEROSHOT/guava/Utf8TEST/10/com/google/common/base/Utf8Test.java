import org.junit.*;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test with empty sequence
        CharSequence sequence = "";
        int expectedLength = 0;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);

        // Test with non-empty sequence
        sequence = "Hello, world!";
        expectedLength = 14;
        actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test with empty sequence
        CharSequence sequence = "";
        int start = 0;
        int expectedLength = 0;
        int actualLength = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(expectedLength, actualLength);

        // Test with non-empty sequence
        sequence = "Hello, world!";
        start = 0;
        expectedLength = 14;
        actualLength = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testIsWellFormed() {
        // Test with empty byte array
        byte[] bytes = new byte[0];
        boolean expectedValue = true;
        boolean actualValue = Utf8.isWellFormed(bytes);
        assertEquals(expectedValue, actualValue);

        // Test with well-formed byte array
        bytes = new byte[]{72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33};
        expectedValue = true;
        actualValue = Utf8.isWellFormed(bytes);
        assertEquals(expectedValue, actualValue);

        // Test with malformed byte array
        bytes = new byte[]{72, 101, 108, 108, 111, -50, -50, 32, 119, 111, 114, 108, 100, 33};
        expectedValue = false;
        actualValue = Utf8.isWellFormed(bytes);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test with empty byte array
        byte[] bytes = new byte[0];
        int off = 0;
        int len = 0;
        boolean expectedValue = true;
        boolean actualValue = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expectedValue, actualValue);

        // Test with well-formed byte array
        bytes = new byte[]{72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33};
        off = 0;
        len = bytes.length;
        expectedValue = true;
        actualValue = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expectedValue, actualValue);

        // Test with malformed byte array
        bytes = new byte[]{72, 101, 108, 108, 111, -50, -50, 32, 119, 111, 114, 108, 100, 33};
        off = 0;
        len = bytes.length;
        expectedValue = false;
        actualValue = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test with well-formed byte array
        byte[] bytes = new byte[]{72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33};
        int off = 0;
        int end = bytes.length;
        boolean expectedValue = true;
        boolean actualValue = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expectedValue, actualValue);

        // Test with malformed byte array
        bytes = new byte[]{72, 101, 108, 108, 111, -50, -50, 32, 119, 111, 114, 108, 100, 33};
        off = 0;
        end = bytes.length;
        expectedValue = false;
        actualValue = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expectedValue, actualValue);
    }

}

