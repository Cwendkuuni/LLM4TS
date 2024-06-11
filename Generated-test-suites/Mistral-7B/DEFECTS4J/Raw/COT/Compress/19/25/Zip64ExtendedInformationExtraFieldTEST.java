import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = field.getHeaderId();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        ZipShort expected = new ZipShort(2 * DWORD);
        ZipShort actual = field.getLocalFileDataLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithoutSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        ZipShort actual = field.getLocalFileDataLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLengthWithSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5),
                null, null);
        ZipShort expected = new ZipShort(2 * DWORD + DWORD + DWORD);
        ZipShort actual = field.getCentralDirectoryLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLengthWithoutSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0);
        ZipShort actual = field.getCentralDirectoryLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataDataWithSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(10), new ZipEightByteInteger(5));
        byte[] expected = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04,
                (byte) 0x06, (byte) 0x08, (byte) 0x0A, (byte) 0x0C, (byte) 0x0E,
                (byte) 0x10, (byte) 0x12, (byte) 0x14, (byte) 0x16, (byte) 0x18,
                (byte) 0x1A};
        byte[] actual = field.getLocalFileDataData();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataDataWithoutSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] actual = field.getLocalFileDataData();
        Assert.assertArrayEqual(new byte[0], actual);
    }

    @Test
    public void testParseFromLocalFileDataWithSizes() throws ZipException {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04,
                (byte) 0x06, (byte) 0x08, (byte) 0x0A, (byte) 0x0C, (byte) 0x0E,
                (byte) 0x10, (byte) 0x12, (byte) 0x14, (byte) 0x16};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        ZipEightByteInteger sizeExpected = new ZipEightByteInteger(10);
        ZipEightByteInteger compressedSizeExpected = new ZipEightByteInteger(5);
        ZipEightByteInteger sizeActual = field.getSize();
        ZipEightByteInteger compressedSizeActual = field.getCompressedSize();
        Assert.assertEquals(sizeExpected, sizeActual);
        Assert.assertEquals(compressedSizeExpected, compressedSizeActual);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithoutBothSizes() throws ZipException {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        Assert.fail(); // should throw ZipException
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithoutSizes() throws ZipException {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04,
                (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        Assert.fail(); // should throw ZipException
    }

    @Test
    public void testParseFromCentralDirectoryDataWithSizes() throws ZipException {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04,
                (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
                (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E,
                (byte) 0x0F};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        ZipEightByteInteger sizeExpected = new ZipEightByteInteger(10);
        ZipEightByteInteger compressedSizeExpected = new ZipEightByteInteger(5);
        ZipEightByteInteger sizeActual = field.getSize();
        ZipEightByteInteger compressedSizeActual = field.getCompressedSize();
        Assert.assertEquals(sizeExpected, sizeActual);
        Assert.assertEquals(compressedSizeExpected, compressedSizeActual);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOffsets() throws ZipException {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04,
                (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
                (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E,
                (byte) 0x0F, (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
                (byte) 0x14};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        ZipEightByteInteger sizeExpected = new ZipEightByteInteger(10);
        ZipEightByteInteger compressedSizeExpected = new ZipEightByteInteger(5);
        ZipEightByteInteger relativeHeaderOffsetExpected = new ZipEightByteInteger(11);
        ZipLong diskStartNumberExpected = new ZipLong(12);
        ZipEightByteInteger sizeActual = field.getSize();
        ZipEightByteInteger compressedSizeActual = field.getCompressedSize();
        ZipEightByteInteger relativeHeaderOffsetActual = field.getRelativeHeaderOffset();
        ZipLong diskStartNumberActual = field.getDiskStartNumber();
        Assert.assertEquals(sizeExpected, sizeActual);
        Assert.assertEquals(compressedSizeExpected, compressedSizeActual);
        Assert.assertEquals(relativeHeaderOffsetExpected, relativeHeaderOffsetActual);
        Assert.assertEquals(diskStartNumberExpected, diskStartNumberActual);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithDiskStartNumberOnly() throws ZipException {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x04,
                (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
                (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E,
                (byte) 0x0F, (byte) 0x10};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        ZipLong diskStartNumberExpected = new ZipLong(10);
        ZipLong diskStartNumberActual = field.getDiskStartNumber();
        Assert.assertEquals(diskStartNumberExpected, diskStartNumberActual);
    }
}