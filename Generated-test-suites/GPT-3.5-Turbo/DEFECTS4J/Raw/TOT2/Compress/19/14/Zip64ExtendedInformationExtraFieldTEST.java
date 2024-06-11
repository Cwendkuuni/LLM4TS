import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertEquals(new ZipShort(0x0001), zip64.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertEquals(new ZipShort(0), zip64.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertEquals(new ZipShort(0), zip64.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertArrayEquals(new byte[0], zip64.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertArrayEquals(new byte[0], zip64.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        try {
            zip64.parseFromLocalFileData(new byte[0], 0, 0);
            fail("No exception thrown for empty data");
        } catch (ZipException e) {
            assertEquals("Zip64 extended information must contain both size values in the local file header.", e.getMessage());
        }
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        try {
            zip64.parseFromCentralDirectoryData(new byte[0], 0, 0);
            fail("No exception thrown for empty data");
        } catch (ZipException e) {
            assertEquals("central directory zip64 extended information extra field's length doesn't match central directory data. Expected length 0 but is 0", e.getMessage());
        }
    }

    @Test
    public void testReparseCentralDirectoryData() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        try {
            zip64.reparseCentralDirectoryData(true, false, false, false);
            fail("No exception thrown for mismatch expected length");
        } catch (ZipException e) {
            assertEquals("central directory zip64 extended information extra field's length doesn't match central directory data. Expected length 8 but is 0", e.getMessage());
        }
    }

    @Test
    public void testGetSize() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertNull(zip64.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertNull(zip64.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertNull(zip64.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        // Create object
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        // Test
        assertNull(zip64.getDiskStartNumber());
    }
}