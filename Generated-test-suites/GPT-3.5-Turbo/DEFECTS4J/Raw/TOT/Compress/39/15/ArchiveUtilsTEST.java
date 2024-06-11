import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToStringForFileEntry() {
        ArchiveEntry entry = new MyArchiveEntry("filename.txt");
        entry.setSize(2000);
        assertFalse(entry.isDirectory());

        String expected = "-    2000 filename.txt";
        String actual = ArchiveUtils.toString(entry);

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringForDirectoryEntry() {
        ArchiveEntry entry = new MyArchiveEntry("dirname");
        entry.setSize(100);
        assertTrue(entry.isDirectory());

        String expected = "d     100 dirname";
        String actual = ArchiveUtils.toString(entry);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchAsciiBufferForMatchingBuffer() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes(StandardCharsets.US_ASCII);
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }
    
    @Test
    public void testMatchAsciiBufferForNonMatchingBuffer() {
        String expected = "Hello";
        byte[] buffer = "World".getBytes(StandardCharsets.US_ASCII);
        
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello";
        byte[] expected = inputString.getBytes(StandardCharsets.US_ASCII);

        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToAsciiStringForFullBuffer() {
        byte[] inputBytes = "Hello".getBytes(StandardCharsets.US_ASCII);
        String expected = "Hello";

        String actual = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expected, actual);
    }

    @Test
    public void testToAsciiStringForPartialBuffer() {
        byte[] inputBytes = "Hello World".getBytes(StandardCharsets.US_ASCII);
        String expected = "World";

        String actual = ArchiveUtils.toAsciiString(inputBytes, 6, 5);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsEqualForEqualBuffers() {
        byte[] buffer1 = "Hello".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "Hello".getBytes(StandardCharsets.US_ASCII);

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualForUnequalBuffers() {
        byte[] buffer1 = "Hello".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "World".getBytes(StandardCharsets.US_ASCII);

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualForEqualBuffersWithTrailingNulls() {
        byte[] buffer1 = "Hello\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "Hello".getBytes(StandardCharsets.US_ASCII);

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualForEqualBuffersWithoutTrailingNulls() {
        byte[] buffer1 = "Hello\0\0".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "Hello".getBytes(StandardCharsets.US_ASCII);

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, false));
    }

    @Test
    public void testIsArrayZeroForZeroArray() {
        byte[] buffer = new byte[5];

        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
    }

    @Test
    public void testIsArrayZeroForNonZeroArray() {
        byte[] buffer = "Hello".getBytes(StandardCharsets.US_ASCII);

        assertFalse(ArchiveUtils.isArrayZero(buffer, 5));
    }

    @Test
    public void testSanitizeForPrintableString() {
        String input = "Hello";

        String expected = "Hello";
        String actual = ArchiveUtils.sanitize(input);

        assertEquals(expected, actual);
    }

    @Test
    public void testSanitizeForNonPrintableString() {
        String input = "Hello\u0001";

        String expected = "Hello?";
        String actual = ArchiveUtils.sanitize(input);

        assertEquals(expected, actual);
    }

    private class MyArchiveEntry implements ArchiveEntry {

        private String name;
        private long size;
        private boolean isDirectory;

        public MyArchiveEntry(String name) {
            this.name = name;
            this.size = 0;
            this.isDirectory = false;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public boolean isDirectory() {
            return isDirectory;
        }

        // other methods omitted for brevity
    }

}