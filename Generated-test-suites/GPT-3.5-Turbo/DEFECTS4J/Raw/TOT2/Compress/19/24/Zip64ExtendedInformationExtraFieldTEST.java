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
        assertEquals(4, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(20, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = field.getLocalFileDataData();
        assertArrayEquals(new byte[]{0xe8, 0x03, 0x20, 0x03}, data);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0xe8, 0x03, 0x20, 0x03};
        field.parseFromLocalFileData(buffer, 0, 4);
        assertEquals(1000, field.getSize().getValue());
        assertEquals(800, field.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0xe8, 0x03, 0x20, 0x03};
        field.parseFromCentralDirectoryData(buffer, 0, 4);
        assertEquals(1000, field.getSize().getValue());
        assertEquals(800, field.getCompressedSize().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(new byte[]{}, 0, 0);
        field.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(1000, field.getSize().getValue());
        assertEquals(800, field.getCompressedSize().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLFHMustHaveBothSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0xe8};
        field.parseFromLocalFileData(buffer, 0, 1);
    }

    @Test
    public void testGetSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, null);
        assertEquals(1000, field.getSize().getValue());
    }

    @Test
    public void testGetCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, compressedSize);
        assertEquals(800, field.getCompressedSize().getValue());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(200);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null, relativeHeaderOffset, null);
        assertEquals(200, field.getRelativeHeaderOffset().getValue());
    }

    @Test
    public void testGetDiskStartNumber() {
        ZipLong diskStart = new ZipLong(3);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null, null, diskStart);
        assertEquals(3, field.getDiskStartNumber().getValue());
    }    
}