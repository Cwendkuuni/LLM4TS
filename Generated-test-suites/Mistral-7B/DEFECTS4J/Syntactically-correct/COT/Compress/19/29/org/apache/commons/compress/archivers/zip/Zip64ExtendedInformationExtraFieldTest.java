import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        Assert.assertEquals(size, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort((short) 0), extraField.getLocalFileDataLength());

        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        Assert.assertEquals(new ZipShort((short) (2 * DWORD)), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort((short) 0), extraField.getCentralDirectoryLength());

        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((short) 0x11223344));
        extraField.setDiskStart(new ZipLong((short) 0x55667788));
        Assert.assertEquals(new ZipShort((short) ((2 * DWORD) + DWORD + DWORD + WORD)), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(Zip64ExtendedInformationExtraField.EMPTY, extraField.getLocalFileDataData());

        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = extraField.getLocalFileDataData();
        Assert.assertEquals(2 * DWORD, data.length);
        Assert.assertArrayEquals(size.getBytes(), data, 0, 0, 4);
        Assert.assertArrayEquals(compressedSize.getBytes(), data, 4, 4, 4);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(Zip64ExtendedInformationExtraField.EMPTY, extraField.getCentralDirectoryData());

        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((short) 0x11223344));
        extraField.setDiskStart(new ZipLong((short) 0x55667788));
        byte[] data = extraField.getCentralDirectoryData();
        Assert.assertEquals(extraField.getCentralDirectoryLength().getValue(), data.length);
        int offset = 0;
        if (size != null) {
            Assert.assertArrayEquals(size.getBytes(), data, offset, 0, 4);
            offset += 4;
        }
        if (compressedSize != null) {
            Assert.assertArrayEquals(compressedSize.getBytes(), data, offset, 0, 4);
            offset += 4;
        }
        if (relativeHeaderOffset != null) {
            Assert.assertArrayEquals(relativeHeaderOffset.getBytes(), data, offset, 0, 4);
            offset += 4;
        }
        if (diskStart != null) {
            Assert.assertArrayEquals(diskStart.getBytes(), data, offset, 0, 2);
        }
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01,
            (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
            (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
            (byte) 0x0e, (byte) 0x0f, (byte) 0x10, (byte) 0x11};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.assertEquals(new ZipEightByteInteger((short) 0x1120a001), extraField.getSize());
        Assert.assertEquals(new ZipEightByteInteger((short) 0x0a019001), extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithoutBothSizes() throws ZipException {
        byte[] localFileData = new byte[]{(byte) 0x00, (byte) 0x01,
            (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
            (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.fail();
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01,
            (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
            (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
            (byte) 0x0e, (byte) 0x0f, (byte) 0x10, (byte) 0x11,
            (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
            (byte) 0x16, (byte) 0x17};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(new ZipEightByteInteger((short) 0x1120a001), extraField.getSize());
        Assert.assertEquals(new ZipEightByteInteger((short) 0x0a019001), extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithNoSizes() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01,
            (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
            (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
            (byte) 0x0e, (byte) 0x0f};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(null, extraField.getSize());
        Assert.assertEquals(null, extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyRelativeHeaderOffsetAndDiskStartNumber() throws ZipException {
        byte[] centralDirectoryData = new byte[]{(byte) 0x00, (byte) 0x01,
            (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
            (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09,
            (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
            (byte) 0x0e, (byte) 0x0f, (byte) 0x10, (byte) 0x11,
            (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
            (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19,
            (byte) 0x20, (byte) 0x21};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(null, extraField.getSize());
        Assert.assertEquals(null, extraField.getCompressedSize());
        Assert.assertEquals(new ZipLong((short) 0x19180706), extraField.getDiskStart());
    }
}