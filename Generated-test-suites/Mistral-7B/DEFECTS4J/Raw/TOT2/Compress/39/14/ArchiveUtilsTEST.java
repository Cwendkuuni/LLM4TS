import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {
    // Test toString method
    @Test
    public void testToStringMethod() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);
    }

    // Test matchAsciiBuffer method with basic usage
    @Test
    public void testMatchAsciiBufferBasicUsage() {
        String expected = "expected string";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    // Test matchAsciiBuffer method with edge cases
    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferEdgeCases() {
        String expected = "expected string";
        byte[] buffer = new byte[10]; // buffer length is not equal to expected string length
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    // Test isEqual method with basic usage
    @Test
    public void testIsEqualMethodBasicUsage() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'a', 'b', 'c'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    // Test isEqual method with edge cases
    @Test
    public void testIsEqualMethodEdgeCases() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'a', 'b', 'd'};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    // Test sanitize method with basic usage
    @Test
    public void testSanitizeMethodBasicUsage() {
        String s = "test string";
        String result = ArchiveUtils.sanitize(s);
        String expected = "test string"; // No non-printable characters in the original string
        assertEquals(expected, result);
    }

    // Test sanitize method with edge cases
    @Test
    public void testSanitizeMethodEdgeCases() {
        String s = "test\u0001string"; // Contains a non-printable character
        String result = ArchiveUtils.sanitize(s);
        String expected = "test??string"; // Replaces non-printable character with question mark
        assertEquals(expected, result);
    }
}