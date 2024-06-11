import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthWithNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0), field.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLengthWithSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            ZipEightByteInteger.valueOf(10), ZipEightByteInteger.valueOf(5));
        assertEquals(ZipShort.valueOf(2 * ZipConstants.DWORD), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            ZipEightByteInteger.valueOf(10), ZipEightByteInteger.valueOf(5), null, null);
        assertEquals(ZipShort.valueOf(2 * ZipConstants.DWORD + ZipConstants.DWORD), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataDataWithNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getLocalFileDataData());
    }

    @Test
    public void testGetLocalFileDataDataWithSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            ZipEightByteInteger.valueOf(10), ZipEightByteInteger.valueOf(5));
        byte[] data = field.getLocalFileDataData();
        assertEquals(2 * ZipConstants.DWORD, data.length);
        assertEquals(ZipEightByteInteger.valueOf(10).getBytes(), Arrays.copyOfRange(data, 0, DWORD));
        assertEquals(ZipEightByteInteger.valueOf(5).getBytes(), Arrays.copyOfRange(data, DWORD, 2 * DWORD));
    }

    @Test
    public void testParseFromLocalFileDataWithNullBuffer() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(IllegalArgumentException.class, () -> field.parseFromLocalFileData(null, 0, 0));
    }

    @Test
    public void testParseFromLocalFileDataWithCorrectData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {
            0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2,
            0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 4
        };
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(ZipEightByteInteger.valueOf(1), field.getSize());
        assertEquals(ZipEightByteInteger.valueOf(2), field.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithLessData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMalformedData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithNullBuffer() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(IllegalArgumentException.class, () -> field.parseFromCentralDirectoryData(null, 0, 0));
    }

    @Test
    public void testParseFromCentralDirectoryDataWithEmptyData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[0];
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithNoSizeInformation() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithBothSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(ZipEightByteInteger.valueOf(1), field.getSize());
        assertEquals(ZipEightByteInteger.valueOf(2), field.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 5};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(ZipEightByteInteger.valueOf(1), field.getSize());
        assertEquals(ZipEightByteInteger.valueOf(2), field.getCompressedSize());
        assertEquals(ZipEightByteInteger.valueOf(5), field.getRelativeHeaderOffset());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 5, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(ZipEightByteInteger.valueOf(1), field.getSize());
        assertEquals(ZipEightByteInteger.valueOf(2), field.getCompressedSize());
        assertEquals(ZipEightByteInteger.valueOf(5), field.getRelativeHeaderOffset());
        assertEquals(ZipLong.valueOf(6), field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNullData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(IllegalArgumentException.class, () -> field.reparseCentralDirectoryData(false, false, false, false));
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoSizeInformation() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertThrows(ZipException.class, () -> field.reparseCentralDirectoryData(false, false, false, false));
    }

    @Test
    public void testReparseCentralDirectoryDataWithBothSizesAndRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 5, 0, 0, 0, 6};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        field.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(ZipEightByteInteger.valueOf(1), field.getSize());
        assertEquals(ZipEightByteInteger.valueOf(2), field.getCompressedSize());
        assertEquals(ZipEightByteInteger.valueOf(5), field.getRelativeHeaderOffset());
    }

    @Test
    public void testReparseCentralDirectoryDataWithBothSizesAndDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 5, 0, 0, 0, 6, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        field.reparseCentralDirectoryData(true, true, false, true);
        assertEquals(ZipEightByteInteger.valueOf(1), field.getSize());
        assertEquals(ZipEightByteInteger.valueOf(2), field.getCompressedSize());
        assertEquals(ZipEightByteInteger.valueOf(5), field.getRelativeHeaderOffset());
        assertEquals(ZipLong.valueOf(7), field.getDiskStartNumber());
    }
}