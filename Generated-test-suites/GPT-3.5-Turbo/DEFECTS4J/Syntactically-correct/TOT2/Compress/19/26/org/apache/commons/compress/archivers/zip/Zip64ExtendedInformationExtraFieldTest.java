import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testSetSizeAndCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(2048);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(1024);

        field.setSize(size);
        field.setCompressedSize(compressedSize);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        field.parseFromLocalFileData(data, 0, data.length);

        assertEquals(new ZipEightByteInteger(1024), field.getSize());
        assertEquals(new ZipEightByteInteger(1024), field.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00};
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(true, true, false, false);

        assertEquals(new ZipEightByteInteger(2048), field.getSize());
        assertEquals(new ZipEightByteInteger(2048), field.getCompressedSize());
    }

    @Test
    public void testIncompleteDataInParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x00, 0x00, 0x00, 0x00, 0x08};
        assertThrows(ZipException.class, () -> field.parseFromCentralDirectoryData(data, 0, data.length));
    }

    // Add additional test cases for error scenarios, edge cases, etc.

}