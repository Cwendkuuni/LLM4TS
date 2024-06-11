import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test when entry is a file
        ArchiveEntry entry1 = new ArchiveEntry() {
            @Override
            public long getSize() {
                return 2000;
            }
            @Override
            public String getName() {
                return "main.c";
            }
            @Override
            public boolean isDirectory() {
                return false;
            }
        };
        String expected1 = "-    2000 main.c";
        String result1 = ArchiveUtils.toString(entry1);
        assertEquals(expected1, result1);

        // Test when entry is a directory
        ArchiveEntry entry2 = new ArchiveEntry() {
            @Override
            public long getSize() {
                return 100;
            }
            @Override
            public String getName() {
                return "testfiles";
            }
            @Override
            public boolean isDirectory() {
                return true;
            }
        };
        String expected2 = "d     100 testfiles";
        String result2 = ArchiveUtils.toString(entry2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Test when buffer matches expected Ascii string
        String expected = "Hello World";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        
        // Test when buffer does not match expected Ascii string
        String unexpected = "Goodbye World";
        assertFalse(ArchiveUtils.matchAsciiBuffer(unexpected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        // Test conversion of string to Ascii bytes
        String inputString = "Hello World";
        byte[] expected = inputString.getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        // Test conversion of Ascii bytes to string
        byte[] inputBytes = "Hello World".getBytes();
        String expected = "Hello World";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        // Test when two buffers have same contents
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        // Test when two buffers have different contents
        byte[] buffer3 = "Goodbye World".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithNull() {
        // Test when two buffers have same contents and trailing nulls are ignored
        byte[] buffer1 = {1, 2, 3, 0, 0, 0};
        byte[] buffer2 = {1, 2, 3};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        // Test when two buffers have different contents and trailing nulls are ignored
        byte[] buffer3 = {1, 2, 4, 0, 0, 0};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsArrayZero() {
        // Test when the first N bytes of the array are all zero
        byte[] a1 = {0, 0, 0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(a1, 5));

        // Test when the first N bytes of the array are not all zero
        byte[] a2 = {0, 0, 1, 0, 0};
        assertFalse(ArchiveUtils.isArrayZero(a2, 5));
    }

    @Test
    public void testSanitize() {
        // Test sanitizing a string with printable characters
        String input1 = "Hello World";
        String expected1 = "Hello World";
        String result1 = ArchiveUtils.sanitize(input1);
        assertEquals(expected1, result1);

        // Test sanitizing a string with non-printable characters
        String input2 = "Hello\u0003World";
        String expected2 = "Hello?World";
        String result2 = ArchiveUtils.sanitize(input2);
        assertEquals(expected2, result2);
    }

}