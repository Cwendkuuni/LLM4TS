import static org.junit.Assert.*;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("main.c");
        entry.setDirectory(false);
        entry.setSize(2000);

        String expected = "-    2000 main.c";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes();

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        assertTrue(Arrays.equals(expected, result));
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = new String(inputBytes);

        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        int offset = 0;
        int length = inputBytes.length;
        String expected = new String(inputBytes, offset, length);

        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        int offset = 0;
        int length = buffer1.length;

        assertFalse(ArchiveUtils.isEqual(buffer1, offset, length, buffer2, offset, length));
    }

    @Test
    public void testIsEqualIgnoringTrailingNulls() {
        byte[] buffer1 = "test\0".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        int size = 5;

        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        String s = "\0\1\2\3\4\5\6\7\b\t\n\13\f\r\16\17";

        String expected = "??????????????";

        String result = ArchiveUtils.sanitize(s);

        assertEquals(expected, result);
    }

}