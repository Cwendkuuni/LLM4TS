import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");
        
        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = {'t', 'e', 's', 't'};
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {'t', 'e', 's', 't'};
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiString_offset_length() {
        byte[] inputBytes = {'t', 'e', 's', 't'};
        int offset = 1;
        int length = 2;
        String expected = "es";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {'t', 'e', 's', 't'};
        byte[] buffer2 = {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls() {
        byte[] buffer1 = {'t', 'e', 's', 't', '\0'};
        byte[] buffer2 = {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {'t', 'e', 's', 't', '\0'};
        byte[] buffer2 = {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = {0, 0, 0, 0};
        int size = 4;
        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        String s = "te\bst";
        String expected = "te?st";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }
}