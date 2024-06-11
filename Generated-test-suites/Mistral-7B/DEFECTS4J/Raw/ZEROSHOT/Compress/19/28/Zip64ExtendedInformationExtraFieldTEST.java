import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        testField.setSize(size);
        testField.setCompressedSize(compressedSize);

        ZipShort expectedLength = new ZipShort(2 * ZipConstants.DWORD);
        ZipShort actualLength = testField.getLocalFileDataLength();

        Assert.assertEquals("Unexpected local file data length.", expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(1L);

        testField.setSize(size);
        testField.setCompressedSize(compressedSize);
        testField.setRelativeHeaderOffset(relativeHeaderOffset);
        testField.setDiskStartNumber(diskStart);

        ZipShort expectedLength = new ZipShort((size.getValue() + compressedSize.getValue())
                + (relativeHeaderOffset.getValue() + diskStart.getValue()));
        ZipShort actualLength = testField.getCentralDirectoryLength();

        Assert.assertEquals("Unexpected central directory length.", expectedLength, actualLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        testField.setSize(size);
        testField.setCompressedSize(compressedSize);

        byte[] expectedData = new byte[2 * ZipConstants.DWORD];
        testField.addSizes(expectedData);

        byte[] actualData = testField.getLocalFileDataData();

        Assert.assertArrayEquals("Unexpected local file data.", expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(1L);

        testField.setSize(size);
        testField.setCompressedSize(compressedSize);
        testField.setRelativeHeaderOffset(relativeHeaderOffset);
        testField.setDiskStartNumber(diskStart);

        byte[] expectedData = new byte[testField.getCentralDirectoryLength().getValue()];
        testField.addSizes(expectedData);

        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, testField.getLocalFileDataLength().getValue(), DipConstants.DWORD);
        }

        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, testField.getLocalFileDataLength().getValue() + testField.getSizes().length, DipConstants.WORD);
        }

        byte[] actualData = testField.getCentralDirectoryData();

        Assert.assertArrayEquals("Unexpected central directory data.", expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        System.arraycopy(size.getBytes(), 0, localFileData, 0, DipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, DipConstants.DWORD, DipConstants.DWORD);

        testField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        Assert.assertEquals("Unexpected size after parsing local file data.", size, testField.getSize());
        Assert.assertEquals("Unexpected compressed size after parsing local file data.", compressedSize, testField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DipConstants.DWORD, DipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * ZipConstants.DWORD, DipConstants.DWORD);

        testField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assert.assertEquals("Unexpected size after parsing central directory data.", size, testField.getSize());
        Assert.assertEquals("Unexpected compressed size after parsing central directory data.", compressedSize, testField.getCompressedSize());
        Assert.assertEquals("Unexpected relative header offset after parsing central directory data.", relativeHeaderOffset, testField.getRelativeHeaderOffset());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() throws ZipException {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[2 * ZipConstants.DWORD - 1];

        testField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(1L);
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = false;

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DipConstants.DWORD, DipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * ZipConstants.DWORD, DipConstants.DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 3 * ZipConstants.DWORD, DipConstants.WORD);

        testField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        testField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);

        if (hasUncompressedSize) {
            Assert.assertEquals("Unexpected size after reparsing central directory data.", size, testField.getSize());
        }

        if (hasCompressedSize) {
            Assert.assertEquals("Unexpected compressed size after reparsing central directory data.", compressedSize, testField.getCompressedSize());
        }

        if (hasRelativeHeaderOffset) {
            Assert.assertEquals("Unexpected relative header offset after reparsing central directory data.", relativeHeaderOffset, testField.getRelativeHeaderOffset());
        }

        if (hasDiskStart) {
            Assert.assertEquals("Unexpected disk start number after reparsing central directory data.", diskStart, testField.getDiskStartNumber());
        }
    }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);

        testField.setSize(size);

        Assert.assertEquals("Unexpected size.", size.getValue(), testField.getSize().getValue());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        testField.setCompressedSize(compressedSize);

        Assert.assertEquals("Unexpected compressed size.", compressedSize.getValue(), testField.getCompressedSize().getValue());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);

        testField.setRelativeHeaderOffset(relativeHeaderOffset);

        Assert.assertEquals("Unexpected relative header offset.", relativeHeaderOffset.getValue(), testField.getRelativeHeaderOffset().getValue());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        ZipLong diskStart = new ZipLong(1L);

        testField.setDiskStartNumber(diskStart);

        Assert.assertEquals("Unexpected disk start number.", diskStart.getValue(), testField.getDiskStartNumber().getValue());
    }
}