
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
        assertEquals(new ZipShort(8), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[]{0, 0, 0, 0}, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(), field.getSize());
        assertEquals(new ZipEightByteInteger(), field.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(), field.getSize());
        assertEquals(new ZipEightByteInteger(), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
   }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = false;
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertEquals(new ZipEightByteInteger(), field.getSize());
        assertEquals(new ZipEightByteInteger(), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
   }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(new ZipEightByteInteger());
        assertEquals(new ZipEightByteInteger(), field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setCompressedSize(new ZipEightByteInteger());
        assertEquals(new ZipEightByteInteger(), field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setRelativeHeaderOffset(new ZipEightByteInteger());
        assertEquals(new ZipEightByteInteger(), field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setDiskStartNumber(new ZipLong());
        assertEquals(new ZipLong(), field.getDiskStartNumber());
    }
}
