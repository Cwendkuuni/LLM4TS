// Test Cases for ArchiveUtils

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;

import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    private static ArchiveEntry entry;

    @BeforeClass
    public static void setUp() {
        entry = new ArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(1000);
        entry.setDirectory(false);
    }

    @Test
    public void testToString() {
        String expected = "-    1000 test.txt";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = expected.getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello";
        byte[] expected = inputString.getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        String expected = "hello";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {104, 101, 108, 108, 111};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {104, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer2, 0, 5);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = {0, 0, 0, 0, 0};
        boolean result = ArchiveUtils.isArrayZero(array, 5);
        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String inputString = "hello\nworld";
        String expected = "hello?world";
        String result = ArchiveUtils.sanitize(inputString);
        assertEquals(expected, result);
    }
}

//