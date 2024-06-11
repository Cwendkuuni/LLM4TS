import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        zipEntry.setMethod(1);
        assertEquals(1, zipEntry.getMethod());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        zipEntry.setUnixMode(755);
        assertEquals(755, zipEntry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        ZipExtraField extraField = new TestZipExtraField();
        zipEntry.addExtraField(extraField);
        assertEquals(1, zipEntry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        ZipExtraField extraField = new TestZipExtraField();
        zipEntry.addExtraField(extraField);
        zipEntry.removeExtraField(extraField.getHeaderId());
        assertEquals(0, zipEntry.getExtraFields().length);
    }

    private static class TestZipExtraField implements ZipExtraField {
        @Override
        public ZipShort getHeaderId() {
            return new ZipShort(12345);
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[0];
        }

        @Override
        public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
            // implementation not relevant for this test case
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[0];
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
            // implementation not relevant for this test case
        }
    }
}