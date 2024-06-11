import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry() {
            public boolean isDirectory() { return false; }
            public long getSize() { return 2000; }
            public String getName() { return "main.c"; }
        };
        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithOffsetAndLength() {
        String expected = "test";
        byte[] buffer = ("abc" + expected + "def").getBytes(CharsetNames.US_ASCII);
        int offset = 3;
        int length = expected.length();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes(CharsetNames.US_ASCII);
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes(CharsetNames.US_ASCII);
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = ("abc" + "test" + "def").getBytes(CharsetNames.US_ASCII);
        int offset = 3;
        int length = 4;
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = ("abc" + "test" + "def").getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = ("xyz" + "test" + "uvw").getBytes(CharsetNames.US_ASCII);
        int offset1 = 3;
        int length1 = 4;
        int offset2 = 3;
        int length2 = 4;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = "test\0\0".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test\0\0".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[4];
        assertTrue(ArchiveUtils.isArrayZero(a, 4));
    }

    @Test
    public void testIsArrayZeroWithNonZeroBytes() {
        byte[] a = {0, 0, 1, 0};
        assertFalse(ArchiveUtils.isArrayZero(a, 4));
    }

    @Test
    public void testSanitize() {
        String s = "abc";
        String expected = "abc";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }

    @Test
    public void testSanitizeWithNonPrintableCharacters() {
        String s = "abc\0\1\2";
        String expected = "abc???";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }

    @Test
    public void testSanitizeWithTooLongString() {
        String s = "";
        for (int i = 0; i < 300; i++) {
            s += "a";
        }
        String expected = "";
        for (int i = 0; i < 255; i++) {
            expected += "a";
        }
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }

}