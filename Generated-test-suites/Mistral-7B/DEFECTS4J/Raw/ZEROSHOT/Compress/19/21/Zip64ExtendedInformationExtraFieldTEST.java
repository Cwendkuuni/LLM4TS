import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, field.getHeaderId());
    }

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(ZipShort.ZERO, field.getHeaderId());
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSize() {
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger((long) 512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCompressedSize() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger((long) 1024), null);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(new ZipShort(DWORD * 2), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStartNumber = new ZipLong(5678);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        assertEquals(new ZipShort((2 * DWORD) + DWORD + WORD), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] actualData = field.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStartNumber = new ZipLong(5678);
        byte[] expectedData = new byte[2 * DWORD + DWORD + WORD];
        int offset = addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (diskStartNumber != null) {
            System.arraycopy(diskStartNumber.getBytes(), 0, expectedData, offset, WORD);
            offset += WORD;
        }
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        byte[] actualData = field.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[6 * DWORD];
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((long) 512);
        System.arraycopy(expectedSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        assertEquals(expectedSize, field.getSize());
        assertEquals(expectedCompressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithIncompleteData() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[15];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(field.getSize(), null);
        assertEquals(field.getCompressedSize(), null);
        assertEquals(field.getRelativeHeaderOffset(), null);
        assertEquals(field.getDiskStartNumber(), null);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithFullData() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStartNumber = new ZipLong(5678);
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(null, field.getSize());
        assertEquals(null, field.getCompressedSize());
        assertEquals(null, field.getRelativeHeaderOffset());
        assertEquals(null, field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithDiskStartNumber() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStartNumber = new ZipLong(5678);
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStartNumber = true;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStartNumber);
        assertEquals(field.getSize(), new ZipEightByteInteger((long) 1024));
        assertEquals(field.getCompressedSize(), new ZipEightByteInteger((long) 512));
        assertEquals(field.getRelativeHeaderOffset(), new ZipEightByteInteger(1234));
        assertEquals(field.getDiskStartNumber(), new ZipLong(5678));
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoUncompressedSize() {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];
        boolean hasUncompressedSize = false;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStartNumber = true;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStartNumber);
        assertNull(field.getSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoCompressedSize() {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = false;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStartNumber = true;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStartNumber);
        assertNull(field.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoRelativeHeaderOffset() {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStartNumber = true;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStartNumber);
        assertNull(field.getRelativeHeaderOffset());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoDiskStartNumber() {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStartNumber = false;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStartNumber);
        assertNull(field.getDiskStartNumber());
    }

    private void addSizes(byte[] data) {
        int offset = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
        }
    }
}