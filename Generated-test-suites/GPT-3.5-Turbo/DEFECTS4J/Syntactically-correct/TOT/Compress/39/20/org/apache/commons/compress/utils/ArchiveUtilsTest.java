import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarEntry;
import org.junit.Before;
import org.junit.Test;

public class ArchiveUtilsTest {

    private ArchiveEntry entry;

    @Before
    public void setUp() {
        entry = new JarEntry();
    }

    @Test
    public void testToString_directoryEntry() {
        entry.setDirectory(true);
        entry.setSize(100);

        assertEquals("d     100 null", ArchiveUtils.toString(entry));
    }

    @Test
    public void testToString_fileEntry() {
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer_match() {
        String expected = "test";
        byte[] buffer = expected.getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_mismatch() {
        String expected = "test";
        byte[] buffer = "tesT".getBytes();

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";

        byte[] expected = inputString.getBytes();

        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();

        String expected = new String(inputBytes);

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiString_withOffsetAndLength() {
        byte[] inputBytes = "testings".getBytes();
        int offset = 2;
        int length = 6;

        String expected = new String(inputBytes, offset, length);

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual_equalBuffers() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_differentBuffers() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "tesT".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test\0".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull_equalBuffers() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }

    @Test
    public void testIsEqualWithNull_unequalBuffers() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "tesT".getBytes();

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }

    @Test
    public void testIsArrayZero_allZeros() {
        byte[] array = new byte[4];

        assertTrue(ArchiveUtils.isArrayZero(array, 4));
    }

    @Test
    public void testIsArrayZero_notAllZeros() {
        byte[] array = "test".getBytes();

        assertFalse(ArchiveUtils.isArrayZero(array, 4));
    }

    @Test
    public void testSanitize_nonPrintableCharacters() {
        String inputString = "non-printable\u0007characters\u001F";
        String expected = "non-printable?characters?";

        assertEquals(expected, ArchiveUtils.sanitize(inputString));
    }

    @Test
    public void testSanitize_printableCharactersOnly() {
        String inputString = "printable characters";
        String expected = inputString;

        assertEquals(expected, ArchiveUtils.sanitize(inputString));
    }

    @Test
    public void testSanitize_existingSpecialCharacters() {
        String inputString = "special\ncharacters\texist";
        String expected = inputString;

        assertEquals(expected, ArchiveUtils.sanitize(inputString));
    }
}