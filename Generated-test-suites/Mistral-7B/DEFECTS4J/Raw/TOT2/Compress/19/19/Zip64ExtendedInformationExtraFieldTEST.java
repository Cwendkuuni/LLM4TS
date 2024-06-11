import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        ZipShort expectedHeaderId = ZipShort.valueOf((short) 0x0001);
        Assert.assertEquals(expectedHeaderId, extraField.getHeaderId());
        Assert.assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testParseLocalFileDataWithBothSizes() {
        byte[] localFileData = new byte[16];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        ZipShort expectedHeaderId = ZipShort.valueOf((short) 0x0001);

        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, localFileData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, offset, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assert.assertEquals(expectedHeaderId, extraField.getHeaderId());
        Assert.assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseLocalFileDataWithoutBothSizes() {
        byte[] localFileData = new byte[8];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseCentralDirectoryDataWithUncompressedSize() {
        byte[] centralDirectoryData = new byte[12];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x1000);
        ZipShort expectedHeaderId = ZipShort.valueOf((short) 0x0001);

        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(expectedHeaderId, extraField.getHeaderId());
        Assert.assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseCentralDirectoryDataWithoutTag() {
        byte[] centralDirectoryData = new byte[4];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testParseCentralDirectoryDataWithAllFields() {
        byte[] centralDirectoryData = new byte[24];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x20);
        ZipLong diskStart = new ZipLong(0x30);
        ZipShort expectedHeaderId = ZipShort.valueOf((short) 0x0001);

        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, offset + DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertEquals(expectedHeaderId, extraField.getHeaderId());
        Assert.assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        Assert.assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        Assert.assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[24];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        int offset = 0;
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x1000);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x20);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        ZipLong diskStart = new ZipLong(0x30);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, offset, WORD);

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        Assert.assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        Assert.assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        Assert.assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithUnmatchedLength() {
        byte[] centralDirectoryData = new byte[23];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort(DWORD).getValue(), extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(0x1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x20);
        ZipLong diskStart = new ZipLong(0x30);

        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        Assert.assertEquals(new ZipShort((short) (2 * DWORD + DWORD + DWORD + WORD)).getValue(), extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(new byte[0], extraField.getLocalFileDataData());

        ZipEightByteInteger size = new ZipEightByteInteger(0x1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] expectedData = new byte[2 * DWORD];
        extraField.addSizes(expectedData);
        Assert.assertArrayEquals(expectedData, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());

        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x800);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x20);
        ZipLong diskStart = new ZipLong(0x30);

        extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        byte[] expectedData = new byte[originalSize.getValue() + compressedSize.getValue() + relativeHeaderOffset.getValue() + diskStart.getValue()];
        extraField.addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, expectedData.length - WORD, WORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, expectedData.length - WORD, WORD);
        }
        Assert.assertArrayEquals(expectedData, extraField.getCentralDirectoryData());
    }
}