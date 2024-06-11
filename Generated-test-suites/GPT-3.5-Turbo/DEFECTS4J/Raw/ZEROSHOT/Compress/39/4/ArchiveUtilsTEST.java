import java.nio.charset.Charset;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("dir1", true, 100);
        ArchiveEntry entry2 = new ArchiveEntry("file1", false, 2000);

        String result1 = ArchiveUtils.toString(entry1);
        String result2 = ArchiveUtils.toString(entry2);

        assertEquals("d     100 dir1", result1);
        assertEquals("-    2000 file1", result2);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "TEST";
        byte[] buffer1 = {'T', 'E', 'S', 'T'};
        byte[] buffer2 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.matchAsciiBuffer(expected, buffer1);
        boolean result2 = ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, 4);

        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "TEST";
        byte[] expected = {'T', 'E', 'S', 'T'};

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {'T', 'E', 'S', 'T'};
        String expected = "TEST";

        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {'T', 'E', 'S', 'T', '!'};
        String expected = "EST";

        String result = ArchiveUtils.toAsciiString(inputBytes, 1, 3);

        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {'T', 'E', 'S', 'T'};
        byte[] buffer2 = {'T', 'E', 'S', 'T'};
        byte[] buffer3 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.isEqual(buffer1, buffer2);
        boolean result2 = ArchiveUtils.isEqual(buffer1, buffer3);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = {'T', 'E', 'S', 'T'};
        byte[] buffer2 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 4);
        boolean result2 = ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 4);

        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {'T', 'E', 'S', 'T', 0};
        byte[] buffer2 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true);
        boolean result2 = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 4, true);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLengths() {
        byte[] buffer1 = {'T', 'E', 'S', 'T', 0};
        byte[] buffer2 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 4, true);
        boolean result2 = ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 5, true);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLengthsAndOffset() {
        byte[] buffer1 = {0, 0, 'T', 'E', 'S', 'T', 0, 0};
        byte[] buffer2 = {0, 0, 'T', 'E', 'S', 'T', '!', 0};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 2, 5, buffer2, 2, 5, true);
        boolean result2 = ArchiveUtils.isEqual(buffer1, 2, 5, buffer2, 2, 6, true);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndEqualLengths() {
        byte[] buffer1 = {'T', 'E', 'S', 'T', 0};
        byte[] buffer2 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true);

        assertTrue(result1);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndEqualLengthsAndOffset() {
        byte[] buffer1 = {0, 0, 'T', 'E', 'S', 'T', 0};
        byte[] buffer2 = {0, 0, 'T', 'E', 'S', 'T', '!', 0};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 2, 6, buffer2, 2, 6, true);

        assertTrue(result1);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndEqualLengthsAndDifferentOffset() {
        byte[] buffer1 = {0, 0, 'T', 'E', 'S', 'T', 0};
        byte[] buffer2 = {0, 0, 0, 'T', 'E', 'S', 'T', '!', 0};

        boolean result1 = ArchiveUtils.isEqual(buffer1, 2, 6, buffer2, 3, 6, true);

        assertTrue(result1);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {'T', 'E', 'S', 'T'};
        byte[] buffer2 = {'T', 'E', 'S', 'T', '!'};

        boolean result1 = ArchiveUtils.isEqual(buffer1, buffer2, false);
        boolean result2 = ArchiveUtils.isEqual(buffer1, buffer2, true);

        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array1 = {0, 0, 0, 0, 0};
        byte[] array2 = {0, 0, 0, 1, 0};

        boolean result1 = ArchiveUtils.isArrayZero(array1, 5);
        boolean result2 = ArchiveUtils.isArrayZero(array2, 5);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testSanitize() {
        String input = "abc\u0000def\u007fghi\u00ffjkl";
        String expected = "abc?def?ghi?jkl";

        String result = ArchiveUtils.sanitize(input);

        assertEquals(expected, result);
    }
}