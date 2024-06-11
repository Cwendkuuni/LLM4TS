import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_SimpleAsciiString() {
        String sequence = "Hello";
        int expectedLength = 5;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLength_UnicodeString() {
        String sequence = "€";
        int expectedLength = 3;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_Exception() {
        String sequence = new String(new char[Integer.MAX_VALUE]);
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_ValidBytes() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        boolean expectedResult = true;
        boolean actualResult = Utf8.isWellFormed(bytes);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsWellFormed_InvalidBytes() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0x00};
        boolean expectedResult = false;
        boolean actualResult = Utf8.isWellFormed(bytes);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsWellFormed_ValidBytesWithOffsetAndLength() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        int offset = 2;
        int length = 2;
        boolean expectedResult = true;
        boolean actualResult = Utf8.isWellFormed(bytes, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsWellFormed_InvalidBytesWithOffsetAndLength() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0x00, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        int offset = 2;
        int length = 2;
        boolean expectedResult = false;
        boolean actualResult = Utf8.isWellFormed(bytes, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsWellFormedSlowPath_ValidBytes() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        int offset = 0;
        int end = 3;
        boolean expectedResult = true;
        boolean actualResult = Utf8.isWellFormedSlowPath(bytes, offset, end);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsWellFormedSlowPath_InvalidBytes() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0x00};
        int offset = 0;
        int end = 3;
        boolean expectedResult = false;
        boolean actualResult = Utf8.isWellFormedSlowPath(bytes, offset, end);
        assertEquals(expectedResult, actualResult);
    }
}