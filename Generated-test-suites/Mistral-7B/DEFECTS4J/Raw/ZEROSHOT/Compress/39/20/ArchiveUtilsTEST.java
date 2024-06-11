import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "-       0 test.txt";
        assertEquals(expected, result);

        ArchiveEntry dirEntry = new ArchiveEntry("test_dir");
        dirEntry.setDirectory(true);
        result = ArchiveUtils.toString(dirEntry);
        expected = "d       0 test_dir/";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);

        byte[] buffer1 = Arrays.copyOfRange(buffer, 0, 3);
        byte[] buffer2 = Arrays.copyOfRange(buffer, 3, buffer.length);

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, false));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = toAsciiBytes("test123");
        byte[] buffer3 = toAsciiBytes("test456");

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[10];
        byte[] buffer2 = new byte[10];
        byte[] buffer3 = new byte[5];

        Arrays.fill(buffer1, 0);
        Arrays.fill(buffer2, 0);
        Arrays.fill(buffer3, 0);

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testSanitize() {
        String input = "test\ninvalid\r\fchars\012\015";
        String result = ArchiveUtils.sanitize(input);
        String expected = "test????????????invalid????????????chars";
        assertEquals(expected, result);
    }
}