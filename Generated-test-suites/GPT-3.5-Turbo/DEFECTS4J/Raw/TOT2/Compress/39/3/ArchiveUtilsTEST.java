import static org.junit.Assert.*;
import org.junit.Test;
import java.io.UnsupportedEncodingException;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("testFile", 100, false);
        String expected = "-     100 testFile";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello";
        byte[] expected = {104, 101, 108, 108, 111};
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        String expected = "hello";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testSanitize() {
        String input = "abc\u0000def";
        String expected = "abc?def";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

}