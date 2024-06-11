import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("testFile.txt");
        entry.setDirectory(false);
        entry.setSize(100);

        String expected = "-     100 testFile.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));

        // Edge case
        entry.setName("");
        entry.setDirectory(true);
        entry.setSize(0);

        expected = "d       0 ";
        assertEquals(expected, ArchiveUtils.toString(entry));

        // Error scenario - testing null entry
        assertNull(ArchiveUtils.toString(null));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "test string";
        byte[] buffer = expected.getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case
        expected = "test string";
        buffer = "different string".getBytes();

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Error scenario - testing null buffer
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, null));
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        String inputString = "test string";

        byte[] expected = inputString.getBytes();
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));

        // Error scenario - testing null inputString
        assertThrows(NullPointerException.class, () -> ArchiveUtils.toAsciiBytes(null));
    }

    // Add more test methods for each public method in ArchiveUtils class

}