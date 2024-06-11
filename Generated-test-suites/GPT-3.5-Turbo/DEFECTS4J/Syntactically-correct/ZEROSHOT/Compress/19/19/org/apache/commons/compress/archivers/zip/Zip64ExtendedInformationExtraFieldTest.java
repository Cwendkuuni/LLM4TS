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
        assertEquals(new ZipShort(16), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(20), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new byte[16], field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new byte[20], field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[16];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, 16);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidLength() {
        byte[] buffer = new byte[8];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, 8);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[20];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 20);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] buffer = new byte[20];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 20);
        field.reparseCentralDirectoryData(false, false, false, false);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataInvalidLength() {
        byte[] buffer = new byte[20];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 20);
        field.reparseCentralDirectoryData(true, true, true, false);
    }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(123456789L);
        field.setSize(size);
        assertEquals(size, field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(987654321L);
        field.setCompressedSize(compressedSize);
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(123456789L);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipLong diskStartNumber = new ZipLong(1234);
        field.setDiskStartNumber(diskStartNumber);
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }

    @Test
    public void testAddSizes() {
        byte[] data = new byte[16];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        int off = field.addSizes(data);
        assertEquals(0, off);
    }
}