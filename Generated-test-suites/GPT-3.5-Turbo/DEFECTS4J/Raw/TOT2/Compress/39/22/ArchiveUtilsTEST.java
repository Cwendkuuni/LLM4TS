import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("testfile.txt", false, 100);
        String expected = "-    100 testfile.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));

        // Edge case: Entry is a directory
        ArchiveEntry dirEntry = new ArchiveEntry("directory", true, 0);
        String expectedDir = "d       0 directory";
        assertEquals(expectedDir, ArchiveUtils.toString(dirEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String input = "test";
        byte[] buffer = {116, 101, 115, 116}; // ASCII values for "test"
        assertTrue(ArchiveUtils.matchAsciiBuffer(input, buffer));

        // Edge case: Buffer length is greater than expected string length 
        String input2 = "hello";
        byte[] buffer2 = {104, 101, 108, 108, 111}; // ASCII values for "hello"
        assertFalse(ArchiveUtils.matchAsciiBuffer(input2, buffer2));

        // Exception handling: Unsupported encoding
        try {
            ArchiveUtils.matchAsciiBuffer("test", buffer, 0, 4);
        } catch (RuntimeException e) {
            fail("Should not throw an exception");
        }
    }

    // More test cases for other methods...

}