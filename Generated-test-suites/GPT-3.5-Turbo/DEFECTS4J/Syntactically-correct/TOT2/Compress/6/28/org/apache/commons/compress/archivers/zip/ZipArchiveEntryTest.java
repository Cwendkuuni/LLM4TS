import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntry_ConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntry_ConstructorWithZipEntry() {
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntry_ConstructorWithZipArchiveEntry() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testClone() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        assertEquals(entry.getName(), clonedEntry.getName());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(0);
        assertEquals(0, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(755);
        assertEquals(755, entry.getUnixMode());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setPlatform(3);
        assertEquals(3, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new TestZipExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setExtraFields(fields);
        assertEquals(1, entry.getExtraFields().length);
    }

    @Test
    public void testAddExtraField() {
        TestZipExtraField extraField = new TestZipExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.addExtraField(extraField);
        assertEquals(extraField, entry.getExtraField(extraField.getHeaderId()));
    }

    @Test
    public void testAddAsFirstExtraField() {
        TestZipExtraField extraField = new TestZipExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.addAsFirstExtraField(extraField);
        assertEquals(extraField, entry.getExtraFields()[0]);
    }

    @Test
    public void testRemoveExtraField() {
        TestZipExtraField extraField = new TestZipExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.addExtraField(extraField);
        entry.removeExtraField(extraField.getHeaderId());
        assertNull(entry.getExtraField(extraField.getHeaderId()));
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        byte[] centralDirExtra = entry.getCentralDirectoryExtra();
        assertNotNull(centralDirExtra);
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.zip");
        assertTrue(entry1.equals(entry2));
    }

    private static class TestZipExtraField implements ZipExtraField {
        public ZipShort getHeaderId() {
            return null;
        }
        public ZipExtraField createFromLocalFileData(byte[] data, int offset, int length) {
            return null;
        }
        public ZipExtraField createFromCentralDirectoryData(byte[] data, int offset, int length) {
            return null;
        }
        public byte[] getLocalFileDataData() {
            return null;
        }
        public byte[] getCentralDirectoryData() {
            return null;
        }
        public byte[] getLocalFileData() {
            return null;
        }
        public byte[] getCentralDirectoryData() {
            return null;
        }
        public int getLocalFileDataLength() {
            return 0;
        }
        public int getCentralDirectoryLength() {
            return 0;
        }
        public byte[] getData() {
            return null;
        }
    }
}