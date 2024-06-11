import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("testFile", false, 100);
        String expected = "-    100 testFile";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
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
        byte[] expectedBytes = inputString.getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expectedBytes, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expectedString = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expectedString, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testSanitize() {
        String input = "hello\tworld123";
        String expected = "hello?world123";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

}