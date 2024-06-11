import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));

        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");

        expected = "d     100 testfiles";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        expected = "Test";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expectedBytes = inputString.getBytes();

        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expectedString = "test";

        assertEquals(expectedString, ArchiveUtils.convertToAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiString_withOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        String expectedString = "est";

        assertEquals(expectedString, ArchiveUtils.convertToAsciiString(inputBytes, 1, 3));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        buffer2[0] = 'T';
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_withOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "Test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 4));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3));
    }

    @Test
    public void testIsEqual_withIgnoreTrailingNulls() {
        byte[] buffer1 = "test\0\0\0".getBytes();
        byte[] buffer2 = "test\0".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 7, buffer2, 0, 5, true));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 7, buffer2, 0, 4, true));
    }

    @Test
    public void testIsEqual_withIgnoreTrailingNullsWhenLengthsAreEqual() {
        byte[] buffer1 = "test\0\0\0".getBytes();
        byte[] buffer2 = "test\0".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 7, buffer2, 0, 4, true));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 7, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[4];

        assertTrue(ArchiveUtils.isArrayZero(array, 4));

        array[0] = 1;
        assertFalse(ArchiveUtils.isArrayZero(array, 4));
    }

    @Test
    public void testSanitize() {
        String inputString = "test\u0000\u001F";
        String expectedString = "test??";

        assertEquals(expectedString, ArchiveUtils.sanitize(inputString));
    }

}