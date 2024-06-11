import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry(true, 2000, "main.c");
        ArchiveEntry entry2 = new ArchiveEntry(false, 100, "testfiles");
        
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test string";
        byte[] buffer = expected.getBytes(CharsetNames.US_ASCII);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length + 1));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test string";
        byte[] expected = inputString.getBytes(CharsetNames.US_ASCII);

        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test string".getBytes(CharsetNames.US_ASCII);
        String expected = new String(inputBytes, CharsetNames.US_ASCII);

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        buffer1 = Arrays.copyOf(buffer1, buffer1.length + 1);
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];
        Arrays.fill(array, (byte) 0);

        assertTrue(ArchiveUtils.isArrayZero(array, 5));

        array[2] = 1;
        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test\tstring\n123";
        String expected = "test?string?123";

        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}