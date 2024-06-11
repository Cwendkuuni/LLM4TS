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
        byte[] expectedData = new byte[16];
        assertArrayEquals(expectedData, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] expectedData = new byte[20];
        assertArrayEquals(expectedData, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[16];
        field.parseFromLocalFileData(buffer, 0, 16);
        // Add assertions here
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[20];
        field.parseFromCentralDirectoryData(buffer, 0, 20);
        // Add assertions here
    }

    @Test
    public void testReparseCentralDirectoryData() throws Exception {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        // Add assertions here
    }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger();
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
        ZipEightByteInteger compressedSize = new ZipEightByteInteger();
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
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger();
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
        ZipLong diskStartNumber = new ZipLong();
        field.setDiskStartNumber(diskStartNumber);
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }
}