import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("example.txt", false, 100);
        String expected = "-    100 example.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));
        
        // Edge case
        ArchiveEntry emptyEntry = new ArchiveEntry("", true, 0);
        String expectedEmpty = "d         0 ";
        assertEquals(expectedEmpty, ArchiveUtils.toString(emptyEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "hello";
        byte[] buffer = "hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case
        String emptyString = "";
        byte[] emptyBuffer = new byte[0];
        assertTrue(ArchiveUtils.matchAsciiBuffer(emptyString, emptyBuffer));
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        String inputString = "test";
        byte[] expectedBytes = {(byte) 116, (byte) 101, (byte) 115, (byte) 116};
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));

        // Edge case
        String emptyString = "";
        byte[] emptyBytes = new byte[0];
        assertArrayEquals(emptyBytes, ArchiveUtils.toAsciiBytes(emptyString));
    }

    @Test
    public void testToAsciiString() {
        // Typical use case
        byte[] inputBytes = {(byte) 104, (byte) 101, (byte) 108, (byte) 108, (byte) 111};
        String expectedString = "hello";
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes));

        // Edge case
        byte[] emptyBytes = new byte[0];
        String emptyString = "";
        assertEquals(emptyString, ArchiveUtils.toAsciiString(emptyBytes));
    }

    // Additional tests for other methods in ArchiveUtils class...

}