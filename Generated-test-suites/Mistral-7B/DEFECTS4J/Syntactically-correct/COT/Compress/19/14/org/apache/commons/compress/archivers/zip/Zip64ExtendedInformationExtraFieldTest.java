import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = extraField.getHeaderId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithNullSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertNull(extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLengthWithBothSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), new ZipEightByteInteger(0x8));
        ZipShort expected = new ZipShort(2 * DWORD);
        ZipShort actual = extraField.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithOnlyCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                null, new ZipEightByteInteger(0x8));
        extraField.getLocalFileDataLength();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithOnlySize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), null);
        extraField.getLocalFileDataLength();
    }

    @Test
    public void testGetCentralDirectoryLengthWithNullSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        ZipShort actual = extraField.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLengthWithAllSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), new ZipEightByteInteger(0x8),
                null, null);
        ZipShort expected = new ZipShort(2 * DWORD + DWORD);
        ZipShort actual = extraField.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCentralDirectoryLengthWithMissingSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), null, null, null);
        extraField.getCentralDirectoryLength();
    }

    @Test
    public void testGetLocalFileDataDataWithNullSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] expected = new byte[0];
        byte[] actual = extraField.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataDataWithBothSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), new ZipEightByteInteger(0x8));
        byte[] expected = new byte[]{(byte) 0x08, (byte) 0x02,
                (byte) 0x00, (byte) 0x10, (byte) 0x02, (byte) 0x00,
                (byte) 0x18, (byte) 0x00, (byte) 0x18, (byte) 0x00,
                (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x04};
        byte[] actual = extraField.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseFromLocalFileDataWithNullBuffer() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(null, 0, 0);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
        extraField.parseFromLocalFileData(buffer, 0, 3);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithIncompleteData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{(byte) 0x08, (byte) 0x02,
                (byte) 0x00, (byte) 0x10};
        extraField.parseFromLocalFileData(buffer, 0, 7);
    }

    @Test
    public void testParseFromLocalFileDataWithCompleteData() {
        Zip64ExtendedInformationExtraField expected = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), new ZipEightByteInteger(0x8));
        byte[] buffer = new byte[]{(byte) 0x08, (byte) 0x02,
                (byte) 0x00, (byte) 0x10, (byte) 0x08, (byte) 0x02,
                (byte) 0x00, (byte) 0x08, (byte) 0x02, (byte) 0x00,
                (byte) 0x18, (byte) 0x00, (byte) 0x18, (byte) 0x00};
        Zip64ExtendedInformationExtraField actual = new Zip64ExtendedInformationExtraField();
        actual.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithNullBuffer() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(null, 0, 0);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x01};
        extraField.parseFromCentralDirectoryData(buffer, 0, 1);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithEmptyData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[0];
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x08, 0x00, 0x08, 0x00, 0x08, 0x00,
                0x08, 0x00, 0x08, 0x00, 0x00, 0x04, 0x00, 0x04};
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertTrue(extraField.hasUncompressedSize());
        assertTrue(extraField.hasCompressedSize());
        assertFalse(extraField.hasRelativeHeaderOffset());
        assertFalse(extraField.hasDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithFullData() {
        Zip64ExtendedInformationExtraField expected = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), new ZipEightByteInteger(0x8),
                new ZipEightByteInteger(0x20), new ZipLong(0x30));
        byte[] buffer = new byte[]{0x08, 0x02, 0x00, 0x10, 0x08, 0x02,
                0x00, 0x08, 0x02, 0x00, 0x18, 0x00, 0x30, 0x00};
        Zip64ExtendedInformationExtraField actual = new Zip64ExtendedInformationExtraField();
        actual.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testReparseCentralDirectoryDataWithNullData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.reparseCentralDirectoryData(false, false, false, false);
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoSizeData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[]{0x08, 0x02, 0x00, 0x18, 0x00, 0x18,
                0x00, 0x00, 0x04, 0x00, 0x04};
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(false, true, true, true);
        assertFalse(extraField.hasUncompressedSize());
        assertFalse(extraField.hasCompressedSize());
        assertTrue(extraField.hasRelativeHeaderOffset());
        assertFalse(extraField.hasDiskStart());
    }

    @Test
    public void testReparseCentralDirectoryDataWithOnlySizeData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[]{0x08, 0x02, 0x00, 0x10, 0x08, 0x02,
                0x00, 0x08, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertTrue(extraField.hasUncompressedSize());
        assertTrue(extraField.hasCompressedSize());
        assertFalse(extraField.hasRelativeHeaderOffset());
        assertFalse(extraField.hasDiskStart());
    }

    @Test
    public void testReparseCentralDirectoryDataWithAllData() {
        Zip64ExtendedInformationExtraField expected = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(0x10), new ZipEightByteInteger(0x8),
                new ZipEightByteInteger(0x20), new ZipLong(0x30));
        byte[] data = new byte[]{0x08, 0x02, 0x00, 0x10, 0x08, 0x02,
                0x00, 0x08, 0x02, 0x00, 0x18, 0x00, 0x30, 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(expected, extraField);
    }
}