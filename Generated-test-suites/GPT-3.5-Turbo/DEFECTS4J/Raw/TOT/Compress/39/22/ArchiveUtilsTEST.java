import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntryImpl("file1.txt", false, 100);
        ArchiveEntry entry2 = new ArchiveEntryImpl("dir1", true, 0);

        assertEquals("-    100 file1.txt", ArchiveUtils.toString(entry1));
        assertEquals("d      0 dir1", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        assertFalse(ArchiveUtils.matchAsciiBuffer("hello", new byte[]{(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F}));
        assertTrue(ArchiveUtils.matchAsciiBuffer("hello", new byte[]{(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x00}));
        assertTrue(ArchiveUtils.matchAsciiBuffer("hello", new byte[]{(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x00, (byte) 0x00}));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] bytes = ArchiveUtils.toAsciiBytes("hello");
        assertArrayEquals(new byte[]{(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F}, bytes);
    }

    @Test
    public void testToAsciiString() {
        byte[] bytes = {(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
        String str = ArchiveUtils.toAsciiString(bytes);
        assertEquals("hello", str);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
        byte[] buffer2 = {(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = {(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x00};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        assertTrue(ArchiveUtils.isArrayZero(array1, 4));

        byte[] array2 = {(byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C};
        assertFalse(ArchiveUtils.isArrayZero(array2, 4));
    }

    @Test
    public void testSanitize() {
        String s1 = "hello";
        assertEquals("hello", ArchiveUtils.sanitize(s1));

        String s2 = "\u0008world";
        assertEquals("?world", ArchiveUtils.sanitize(s2));
    }

    private static class ArchiveEntryImpl implements ArchiveEntry {
        private String name;
        private boolean isDirectory;
        private long size;

        public ArchiveEntryImpl(String name, boolean isDirectory, long size) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.size = size;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isDirectory() {
            return isDirectory;
        }

        @Override
        public long getSize() {
            return size;
        }
    }
}