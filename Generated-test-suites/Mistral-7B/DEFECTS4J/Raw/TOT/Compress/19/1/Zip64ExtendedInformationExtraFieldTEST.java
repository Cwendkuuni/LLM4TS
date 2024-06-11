import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidZip64ExtendedInformationExtraField() {
        ZipShort headerId = new ZipShort(0x0001);
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStart = new ZipLong((long) 5678);

        Zip64ExtendedInformationExtraField zip64ExtraField =
                new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                        relativeHeaderOffset, diskStart);

        Assert.assertEquals(headerId, zip64ExtraField.getHeaderId());

        Assert.assertEquals(originalSize, zip64ExtraField.getSize());
        Assert.assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, zip64ExtraField.getDiskStartNumber());

        byte[] localFileData = zip64ExtraField.getLocalFileDataData();
        Assert.assertEquals(localFileData.length, 2 * ZipConstants.DWORD);
        Assert.assertArrayEquals(originalSize.getBytes(), localFileData, 0, 8);
        Assert.assertArrayEquals(compressedSize.getBytes(), localFileData, 8, 8);

        byte[] centralDirectoryData = zip64ExtraField.getCentralDirectoryData();
        Assert.assertEquals(centralDirectoryData.length,
                originalSize.getValue() + compressedSize.getValue() +
                        relativeHeaderOffset.getValue() + diskStart.getValue() +
                        (relativeHeaderOffset != null ? DWORD : 0) +
                        (diskStart != null ? WORD : 0));

        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                centralDirectoryData.length);
        Assert.assertEquals(originalSize, zip64ExtraField.getSize());
        Assert.assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
        Assert.assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testEmptyZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64ExtraField =
                new Zip64ExtendedInformationExtraField();
        byte[] localFileData = zip64ExtraField.getLocalFileDataData();
        Assert.assertArrayEquals(new byte[0], localFileData);
        byte[] centralDirectoryData = zip64ExtraField.getCentralDirectoryData();
        Assert.assertArrayEquals(new byte[0], centralDirectoryData);
    }

    @Test(expected = ZipException.class)
    public void testInvalidZip64ExtendedInformationExtraField() {
        ZipShort headerId = new ZipShort(0x0001);
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = null;
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStart = new ZipLong((long) 5678);

        Zip64ExtendedInformationExtraField zip64ExtraField =
                new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                        relativeHeaderOffset, diskStart);
    }

    @Test(expected = ZipException.class)
    public void testInvalidLocalFileDataInZip64ExtendedInformationExtraField() {
        ZipShort headerId = new ZipShort(0x0001);
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        byte[] localFileData = new byte[2 * ZipConstants.DWORD - 2];

        Zip64ExtendedInformationExtraField zip64ExtraField =
                new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test(expected = ZipException.class)
    public void testInvalidCentralDirectoryDataInZip64ExtendedInformationExtraField() {
        ZipShort headerId = new ZipShort(0x0001);
        byte[] centralDirectoryData = new byte[2 * ZipConstants.DWORD];

        Zip64ExtendedInformationExtraField zip64ExtraField =
                new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                centralDirectoryData.length);
    }
}