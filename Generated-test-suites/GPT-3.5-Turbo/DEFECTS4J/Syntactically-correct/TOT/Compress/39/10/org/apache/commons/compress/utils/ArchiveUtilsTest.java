
import org.junit.Test;
import org.junit.Assert;

import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry1 = new ArchiveEntry("file1", false, 100);
        String expected1 = "-    100 file1";
        Assert.assertEquals(expected1, ArchiveUtils.toString(entry1));

        // Edge case - isDirectory is true
        ArchiveEntry entry2 = new ArchiveEntry("dir1", true, 0);
        String expected2 = "d     0 dir1";
        Assert.assertEquals(expected2, ArchiveUtils.toString(entry2));

        // Edge case - entry name is empty
        ArchiveEntry entry3 = new ArchiveEntry("", false, 10);
        String expected3 = "-     10 ";
        Assert.assertEquals(expected3, ArchiveUtils.toString(entry3));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case - matching buffer
        String expected = "Hello";
        byte[] buffer = expected.getBytes();
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Typical use case - mismatching buffer
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer(expected, new byte[0]));

        // Edge case - empty expected string
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer("", buffer2));

        // Error scenario - unsupported encoding
        byte[] buffer3 = new byte[0];
        Assert.assertThrows(RuntimeException.class, () -> {
            ArchiveUtils.matchAsciiBuffer("Hello", buffer3);
        });
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        byte[] expected = "Hello".getBytes();
        Assert.assertArrayEquals(expected, ArchiveUtils.toAsciiBytes("Hello"));

        // Edge case - empty input string
        byte[] expected2 = new byte[0];
        Assert.assertArrayEquals(expected2, ArchiveUtils.toAsciiBytes(""));

        // Error scenario - unsupported encoding
        Assert.assertThrows(RuntimeException.class, () -> {
            ArchiveUtils.toAsciiBytes("Hello");
        });
    }

    @Test
    public void testToAsciiString() {
        // Typical use case
        byte[] inputBytes = "Hello".getBytes();
        String expected = "Hello";
        Assert.assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));

        // Edge case - offset and length specified
        byte[] inputBytes2 = "Hello, World!".getBytes();
        int offset = 7;
        int length = 6;
        String expected2 = "World!";
        Assert.assertEquals(expected2, ArchiveUtils.toAsciiString(inputBytes2, offset, length));

        // Error scenario - unsupported encoding
        byte[] inputBytes3 = new byte[0];
        Assert.assertThrows(RuntimeException.class, () -> {
            ArchiveUtils.toAsciiString(inputBytes3);
        });
    }

    @Test
    public void testIsEqual() {
        // Typical use case - equal buffers
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        // Typical use case - unequal buffers
        byte[] buffer3 = "Hello".getBytes();
        byte[] buffer4 = "World".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));

        // Edge case - empty input buffers
        byte[] buffer5 = new byte[0];
        byte[] buffer6 = new byte[0];
        Assert.assertTrue(ArchiveUtils.isEqual(buffer5, buffer6));

        // Error scenario - unequal buffers with ignoring trailing nulls
        byte[] buffer7 = new byte[] {0, 0};
        byte[] buffer8 = new byte[] {0};
        Assert.assertFalse(ArchiveUtils.isEqual(buffer7, buffer8, true));
    }

    @Test
    public void testIsEqualWithNull() {
        // Typical use case - equal buffers
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        // Typical use case - unequal buffers
        byte[] buffer3 = "Hello".getBytes();
        byte[] buffer4 = "World".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length));

        // Edge case - buffer1 and buffer2 both have trailing nulls
        byte[] buffer5 = new byte[] {0, 0};
        byte[] buffer6 = new byte[] {0, 0};
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer5, 0, buffer5.length, buffer6, 0, buffer6.length));

        // Error scenario - buffer1 has trailing nulls while buffer2 does not
        byte[] buffer7 = new byte[] {5, 0};
        byte[] buffer8 = new byte[] {5};
        Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer7, 0, buffer7.length, buffer8, 0, buffer8.length));
    }

    @Test
    public void testIsArrayZero() {
        // Typical use case - array has all zeros
        byte[] arr1 = new byte[5];
        Assert.assertTrue(ArchiveUtils.isArrayZero(arr1, arr1.length));

        // Typical use case - array has non-zero value
        byte[] arr2 = new byte[] {0, 0, 1, 0};
        Assert.assertFalse(ArchiveUtils.isArrayZero(arr2, arr2.length));

        // Edge case - array size is zero
        byte[] arr3 = new byte[0];
        Assert.assertTrue(ArchiveUtils.isArrayZero(arr3, arr3.length));
    }

    @Test
    public void testSanitize() {
        // Typical use case - string does not contain non-printable characters
        String s1 = "Hello, world!";
        String expected1 = "Hello, world!";
        Assert.assertEquals(expected1, ArchiveUtils.sanitize(s1));

        // Typical use case - string contains non-printable characters
        String s2 = "H\u0007e\u00FFy";
        String expected2 = "H?e?";
        Assert.assertEquals(expected2, ArchiveUtils.sanitize(s2));

        // Edge case - string is empty
        String s3 = "";
        String expected3 = "";
        Assert.assertEquals(expected3, ArchiveUtils.sanitize(s3));
    }
}
