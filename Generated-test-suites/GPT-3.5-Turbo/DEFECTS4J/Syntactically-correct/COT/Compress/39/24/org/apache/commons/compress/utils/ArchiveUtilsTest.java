
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntryImpl();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        String expected = "-    2000 main.c";
        String actual = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, actual);

        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");

        expected = "d     100 testfiles";
        actual = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(actual);

        expected = "abcd";
        buffer = "efgh".getBytes();

        actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(actual);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = "test".getBytes();

        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
        Assert.assertArrayEquals(expected, actual);

    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = "test";

        String actual = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        int offset = 1;
        int length = 2;
        String expected = "es";

        String actual = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(actual);

        buffer1 = "abcd".getBytes();
        buffer2 = "efgh".getBytes();

        actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        int offset1 = 1;
        int length1 = 2;
        byte[] buffer2 = "test".getBytes();
        int offset2 = 1;
        int length2 = 2;

        boolean actual = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
        Assert.assertTrue(actual);

        buffer1 = "abcd".getBytes();
        offset1 = 1;
        length1 = 2;
        buffer2 = "efgh".getBytes();
        offset2 = 1;
        length2 = 2;

        actual = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsEqualWithOffsetAndLengthAndIgnoreTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        int offset1 = 1;
        int length1 = 2;
        byte[] buffer2 = "test".getBytes();
        int offset2 = 1;
        int length2 = 2;

        boolean actual = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, false);
        Assert.assertTrue(actual);

        buffer1 = "abcd".getBytes();
        offset1 = 1;
        length1 = 2;
        buffer2 = "efgh".getBytes();
        offset2 = 1;
        length2 = 2;

        actual = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, false);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2, false);
        Assert.assertTrue(actual);

        buffer1 = "abcd".getBytes();
        buffer2 = "efgh".getBytes();

        actual = ArchiveUtils.isEqual(buffer1, buffer2, false);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        int offset1 = 1;
        int length1 = 2;
        byte[] buffer2 = "test".getBytes();
        int offset2 = 1;
        int length2 = 2;

        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2);
        Assert.assertTrue(actual);

        buffer1 = "abcd".getBytes();
        offset1 = 1;
        length1 = 2;
        buffer2 = "efgh".getBytes();
        offset2 = 1;
        length2 = 2;

        actual = ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[]{0, 0, 0, 0};
        int size = 4;

        boolean actual = ArchiveUtils.isArrayZero(a, size);
        Assert.assertTrue(actual);

        a = new byte[]{0, 1, 0, 0};
        size = 4;

        actual = ArchiveUtils.isArrayZero(a, size);
        Assert.assertFalse(actual);
    }

    @Test
    public void testSanitize() {
        String s = "test";
        String expected = "test";

        String actual = ArchiveUtils.sanitize(s);
        Assert.assertEquals(expected, actual);

        s = "te\u0000st";
        expected = "te?st";

        actual = ArchiveUtils.sanitize(s);
        Assert.assertEquals(expected, actual);
    }

    private static class ArchiveEntryImpl implements ArchiveEntry {

        private boolean isDirectory;
        private long size;
        private String name;

        public void setDirectory(boolean isDirectory) {
            this.isDirectory = isDirectory;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean isDirectory() {
            return isDirectory;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
