import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testEntry");
        assertEquals("testEntry", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("testEntry");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        assertEquals("testEntry", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testEntry");
        zipEntry.setUnixMode(777);
        assertEquals(777, zipEntry.getUnixMode());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipFileEntry = new ZipArchiveEntry("testDir/");
        assertTrue(zipFileEntry.isDirectory());

        ZipArchiveEntry zipNonDirEntry = new ZipArchiveEntry("testFile");
        assertFalse(zipNonDirEntry.isDirectory());
    }

    @Test
    public void testSetAndGetExtraFields() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testEntry");
        ZipExtraField[] extraFields = new ZipExtraField[1];
        extraFields[0] = new TestZipExtraField();
        zipEntry.setExtraFields(extraFields);
        ZipExtraField[] retrievedFields = zipEntry.getExtraFields();
        assertEquals(1, retrievedFields.length);
        assertEquals("Test", retrievedFields[0].getData());
    }

    @Test
    public void testAddAndRemoveExtraField() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testEntry");
        ZipExtraField extraField = new TestZipExtraField();
        zipEntry.addExtraField(extraField);
        assertTrue(zipEntry.getExtraField(new ZipShort(1)) != null);
        zipEntry.removeExtraField(new ZipShort(1));
        assertTrue(zipEntry.getExtraField(new ZipShort(1)) == null);
    }

    private static class TestZipExtraField implements ZipExtraField {

        @Override
        public ZipShort getHeaderId() {
            return new ZipShort(1);
        }

        @Override
        public ZipShort getLocalFileDataLength() {
            return new ZipShort(4);
        }

        @Override
        public ZipShort getCentralDirectoryLength() {
            return new ZipShort(4);
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public byte[] getHeaderIdBytes() {
            return new byte[]{0, 1};
        }

        @Override
        public byte[] getLocalFileDataLengthBytes() {
            return new byte[]{4, 0};
        }

        @Override
        public byte[] getCentralDirectoryLengthBytes() {
            return new byte[]{4, 0};
        }

        @Override
        public byte[] getLocalFileData() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
            // Mock implementation
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
            // Mock implementation
        }

        @Override
        public byte[] getRawData() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public byte[] getLocalFileDataExtra() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public byte[] getCentralDirectoryExtra() {
            return new byte[]{0, 0, 0, 0};
        }

        @Override
        public ZipExtraField duplicate() {
            return new TestZipExtraField();
        }

        @Override
        public ZipShort getLocalFileDataLengthZipShort() {
            return new ZipShort(4);
        }

        @Override
        public ZipShort getCentralDirectoryLengthZipShort() {
            return new ZipShort(4);
        }

        @Override
        public byte[] getData() {
            return new byte[]{0, 0, 0, 0};
        }

    }

}