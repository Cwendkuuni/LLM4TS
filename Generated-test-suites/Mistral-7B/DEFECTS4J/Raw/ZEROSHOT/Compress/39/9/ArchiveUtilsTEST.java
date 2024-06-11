import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);

        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");
        result = ArchiveUtils.toString(directoryEntry);
        expected = "d 100 testfiles";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = toAsciiBytes(expected);

        byte[] testBuffer = new byte[buffer.length];
        System.arraycopy(buffer, 0, testBuffer, 0, buffer.length);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, testBuffer));
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "Hello World";
        byte[] buffer = toAsciiBytes(expected);

        byte[] testBuffer = new byte[buffer.length + 10];
        System.arraycopy(buffer, 0, testBuffer, 5, buffer.length);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, testBuffer, 5, buffer.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualWithTrailingNulls() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', 0};

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length + 1));
    }

    @Test
    public void testIsEqualWithTrailingNullsInBuffer1() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', 0};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o'};

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length + 1, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];

        assertTrue(ArchiveUtils.isArrayZero(array, 5));
        assertFalse(ArchiveUtils.isArrayZero(array, 11));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001file\u001Fname";
        String expected = "test??testfile??name";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

}