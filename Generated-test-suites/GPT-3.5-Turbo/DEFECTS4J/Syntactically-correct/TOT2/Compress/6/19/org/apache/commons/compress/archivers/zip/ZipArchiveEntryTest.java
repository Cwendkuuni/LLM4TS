import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        Assert.assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithExistingEntry() {
        ZipArchiveEntry existingEntry = new ZipArchiveEntry("existing.txt");
        existingEntry.setMethod(2);  // Setting an unsupported compression method
        ZipArchiveEntry newEntry = new ZipArchiveEntry(existingEntry);
        Assert.assertNotEquals(existingEntry.getMethod(), newEntry.getMethod());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setUnixMode(755);
        Assert.assertEquals(755, zipEntry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new TestZipExtraField();
        zipEntry.addExtraField(extraField);
        Assert.assertEquals(extraField, zipEntry.getExtraField(extraField.getHeaderId()));
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveExtraField() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new TestZipExtraField();
        zipEntry.addExtraField(extraField);
        zipEntry.removeExtraField(extraField.getHeaderId());
        zipEntry.getExtraField(extraField.getHeaderId());  // This should throw NoSuchElementException
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        long currentTime = System.currentTimeMillis();
        zipEntry.setTime(currentTime);
        Assert.assertEquals(currentTime, zipEntry.getLastModifiedDate().getTime());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
        Assert.assertTrue(entry1.equals(entry2));
    }

    private class TestZipExtraField implements ZipExtraField {
        @Override
        public ZipShort getHeaderId() {
            return new ZipShort(1);
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[0];
        }

        @Override
        public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
            // Do nothing
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[0];
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
            // Do nothing
        }
    }
}