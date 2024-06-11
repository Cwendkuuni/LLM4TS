import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new MyArchiveEntry("file1", false, 1000);
        String expected1 = "-    1000 file1";
        Assert.assertEquals(expected1, ArchiveUtils.toString(entry1));

        ArchiveEntry entry2 = new MyArchiveEntry("dir1", true, 500);
        String expected2 = "d     500  dir1";
        Assert.assertEquals(expected2, ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer1 = expected.getBytes();
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));

        byte[] buffer2 = "different".getBytes();
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes();
        Assert.assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = "test";
        Assert.assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "abcd".getBytes();
        byte[] buffer2 = "abcd".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = "abcd".getBytes();
        byte[] buffer4 = "abdd".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "abcd".getBytes();
        byte[] buffer2 = "abcd\0\0\0".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 7));

        byte[] buffer3 = "abcd\0\0\0".getBytes();
        byte[] buffer4 = "abcd".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer3, 0, 7, buffer4, 0, 4));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        Assert.assertTrue(ArchiveUtils.isArrayZero(buffer, 10));

        buffer[0] = 1;
        Assert.assertFalse(ArchiveUtils.isArrayZero(buffer, 10));
    }

    @Test
    public void testSanitize() {
        String input = "abc123";
        String expected = "abc123";
        Assert.assertEquals(expected, ArchiveUtils.sanitize(input));

        String input2 = "abc\n123";
        String expected2 = "abc?123";
        Assert.assertEquals(expected2, ArchiveUtils.sanitize(input2));

        String input3 = "\0\0\0\0";
        String expected3 = "????";
        Assert.assertEquals(expected3, ArchiveUtils.sanitize(input3));
    }

    private class MyArchiveEntry implements ArchiveEntry {
        private String name;
        private boolean isDirectory;
        private long size;

        public MyArchiveEntry(String name, boolean isDirectory, long size) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        public long getSize() {
            return size;
        }
    }
}