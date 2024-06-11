import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", entry.getName());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(2);
        assertEquals(2, entry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setInternalAttributes(1);
        assertEquals(1, entry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setExternalAttributes(12345);
        assertEquals(12345, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(777);
        assertEquals(777, entry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.addExtraField(new TestZipExtraField());
        assertNotNull(entry.getExtraField(new ZipShort(1)));
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        TestZipExtraField extraField = new TestZipExtraField();
        entry.addExtraField(extraField);
        entry.removeExtraField(new ZipShort(1));
        assertNull(entry.getExtraField(new ZipShort(1)));
    }

    // Add more test cases for additional methods and edge cases

}

class TestZipExtraField implements ZipExtraField {

    public ZipShort getHeaderId() {
        return new ZipShort(1);
    }

    public byte[] getLocalFileDataData() {
        return new byte[0];
    }

    public byte[] getCentralDirectoryData() {
        return new byte[0];
    }

    public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
        // Do nothing for testing
    }

    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
        // Do nothing for testing
    }

}