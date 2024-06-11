import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ArchiveUtilsTest {
    @Test
    public void testToString() {
        ArchiveEntry fileEntry = new ArchiveEntry("file.txt", false, 100);
        ArchiveEntry directoryEntry = new ArchiveEntry("directory", true, 0);
        
        assertEquals("-     100 file.txt", ArchiveUtils.toString(fileEntry));
        assertEquals("d       0 directory", ArchiveUtils.toString(directoryEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = {0x41, 0x42, 0x43};
        assertTrue(ArchiveUtils.matchAsciiBuffer("ABC", buffer));
        
        byte[] buffer1 = {0x41, 0x42, 0x43};
        byte[] buffer2 = {0x44, 0x45, 0x46};
        assertFalse(ArchiveUtils.matchAsciiBuffer("DEF", buffer1, 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("DEF", buffer2, 0, 3));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = {0x41, 0x42, 0x43};
        byte[] result = ArchiveUtils.toAsciiBytes("ABC");
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {0x41, 0x42, 0x43};
        String expected = "ABC";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {0x41, 0x42, 0x43, 0x44, 0x45};
        String expected = "BCD";
        String result = ArchiveUtils.toAsciiString(inputBytes, 1, 3);
        
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {0x41, 0x42, 0x43};
        byte[] buffer2 = {0x41, 0x42, 0x43};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        byte[] buffer3 = {0x41, 0x42, 0x43};
        byte[] buffer4 = {0x44, 0x45, 0x46};
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = {0x41, 0x42, 0x43};
        byte[] buffer2 = {0x41, 0x42, 0x43};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3));
        
        byte[] buffer3 = {0x41, 0x42, 0x43};
        byte[] buffer4 = {0x44, 0x45, 0x46};
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, 3, buffer4, 0, 3));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {0x41, 0x42, 0x43, 0x00};
        byte[] buffer2 = {0x41, 0x42, 0x43};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3, true));
        
        byte[] buffer3 = {0x41, 0x42, 0x43};
        byte[] buffer4 = {0x41, 0x42, 0x43, 0x00};
        assertTrue(ArchiveUtils.isEqual(buffer3, 0, 3, buffer4, 0, 4, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = {0x00, 0x00, 0x00};
        assertTrue(ArchiveUtils.isArrayZero(a, 3));
        
        byte[] b = {0x00, 0x00, 0x01};
        assertFalse(ArchiveUtils.isArrayZero(b, 3));
    }

    @Test
    public void testSanitize() {
        String input = "abc\ndef\nghi";
        String expected = "abc?def?ghi";
        String result = ArchiveUtils.sanitize(input);
        
        assertEquals(expected, result);
        
        String input2 = "abc가나다def";
        String expected2 = "abc?def";
        String result2 = ArchiveUtils.sanitize(input2);
        
        assertEquals(expected2, result2);
    }
}