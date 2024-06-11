import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToStringForFileEntry() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("file.txt");
        entry.setDirectory(false);
        entry.setSize(1000);
        
        String expected = "-    1000 file.txt";
        String result = ArchiveUtils.toString(entry);
        
        assertEquals(expected, result);
    }

    @Test
    public void testToStringForDirectoryEntry() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("directory");
        entry.setDirectory(true);
        entry.setSize(0);
        
        String expected = "d        0 directory";
        String result = ArchiveUtils.toString(entry);
        
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBufferWithMatch() {
        String expected = "test";
        byte[] buffer = expected.getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithMismatch() {
        String expected = "test";
        byte[] buffer = "different".getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = new String(inputBytes);
        String result = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        int offset = 1;
        int length = 2;
        String expected = new String(inputBytes, offset, length);
        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqualWithTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithoutTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        
        assertFalse(result);
    }

    @Test
    public void testIsEqualForEqualBuffers() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqualForUnequalBuffers() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsForEqualBuffers() {
        byte[] buffer1 = "test\0\0\0\0".getBytes();
        byte[] buffer2 = "test\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsForUnequalBuffers() {
        byte[] buffer1 = "test1\0\0\0\0".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsForEqualLengthBuffers() {
        byte[] buffer1 = "test\0".getBytes();
        byte[] buffer2 = "test".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsForUnequalLengthBuffers() {
        byte[] buffer1 = "test\0".getBytes();
        byte[] buffer2 = "test\0\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullsForEqualBuffers() {
        byte[] buffer1 = "test\0\0".getBytes();
        byte[] buffer2 = "test\0\0".getBytes();
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullsForUnequalBuffers() {
        byte[] buffer1 = "test1\0".getBytes();
        byte[] buffer2 = "test2\0".getBytes();
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertFalse(result);
    }

    @Test
    public void testIsArrayZeroForAllZeroBytes() {
        byte[] a = new byte[] {0, 0, 0, 0};
        boolean result = ArchiveUtils.isArrayZero(a, 4);
        
        assertTrue(result);
    }

    @Test
    public void testIsArrayZeroForSomeZeroBytes() {
        byte[] a = new byte[] {0, 0, 1, 0};
        boolean result = ArchiveUtils.isArrayZero(a, 4);
        
        assertFalse(result);
    }

    @Test
    public void testSanitizeForSanitizableString() {
        String s = "test";
        String expected = "test";
        String result = ArchiveUtils.sanitize(s);
        
        assertEquals(expected, result);
    }

    @Test
    public void testSanitizeForStringWithNonPrintableCharacters() {
        String s = "\u0001test";
        String expected = "?test";
        String result = ArchiveUtils.sanitize(s);
        
        assertEquals(expected, result);
    }

    @Test
    public void testSanitizeForLongString() {
        String s = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        String expected = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqr?";
        String result = ArchiveUtils.sanitize(s);
        
        assertEquals(expected, result);
    }

}