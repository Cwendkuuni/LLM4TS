import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
    
    private static final String TEST_STRING = "test";
    private static final byte[] TEST_BYTES = TEST_STRING.getBytes(StandardCharsets.US_ASCII);
    
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry() {
            public boolean isDirectory() {
                return false;
            }
            
            public long getSize() {
                return 0;
            }
            
            public String getName() {
                return "test.txt";
            }
        };
        
        String expected = "-       0 test.txt";
        String actual = ArchiveUtils.toString(entry);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        assertTrue(ArchiveUtils.matchAsciiBuffer(TEST_STRING, TEST_BYTES));
    }
    
    @Test
    public void testMatchAsciiBuffer_withOffsetAndLength() {
        assertTrue(ArchiveUtils.matchAsciiBuffer(TEST_STRING, TEST_BYTES, 0, TEST_BYTES.length));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] expected = TEST_STRING.getBytes(StandardCharsets.US_ASCII);
        byte[] actual = ArchiveUtils.toAsciiBytes(TEST_STRING);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiString() {
        String expected = TEST_STRING;
        String actual = ArchiveUtils.toAsciiString(TEST_BYTES);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiString_withOffsetAndLength() {
        String expected = TEST_STRING;
        String actual = ArchiveUtils.toAsciiString(TEST_BYTES, 0, TEST_BYTES.length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "abc".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc".getBytes(StandardCharsets.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls() {
        byte[] buffer1 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0".getBytes(StandardCharsets.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }
    
    @Test
    public void testIsEqual_noMatch() {
        byte[] buffer1 = "abc".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "def".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_noMatch() {
        byte[] buffer1 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0xyz".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_buffer1Longer() {
        byte[] buffer1 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_buffer2Longer() {
        byte[] buffer1 = "abc\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }
    
    @Test
    public void testIsEqual_buffer1AndBuffer2Same() {
        byte[] buffer1 = "abc".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc".getBytes(StandardCharsets.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_buffer1AndBuffer2Same() {
        byte[] buffer1 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0".getBytes(StandardCharsets.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqual_buffer1AndBuffer2Different() {
        byte[] buffer1 = "abc".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "def".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_buffer1AndBuffer2Different() {
        byte[] buffer1 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0xyz".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_buffer1Longer() {
        byte[] buffer1 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls_buffer2Longer() {
        byte[] buffer1 = "abc\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc\0\0\0".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "abc".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "abc".getBytes(StandardCharsets.US_ASCII);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testIsEqualWithNull_noMatch() {
        byte[] buffer1 = "abc".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "def".getBytes(StandardCharsets.US_ASCII);
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testIsArrayZero_allZero() {
        byte[] array = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
    }
    
    @Test
    public void testIsArrayZero_notAllZero() {
        byte[] array = {0, 1, 2, 3, 0};
        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }
    
    @Test
    public void testSanitize() {
        String input = "abc\0def";
        String expected = "abc?def";
        String actual = ArchiveUtils.sanitize(input);
        assertEquals(expected, actual);
    }
    
}