import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("testEntry", false, 100);
        String expected = "-    100 testEntry";
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
        String expected = "test";
        byte[] buffer = expected.getBytes();
        assertEquals(expected, ArchiveUtils.toAsciiString(buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5]; // Initialized with zeros
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testSanitize() {
        String input = "te\tst\n";
        String expected = "te?st?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

}