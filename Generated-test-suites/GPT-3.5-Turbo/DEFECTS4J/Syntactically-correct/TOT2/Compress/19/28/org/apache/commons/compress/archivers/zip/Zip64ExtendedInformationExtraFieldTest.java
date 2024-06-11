import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0x0001, field.getHeaderId().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        // Test case for when size and compressedSize are both null
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = field.getLocalFileDataData();
        assertEquals(0, data.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        // Test case for all fields being null
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = field.getCentralDirectoryData();
        assertEquals(0, data.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        // Test case for length being 0
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[0];
        assertThrows(ZipException.class, () -> field.parseFromLocalFileData(buffer, 0, 0));
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[10];
        // Test case for length == 10
        assertDoesNotThrow(() -> field.parseFromCentralDirectoryData(buffer, 0, 10));
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        // Test case for all fields being present
        assertDoesNotThrow(() -> field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart));
    }

    @Test
    public void testSizeMethods() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        // Test cases for size, compressedSize, relativeHeaderOffset, diskStart getters and setters
        field.setSize(new ZipEightByteInteger(1000));
        assertEquals(1000, field.getSize().getValue());

        field.setCompressedSize(new ZipEightByteInteger(500));
        assertEquals(500, field.getCompressedSize().getValue());

        field.setRelativeHeaderOffset(new ZipEightByteInteger(200));
        assertEquals(200, field.getRelativeHeaderOffset().getValue());

        field.setDiskStartNumber(new ZipLong(1));
        assertEquals(1, field.getDiskStartNumber().getValue());
    }
}