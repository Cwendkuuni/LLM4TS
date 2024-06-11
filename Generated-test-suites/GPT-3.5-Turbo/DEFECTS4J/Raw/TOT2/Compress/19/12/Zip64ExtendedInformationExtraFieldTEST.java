import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0x0001, extraField.getHeaderId().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(4, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(8, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = extraField.getLocalFileDataData();
        assertEquals(0, data.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = extraField.getCentralDirectoryData();
        assertEquals(16, data.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromLocalFileData(buffer, 0, 12);
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(buffer, 0, 12);
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(buffer, 0, 12);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertNotNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testConstructors() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());

        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(87654321);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertNotNull(extraField.getSize());
        assertEquals(12345678, extraField.getSize().getValue());
        assertEquals(87654321, extraField.getCompressedSize().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompleteLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromLocalFileData(buffer, 0, 8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromLocalFileData(buffer, 0, 8);
    }

}