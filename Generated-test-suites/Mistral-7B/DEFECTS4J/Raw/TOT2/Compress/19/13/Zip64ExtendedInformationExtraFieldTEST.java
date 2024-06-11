import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithBothSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x100), new ZipEightByteInteger(0x80));
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(0x100, actualSize.getValue());
        assertEquals(0x80, actualCompressedSize.getValue());
    }

    @Test
    public void testNewInstanceWithOnlyCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            null, new ZipEightByteInteger(0x80));
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(0x80, actualCompressedSize.getValue());
        assertNull(extraField.getSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithOnlySize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x100), null);
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(0x100, actualSize.getValue());
        assertNull(actualCompressedSize);
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x100), new ZipEightByteInteger(0x80),
            new ZipEightByteInteger(0x30), new ZipLong(0x400));
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        ZipEightByteInteger actualRelativeHeaderOffset = extraField.getRelativeHeaderOffset();
        ZipLong actualDiskStart = extraField.getDiskStartNumber();
        assertEquals(0x100, actualSize.getValue());
        assertEquals(0x80, actualCompressedSize.getValue());
        assertEquals(0x30, actualRelativeHeaderOffset.getValue());
        assertEquals(0x400, actualDiskStart.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullSize() {
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(0x80));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullCompressedSize() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(0x100), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithOnlySize() {
        byte[] data = new byte[2 * DWORD];
        new ZipShort(0x0001).writeBytes(data);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 2 * DWORD);
        assertEquals(new ZipEightByteInteger(0x100), extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithOnlyCompressedSize() {
        byte[] data = new byte[2 * DWORD];
        new ZipShort(0x0001).writeBytes(data);
        new ZipEightByteInteger(0x80).writeBytes(data, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 2 * DWORD);
        assertNull(extraField.getSize());
        assertEquals(new ZipEightByteInteger(0x80), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithNoSizeValues() {
        byte[] data = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, DWORD);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] data = new byte[3 * DWORD + WORD];
        new ZipShort(0x0001).writeBytes(data);
        new ZipEightByteInteger(0x100).writeBytes(data, DWORD);
        new ZipEightByteInteger(0x80).writeBytes(data, 2 * DWORD);
        new ZipEightByteInteger(0x30).writeBytes(data, 3 * DWORD);
        new ZipLong(0x400).writeBytes(data, 4 * DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 8 * DWORD);
        assertEquals(new ZipEightByteInteger(0x100), extraField.getSize());
        assertEquals(new ZipEightByteInteger(0x80), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0x30), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x400), extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlySize() {
        byte[] data = new byte[3 * DWORD];
        new ZipShort(0x0001).writeBytes(data);
        new ZipEightByteInteger(0x100).writeBytes(data, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 3 * DWORD);
        assertEquals(new ZipEightByteInteger(0x100), extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithNoSizeValues() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 2 * DWORD);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyCompressedSize() {
        byte[] data = new byte[4 * DWORD];
        new ZipShort(0x0001).writeBytes(data);
        new ZipEightByteInteger(0x80).writeBytes(data, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 4 * DWORD);
        assertNull(extraField.getSize());
        assertEquals(new ZipEightByteInteger(0x80), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyRelativeHeaderOffset() {
        byte[] data = new byte[4 * DWORD + WORD];
        new ZipShort(0x0001).writeBytes(data);
        new ZipEightByteInteger(0x100).writeBytes(data, DWORD);
        new ZipEightByteInteger(0x80).writeBytes(data, 2 * DWORD);
        new ZipEightByteInteger(0x30).writeBytes(data, 3 * DWORD);
        new ZipShort(0xff).writeBytes(data, 4 * DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 5 * DWORD);
        assertEquals(new ZipEightByteInteger(0x100), extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0x30), extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStart() {
        byte[] data = new byte[4 * DWORD];
        new ZipShort(0x0001).writeBytes(data);
        new ZipEightByteInteger(0x100).writeBytes(data, DWORD);
        new ZipEightByteInteger(0x80).writeBytes(data, 2 * DWORD);
        new ZipLong(0x400).writeBytes(data, 3 * DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 4 * DWORD);
        assertEquals(new ZipEightByteInteger(0x100), extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x400), extraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x100), new ZipEightByteInteger(0x80));
        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x100), new ZipEightByteInteger(0x80),
            new ZipEightByteInteger(0x30), new ZipLong(0x400));
        byte[] expectedData = new byte[3 * DWORD + WORD];
        int offset = addSizes(expectedData);
        System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0,
                         expectedData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(extraField.getDiskStart().getBytes(), 0,
                         expectedData, offset, WORD);
        byte[] actualData = extraField.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
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
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, offset, WORD);
            offset += WORD;
        }
        return offset;
    }

    private ZipEightByteInteger size;
    private ZipEightByteInteger compressedSize;
    private ZipEightByteInteger relativeHeaderOffset;
    private ZipLong diskStart;
}