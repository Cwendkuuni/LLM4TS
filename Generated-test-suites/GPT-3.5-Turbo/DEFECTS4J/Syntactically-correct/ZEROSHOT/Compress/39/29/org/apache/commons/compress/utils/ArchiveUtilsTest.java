import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry();
        entry1.setDirectory(true);
        entry1.setSize(100);
        entry1.setName("testDirectory");
        
        ArchiveEntry entry2 = new ArchiveEntry();
        entry2.setDirectory(false);
        entry2.setSize(2000);
        entry2.setName("main.c");
        
        String result1 = ArchiveUtils.toString(entry1);
        String result2 = ArchiveUtils.toString(entry2);
        
        assertEquals("d       100 testDirectory", result1);
        assertEquals("-      2000 main.c", result2);
    }

    @Test
    public void testMatchAsciiBuffer_withOffsetAndLength() {
        String expected = "hello world";
        byte[] buffer = new byte[11];
        System.arraycopy(expected.getBytes(), 0, buffer, 0, 11);
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 11);
        
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_withoutOffsetAndLength() {
        String expected = "hello world";
        byte[] buffer = expected.getBytes();
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "hello world";
        byte[] result = ArchiveUtils.toAsciiBytes(expected);
        
        assertArrayEquals(expected.getBytes(), result);
    }

    @Test
    public void testToAsciiString_withOffsetAndLength() {
        String expected = "hello world";
        byte[] buffer = expected.getBytes();
        
        String result = ArchiveUtils.toAsciiString(buffer, 0, buffer.length);
        
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiString_withoutOffsetAndLength() {
        String expected = "hello world";
        byte[] buffer = expected.getBytes();
        
        String result = ArchiveUtils.toAsciiString(buffer);
        
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual_withOffsetAndLength() {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqual_withoutOffsetAndLength() {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqual_withIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[] {1, 2, 3, 0, 0, 0};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqual_withoutIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 3, 0, 0, 0};
        
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        
        assertFalse(result);
    }

    @Test
    public void testIsEqual_withTwoBuffers() {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqual_withIgnoreTrailingNulls_usingTwoBuffers() {
        byte[] buffer1 = new byte[] {1, 2, 3, 0, 0, 0};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqual_withOffsetAndLength_withTrailingNulls() {
        byte[] buffer1 = new byte[] {1, 2, 3, 0, 0, 0};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] {1, 2, 3, 0, 0, 0};
        byte[] buffer2 = new byte[] {1, 2, 3};
        
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[] {0, 0, 0, 0, 0};
        
        boolean result = ArchiveUtils.isArrayZero(buffer, buffer.length);
        
        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String input = "hello \u0000 world";
        
        String result = ArchiveUtils.sanitize(input);
        
        assertEquals("hello ? world", result);
    }

}