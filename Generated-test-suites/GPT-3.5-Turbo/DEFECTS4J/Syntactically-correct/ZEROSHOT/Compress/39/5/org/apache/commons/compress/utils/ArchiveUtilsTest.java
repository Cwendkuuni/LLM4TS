import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");

        String result = ArchiveUtils.toString(entry);
        assertEquals("d     100 testfiles", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = {'t', 'e', 's', 't'};

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = {'t', 'e', 's', 't'};

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {'t', 'e', 's', 't'};
        String expected = "test";

        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {'t', 'e', 's', 't'};
        byte[] buffer2 = {'t', 'e', 's', 't'};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {'t', 'e', 's', 't', 0};
        byte[] buffer2 = {'t', 'e', 's', 't'};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "test\0file";
        String expected = "test?file";

        String result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
    }

}