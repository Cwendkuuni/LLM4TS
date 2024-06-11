import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = field.getHeaderId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithBothSizesPresent() {
        Zip64ExtendedInformationExtraField field =
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(100),
                                                   new ZipEightByteInteger(50));
        ZipShort expected = new ZipShort(2 * ZipConstants.DWORD);
        ZipShort actual = field.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithNoSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        ZipShort actual = field.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithOnlyOneSizePresent() {
        Zip64ExtendedInformationExtraField field =
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(100));
        field.getLocalFileDataLength();
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field =
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(100),
                                                   new ZipEightByteInteger(50),
                                                   new ZipEightByteInteger(20),
                                                   new ZipLong(3));
        ZipShort expected = new ZipShort((2 * ZipConstants.DWORD)
                                         + (ZipConstants.DWORD)
                                         + DWORD
                                         + WORD);
        ZipShort actual = field.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field =
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(100),
                                                   new ZipEightByteInteger(50),
                                                   new ZipEightByteInteger(20),
                                                   new ZipLong(3));
        byte[] expected = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x00,
                                     (byte) 0x00, (byte) 0x00, (byte) 0x64,
                                     (byte) 0x00, (byte) 0x01, 0x00, 0x00, 0x00,
                                     0x00, 0x00, 0x00, 0x00, 0x00, 0x32, 0x00,
                                     (byte) 0x00, (byte) 0x00, 0x00, 0x00, 0x00,
                                     0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                     0x00, 0x00, 0x00, 0x03, 0x00, 0x00};
        byte[] actual = field.getCentralDirectoryData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{
            0x00, 0x01, 0x00, 0x00, 0x00, 0x64, 0x00, 0x01, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x03, 0x00, 0x00};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        ZipEightByteInteger actualSize = field.getSize();
        ZipEightByteInteger expectedSize = new ZipEightByteInteger(100);
        assertEquals(expectedSize, actualSize);

        ZipEightByteInteger actualCompressedSize = field.getCompressedSize();
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50);
        assertEquals(expectedCompressedSize, actualCompressedSize);

        ZipEightByteInteger actualRelativeHeaderOffset = field.getRelativeHeaderOffset();
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(20);
        assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);

        ZipLong actualDiskStart = field.getDiskStartNumber();
        ZipLong expectedDiskStart = new ZipLong(3);
        assertEquals(expectedDiskStart, actualDiskStart);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidDataLength()
        throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, 0x00, 0x00};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData()
        throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, 0x00, 0x00, 0x00, 0x64, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x03, 0x01};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, 0x00, 0x00, 0x00, 0x64, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x03};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        ZipEightByteInteger actualSize = field.getSize();
        ZipEightByteInteger expectedSize = new ZipEightByteInteger(100);
        assertEquals(expectedSize, actualSize);

        ZipEightByteInteger actualCompressedSize = field.getCompressedSize();
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50);
        assertEquals(expectedCompressedSize, actualCompressedSize);

        ZipEightByteInteger actualRelativeHeaderOffset = field.getRelativeHeaderOffset();
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(20);
        assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);

        ZipLong actualDiskStart = field.getDiskStartNumber();
        ZipLong expectedDiskStart = new ZipLong(3);
        assertEquals(expectedDiskStart, actualDiskStart);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidDataLength()
        throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, 0x00, 0x00};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, 0x00, 0x00, 0x00, 0x64, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x03};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        field.reparseCentralDirectoryData(hasUncompressedSize,
                                          hasCompressedSize,
                                          hasRelativeHeaderOffset,
                                          hasDiskStart);
        ZipEightByteInteger actualSize = field.getSize();
        ZipEightByteInteger expectedSize = new ZipEightByteInteger(100);
        assertEquals(expectedSize, actualSize);

        ZipEightByteInteger actualCompressedSize = field.getCompressedSize();
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(50);
        assertEquals(expectedCompressedSize, actualCompressedSize);

        ZipEightByteInteger actualRelativeHeaderOffset = field.getRelativeHeaderOffset();
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(20);
        assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);

        ZipLong actualDiskStart = field.getDiskStartNumber();
        ZipLong expectedDiskStart = new ZipLong(3);
        assertEquals(expectedDiskStart, actualDiskStart);
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingSizes()
        throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, 0x00, 0x00, 0x00, 0x64, 0x00,
            0x01, 0x00, 0x00, 0x00, 0x00, 0x32};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = false;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        field.reparseCentralDirectoryData(hasUncompressedSize,
                                          hasCompressedSize,
                                          hasRelativeHeaderOffset,
                                          hasDiskStart);
    }
}