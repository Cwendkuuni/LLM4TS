import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("example.txt", false, 100);
        String expectedResult = "-    100 example.txt";
        assertEquals(expectedResult, ArchiveUtils.toString(entry));

        // Edge case
        entry = new ArchiveEntry("", false, 0);
        expectedResult = "-      0 ";
        assertEquals(expectedResult, ArchiveUtils.toString(entry));
        
        // Error scenario (null entry)
        try {
            ArchiveUtils.toString(null);
            fail("An exception should have been thrown for null entry");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "hello";
        byte[] buffer = "hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case
        expected = "world";
        buffer = "world".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Error scenario (mismatching buffer)
        expected = "hello";
        buffer = "HELLO".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        String inputString = "test";
        byte[] expectedBytes = {116, 101, 115, 116}; // ascii values for 't', 'e', 's', 't'
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));

        // Edge case
        inputString = "";
        expectedBytes = new byte[0];
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }

    // Continue with similar test cases for remaining methods...

}