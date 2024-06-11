import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("testfile.txt", 100, false);
        String result = ArchiveUtils.toString(entry);
        assertEquals("-    100 testfile.txt", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello";
        byte[] buffer = "Hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(new byte[]{72, 101, 108, 108, 111}, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {72, 101, 108, 108, 111};
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals("Hello", result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, a.length));
    }

    @Test
    public void testSanitize() {
        String s = "Hello\tWorld";
        String result = ArchiveUtils.sanitize(s);
        assertEquals("Hello?World", result);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}