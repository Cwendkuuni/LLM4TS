import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(HEADER_ID, field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthWithNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLengthWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(new ZipShort(2 * DWORD), field.getLocalFileDataLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithOneMissingSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, null);
        field.getLocalFileDataLength();
    }

    @Test
    public void testGetCentralDirectoryLengthWithNoSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(new ZipShort(DWORD + DWORD + DWORD + WORD), field.getCentralDirectoryLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCentralDirectoryLengthWithOneMissingSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, null);
        field.getCentralDirectoryLength();
    }

    @Test
    public void testGetLocalFileDataDataWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = field.getLocalFileDataData();
        ZipShort length = new ZipShort(2 * DWORD);
        assertEquals(length.getValue(), data.length);
        addSizesCheck(data, size.getBytes(), compressedSize.getBytes());
    }

    @Test
    public void testGetCentralDirectoryDataWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(20L);
        ZipLong diskStart = new ZipLong(12345L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        byte[] data = field.getCentralDirectoryData();
        ZipShort length = new ZipShort(DWORD + DWORD + DWORD + WORD);
        assertEquals(length.getValue(), data.length);
        addSizesCheck(data, size.getBytes(), compressedSize.getBytes());
        if (relativeHeaderOffset != null) {
            addOffsetCheck(data, relativeHeaderOffset.getBytes(), DWORD);
        }
        if (diskStart != null) {
            addDiskStartCheck(data, diskStart.getBytes(), WORD);
        }
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizes() throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        byte[] data = new byte[2 * DWORD];
        addSizes(data);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        assertEquals(size, field.size);
        assertEquals(compressedSize, field.compressedSize);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithNoSizes() throws ZipException {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithOneMissingSize() throws ZipException {
        byte[] data = new byte[2 * DWORD - WORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(20L);
        ZipLong diskStart = new ZipLong(12345L);
        byte[] data = new byte[3 * DWORD + WORD];
        System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, 3 * DWORD, WORD);
        }
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(size, field.size);
        assertEquals(compressedSize, field.compressedSize);
        if (relativeHeaderOffset != null) {
            assertEquals(relativeHeaderOffset, field.relativeHeaderOffset);
        }
        if (diskStart != null) {
            assertEquals(diskStart, field.diskStart);
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithNoSizes() throws ZipException {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithOneMissingSize() throws ZipException {
        byte[] data = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testReparseCentralDirectoryDataWithAllFields() throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(20L);
        ZipLong diskStart = new ZipLong(12345L);
        byte[] rawData = new byte[3 * DWORD + WORD];
        System.arraycopy(size.getBytes(), 0, rawData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, rawData, DWORD, DWORD);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, rawData, 2 * DWORD, DWORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, rawData, 3 * DWORD, WORD);
        }
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(rawData, 0, rawData.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, field.size);
        assertEquals(compressedSize, field.compressedSize);
        if (relativeHeaderOffset != null) {
            assertEquals(relativeHeaderOffset, field.relativeHeaderOffset);
        }
        if (diskStart != null) {
            assertEquals(diskStart, field.diskStart);
        }
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingSizes() throws ZipException {
        byte[] rawData = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(rawData, 0, rawData.length);
        field.reparseCentralDirectoryData(true, false, true, true);
    }

    private void addSizesCheck(byte[] data, byte[] size1, byte[] size2) {
        assertArrayEquals(size1, extractBytes(data, 0, DWORD));
        assertArrayEquals(size2, extractBytes(data, DWORD, DWORD));
    }

    private void addOffsetCheck(byte[] data, byte[] offset, int expectedLength) {
        assertArrayEquals(extractBytes(data, 2 * DWORD, DWORD), offset);
        assertEquals(expectedLength, offset.length);
    }

    private void addDiskStartCheck(byte[] data, byte[] start, int expectedLength) {
        assertArrayEquals(extractBytes(data, 3 * DWORD, WORD), start);
        assertEquals(expectedLength, start.length);
    }

    private byte[] extractBytes(byte[] data, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(data, offset, result, 0, length);
        return result;
    }
}