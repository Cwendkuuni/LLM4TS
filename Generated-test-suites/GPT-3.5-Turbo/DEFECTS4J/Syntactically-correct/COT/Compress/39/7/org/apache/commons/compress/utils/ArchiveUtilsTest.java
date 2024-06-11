import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry() {
            @Override
            public boolean isDirectory() {
                return false;
            }

            @Override
            public long getSize() {
                return 2000;
            }

            @Override
            public String getName() {
                return "main.c";
            }
        };

        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes();
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = { 116, 101, 115, 116 };
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = { 116, 101, 115, 116 };
        int offset = 1;
        int length = 2;
        String expected = "es";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        int offset1 = 0;
        int offset2 = 0;
        int length = 4;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length, buffer2, offset2, length));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = { 116, 101, 115, 116, 0, 0 };
        byte[] buffer2 = { 116, 101, 115, 116 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithNullAndIgnoreTrailingNulls() {
        byte[] buffer1 = { 116, 101, 115, 116, 0, 0 };
        byte[] buffer2 = { 116, 101, 115, 116 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = { 0, 0, 0, 0 };
        int size = 4;
        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        String s = "abc\n\r\tdef";
        String expected = "abc??def";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }

}