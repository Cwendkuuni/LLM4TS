import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(HEADER_ID, field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthWithBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        assertEquals(new ZipShort(2 * DWORD), field.getLocalFileDataLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithOnlyOneSizePresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10));
        field.getLocalFileDataLength();
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5),
                new ZipEightByteInteger(1), new ZipLong(2));
        assertEquals(new ZipShort((1 * DWORD) + (1 * DWORD) + (1 * DWORD) + (2 * WORD)),
                field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataDataWithBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        byte[] data = field.getLocalFileDataData();
        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        assertArrayEquals(expectedData, data);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5),
                new ZipEightByteInteger(1), new ZipLong(2));
        byte[] data = field.getCentralDirectoryData();
        byte[] expectedData = new byte[3 * DWORD + WORD];
        int off = addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, off, WORD);
        }
        assertArrayEquals(expectedData, data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithNoSizesPresent() {
        byte[] buffer = new byte[0];
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(buffer, 0, 0);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithOnlyOneSizePresent() {
        byte[] buffer = new byte[DWORD];
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(buffer, 0, DWORD);
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizesPresent() {
        byte[] buffer = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, 2 * DWORD);
        assertEquals(new ZipEightByteInteger(10), field.size);
        assertEquals(new ZipEightByteInteger(5), field.compressedSize);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFieldsPresent() {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD + WORD);
        assertEquals(new ZipEightByteInteger(10), field.size);
        assertEquals(new ZipEightByteInteger(5), field.compressedSize);
        assertEquals(new ZipEightByteInteger(1), field.relativeHeaderOffset);
        assertEquals(new ZipLong(2), field.diskStart);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlySizeFieldPresent() {
        byte[] buffer = new byte[DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, DWORD);
        assertNull(field.compressedSize);
        assertNull(field.relativeHeaderOffset);
        assertNull(field.diskStart);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyRelativeHeaderOffsetAndDiskStartPresent() {
        byte[] buffer = new byte[DWORD + WORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, DWORD + WORD);
        assertNull(field.size);
        assertNull(field.compressedSize);
        assertEquals(new ZipEightByteInteger(1), field.relativeHeaderOffset);
        assertEquals(new ZipLong(2), field.diskStart);
    }

    private void addSizes(byte[] data) {
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        }
    }
}