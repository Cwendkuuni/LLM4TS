import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry baseEntry = new java.util.zip.ZipEntry("test.zip");
        baseEntry.setMethod(java.util.zip.ZipEntry.STORED);
        ZipArchiveEntry entry = new ZipArchiveEntry(baseEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(java.util.zip.ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipArchiveEntry() {
        ZipArchiveEntry sourceEntry = new ZipArchiveEntry("source.txt");
        sourceEntry.setUnixMode(755);
        ZipArchiveEntry entry = new ZipArchiveEntry(sourceEntry);
        assertEquals("source.txt", entry.getName());
        assertEquals(755, entry.getUnixMode());
    }

    @Test
    public void testZipArchiveEntrySetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(644);
        assertEquals(644, entry.getUnixMode());
    }

    @Test
    public void testZipArchiveEntryIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(java.util.zip.ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZipArchiveEntrySetMethodWithNegativeValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testZipArchiveEntrySetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(java.util.zip.ZipEntry.STORED);
        assertEquals(java.util.zip.ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testZipArchiveEntrySetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(12345L);
        assertEquals(12345L, entry.getExternalAttributes());
    }

    @Test
    public void testZipArchiveEntryAddExtraField() {
        ZipExtraField mockField = new MockExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(mockField);
        assertEquals(1, entry.getExtraFields().length);
    }

    @Test
    public void testZipArchiveEntryRemoveExtraField() {
        ZipExtraField mockField = new MockExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(mockField);
        entry.removeExtraField(new ZipShort(123));
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test
    public void testZipArchiveEntryEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));
    }

    private class MockExtraField implements ZipExtraField {
        @Override
        public ZipShort getHeaderId() {
            return new ZipShort(123);
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[0];
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[0];
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[0];
        }
    }
}