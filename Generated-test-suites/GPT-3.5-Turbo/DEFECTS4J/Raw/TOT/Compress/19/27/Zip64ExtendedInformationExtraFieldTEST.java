import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        assertNull(z.getSize());
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1234567890);
        z.setSize(size);
        assertEquals(size, z.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        assertNull(z.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(987654321);
        z.setCompressedSize(compressedSize);
        assertEquals(compressedSize, z.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        assertNull(z.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger offset = new ZipEightByteInteger(1111111);
        z.setRelativeHeaderOffset(offset);
        assertEquals(offset, z.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        assertNull(z.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        ZipLong diskStart = new ZipLong(123);
        z.setDiskStartNumber(diskStart);
        assertEquals(diskStart, z.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        ZipShort expected = new ZipShort(0x0001);
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        assertEquals(expected, z.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        assertEquals(expected, z.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        assertEquals(expected, z.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        byte[] expected = new byte[0];
        assertArrayEquals(expected, z.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        byte[] expected = new byte[0];
        assertArrayEquals(expected, z.getCentralDirectoryData());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = 0;
        z.parseFromLocalFileData(buffer, offset, length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = 0;
        z.parseFromCentralDirectoryData(buffer, offset, length);
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField z = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = false;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = false;
        z.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
    }
}