import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(HEADER_ID, field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getLocalFileDataLength().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthNullSizesThrowsException() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(null);
        field.setCompressedSize(null);
        field.getLocalFileDataLength();
    }

    @Test
    public void testGetLocalFileDataLengthBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        assertEquals(2 * DWORD, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLengthNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLengthBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        assertEquals(2 * DWORD + DWORD + DWORD + WORD, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLengthOnlyCompressedSizePresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            null, new ZipEightByteInteger(5));
        assertEquals(DWORD, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLengthOnlySizePresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(10), null);
        assertEquals(2 * DWORD, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataDataNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getLocalFileDataData());
    }

    @Test
    public void testGetLocalFileDataDataBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        byte[] data = field.getLocalFileDataData();
        assertEquals(2 * DWORD, data.length);
        assertEquals(10, ZipShort.readLittleEndian(data, 0).getValue());
        assertEquals(5, ZipShort.readLittleEndian(data, 2 * DWORD).getValue());
    }

    @Test
    public void testGetCentralDirectoryDataNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getCentralDirectoryData());
    }

    @Test
    public void testGetCentralDirectoryDataBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        byte[] data = field.getCentralDirectoryData();
        assertEquals(2 * DWORD + DWORD + DWORD + WORD, data.length);
        assertEquals(10, new ZipEightByteInteger(data).getValue().intValue());
        assertEquals(5, new ZipEightByteInteger(data, 4 * DWORD).getValue().intValue());
    }

    @Test
    public void testParseFromLocalFileDataNullBuffer() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(NullPointerException.class, () -> field.parseFromLocalFileData(null, 0, 0));
    }

    @Test
    public void testParseFromLocalFileDataInvalidLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[2 * DWORD];
        assertThrows(IllegalArgumentException.class, () -> field.parseFromLocalFileData(buffer, 0, 3 * DWORD));
    }

    @Test
    public void testParseFromLocalFileDataBothSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[2 * DWORD];
        System.arraycopy(new byte[]{0x00, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04,
            0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08}, 0, buffer, 0, 2 * DWORD);
        field.parseFromLocalFileData(buffer, 0, 2 * DWORD);
        assertEquals(10, field.getSize().getValue());
        assertEquals(5, field.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryDataNullBuffer() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(NullPointerException.class, () -> field.parseFromCentralDirectoryData(null, 0, 0));
    }

    @Test
    public void testParseFromCentralDirectoryDataValidLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[3 * DWORD];
        field.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataOnlySizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x02, 0x00, 0x01, 0x00, 0x03, 0x00, 0x05,
            0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09};
        field.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        assertEquals(new ZipEightByteInteger(1), field.getSize());
        assertEquals(new ZipEightByteInteger(5), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataOnlyOffsetPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x02, 0x00, 0x03, 0x01, 0x00, 0x00, 0x04};
        field.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new byte[]{0x03, 0x00, 0x00, 0x04}), field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataAllPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x02, 0x00, 0x01, 0x00, 0x03, 0x00, 0x04,
            0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x01, 0x09, 0x02, 0x0a};
        field.parseFromCentralDirectoryData(buffer, 0, 3 * DWORD);
        assertEquals(new ZipEightByteInteger(new byte[]{0x01, 0x09, 0x00, 0x0a}), field.getSize());
        assertEquals(new ZipEightByteInteger(new byte[]{0x05, 0x00, 0x00, 0x06}), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new byte[]{0x03, 0x00, 0x00, 0x04}), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(new byte[]{0x01, 0x00, 0x00, 0x08}), field.getDiskStartNumber());
    }
}