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
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = null;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = new byte[]{116, 101, 115, 116};
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{116, 101, 115, 116};
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffset() {
        byte[] inputBytes = new byte[]{116, 101, 115, 116};
        int offset = 1;
        int length = 2;
        String expected = "es";
        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithOffset() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        int offset = 1;
        int length = 2;
        boolean result = ArchiveUtils.isEqual(buffer1, offset, length, buffer2, offset, length);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[]{0, 0, 0};
        int size = 3;
        boolean result = ArchiveUtils.isArrayZero(a, size);
        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "test";
        String expected = "test";
        String result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
    }

}