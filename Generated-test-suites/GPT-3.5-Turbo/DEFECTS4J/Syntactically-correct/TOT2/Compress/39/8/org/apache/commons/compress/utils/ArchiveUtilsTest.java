import org.junit.Test;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;

import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("test1", 100, false);
        String result1 = ArchiveUtils.toString(entry1);
        assertEquals("-     100 test1", result1);
        
        ArchiveEntry entry2 = new ArchiveEntry("testDir", 200, true);
        String result2 = ArchiveUtils.toString(entry2);
        assertEquals("d     200 testDir", result2);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals("test".getBytes(), result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals("test", result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test1".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5]; // All bytes should be 0
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testSanitize() {
        String input = "tes\u0000t"; // Contains null character
        String result = ArchiveUtils.sanitize(input);
        assertEquals("tes?t", result);
    }

}