import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField;

    @Before
    public void setUp() {
        zip64ExtendedInformationExtraField = new Zip64ExtendedInformationExtraField();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), zip64ExtendedInformationExtraField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(new ZipShort(16), zip64ExtendedInformationExtraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(20), zip64ExtendedInformationExtraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        assertArrayEquals(expected, zip64ExtendedInformationExtraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        assertArrayEquals(expected, zip64ExtendedInformationExtraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        zip64ExtendedInformationExtraField.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(buffer, 0), zip64ExtendedInformationExtraField.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), zip64ExtendedInformationExtraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        zip64ExtendedInformationExtraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(buffer, 0), zip64ExtendedInformationExtraField.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), zip64ExtendedInformationExtraField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        zip64ExtendedInformationExtraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        byte[] expected = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        assertArrayEquals(expected, zip64ExtendedInformationExtraField.getCentralDirectoryData());
    }

    @Test
    public void testGetSize() {
        assertNull(zip64ExtendedInformationExtraField.getSize());
    }

    @Test
    public void testSetSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(0);
        zip64ExtendedInformationExtraField.setSize(size);
        assertEquals(size, zip64ExtendedInformationExtraField.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        assertNull(zip64ExtendedInformationExtraField.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0);
        zip64ExtendedInformationExtraField.setCompressedSize(compressedSize);
        assertEquals(compressedSize, zip64ExtendedInformationExtraField.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        assertNull(zip64ExtendedInformationExtraField.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0);
        zip64ExtendedInformationExtraField.setRelativeHeaderOffset(relativeHeaderOffset);
        assertEquals(relativeHeaderOffset, zip64ExtendedInformationExtraField.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        assertNull(zip64ExtendedInformationExtraField.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        ZipLong diskStart = new ZipLong(0);
        zip64ExtendedInformationExtraField.setDiskStartNumber(diskStart);
        assertEquals(diskStart, zip64ExtendedInformationExtraField.getDiskStartNumber());
    }
}