import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getLocalFileDataData().length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getCentralDirectoryData().length);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        field.parseFromLocalFileData(data, 0, data.length);
        // Test for successful parsing
        assertNotNull(field.getSize());
        assertNotNull(field.getCompressedSize());
        // Test for invalid input
        try {
            field.parseFromLocalFileData(new byte[0], 0, 0);
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            // Expected
        }
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        field.parseFromCentralDirectoryData(data, 0, data.length);
        // Test for successful parsing
        assertNotNull(field.getSize());
        assertNotNull(field.getCompressedSize());
        // Test for invalid input
        try {
            field.parseFromCentralDirectoryData(new byte[0], 0, 0);
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            // Expected
        }
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = false;
        byte[] data = {0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertNotNull(field.getSize());
        assertNotNull(field.getCompressedSize());
        // Test for invalid input length
        try {
            field.reparseCentralDirectoryData(false, false, false, false);
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            // Expected
        }
    }

    // Add more test cases as needed for the remaining methods

}