import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
    }

    @Test
    public void testNewInstanceWithoutCompressedSize() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(originalSize, null);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertNull(testObject.getCompressedSize());
    }

    @Test
    public void testNewInstanceWithoutOriginalSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(null, compressedSize);

        Assertions.assertNull(testObject.getSize());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(12345L);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                relativeHeaderOffset, diskStart);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
        Assertions.assertEquals(relativeHeaderOffset.getValue(), testObject.getRelativeHeaderOffset().getValue());
        Assertions.assertEquals(diskStart.getValue(), testObject.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);

        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, ZipConstants.DWORD, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromLocalFileData(localFileData, 0, localFileData.length);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromLocalFileDataWithoutBothSizes() {
        byte[] localFileData = new byte[0];

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromLocalFileData(localFileData, 0, localFileData.length);

        Assertions.assertNull(testObject.getSize());
        Assertions.assertNull(testObject.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD * 2, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStartNumber() {
        byte[] centralDirectoryData = new byte[ZipConstants.DWORD];
        ZipLong diskStart = new ZipLong(12345L);

        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 0, ZipConstants.WORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assertions.assertNull(testObject.getSize());
        Assertions.assertNull(testObject.getCompressedSize());
        Assertions.assertEquals(diskStart.getValue(), testObject.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(12345L);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD * 2, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        testObject.reparseCentralDirectoryData(true, true, true, true);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
        Assertions.assertEquals(relativeHeaderOffset.getValue(), testObject.getRelativeHeaderOffset().getValue());
        Assertions.assertEquals(diskStart.getValue(), testObject.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutCompressedSize() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(12345L);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        testObject.reparseCentralDirectoryData(true, false, true, true);

        Assertions.assertEquals(originalSize.getValue(), testObject.getSize().getValue());
        Assertions.assertNull(testObject.getCompressedSize());
        Assertions.assertEquals(relativeHeaderOffset.getValue(), testObject.getRelativeHeaderOffset().getValue());
        Assertions.assertEquals(diskStart.getValue(), testObject.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutOriginalSize() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1L);
        ZipLong diskStart = new ZipLong(12345L);

        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        testObject.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        testObject.reparseCentralDirectoryData(false, true, true, true);

        Assertions.assertNull(testObject.getSize());
        Assertions.assertEquals(compressedSize.getValue(), testObject.getCompressedSize().getValue());
        Assertions.assertEquals(relativeHeaderOffset.getValue(), testObject.getRelativeHeaderOffset().getValue());
        Assertions.assertEquals(diskStart.getValue(), testObject.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutAllFields() {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipLong diskStart = new ZipLong(12345L);

        testObject.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        testObject.reparseCentralDirectoryData(false, false, false, true);

        Assertions.assertNull(testObject.getSize());
        Assertions.assertNull(testObject.getCompressedSize());
        Assertions.assertNull(testObject.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart.getValue(), testObject.getDiskStartNumber().getValue());
    }
}