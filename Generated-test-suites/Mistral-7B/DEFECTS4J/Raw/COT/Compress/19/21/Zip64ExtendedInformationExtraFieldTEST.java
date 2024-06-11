package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Zip64ExtendedInformationExtraField} class.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        Assert.assertEquals(size.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(0, extraField.getLocalFileDataLength().getValue());

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        Zip64ExtendedInformationExtraField extraFieldWithSize = new Zip64ExtendedInformationExtraField(size, null, null, null);
        Assert.assertEquals(2 * DWORD, extraFieldWithSize.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(0, extraField.getCentralDirectoryLength().getValue());

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(7890L);
        ZipLong diskStart = new ZipLong(11111L);
        Zip64ExtendedInformationExtraField extraFieldWithAllFields = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        Assert.assertEquals((size.getValue() + compressedSize.getValue() + relativeHeaderOffset.getValue() + diskStart.getValue()) / WORD * WORD, extraFieldWithAllFields.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = extraField.getLocalFileDataData();
        Assert.assertArrayEquals(new byte[0], data);

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField extraFieldWithSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] dataWithSizes = extraFieldWithSizes.getLocalFileDataData();
        Assert.assertEquals(2 * DWORD, dataWithSizes.length);
        ZipShort expectedId = new ZipShort(0x0001);
        Assert.assertEquals(expectedId.getValue(), ByteBuffer.wrap(dataWithSizes, 0, 2).getShort());
        ZipShort sizeLength = new ZipShort(2 * DWORD);
        Assert.assertEquals(sizeLength.getValue(), ByteBuffer.wrap(dataWithSizes, 2, 2).getShort());

        size = new ZipEightByteInteger(12345L);
        compressedSize = new ZipEightByteInteger(56789L);
        extraFieldWithSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        dataWithSizes = extraFieldWithSizes.getLocalFileDataData();
        Assert.assertEquals(2 * DWORD, dataWithSizes.length);
        Assert.assertArrayEquals(size.getBytes(), ByteBuffer.wrap(dataWithSizes, 0, DWORD).array());
        Assert.assertArrayEquals(compressedSize.getBytes(), ByteBuffer.wrap(dataWithSizes, DWORD, DWORD).array());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[2 * DWORD];
        ZipShort id = new ZipShort(0x0001);
        System.arraycopy(id.getBytes(), 0, data, 0, 2);
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        System.arraycopy(size.getBytes(), 0, data, 2, DWORD);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        System.arraycopy(compressedSize.getBytes(), 0, data, 6, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(size.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = new byte[DWORD];
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        byte[] centralDirectoryDataWithSize = new byte[3 * DWORD + DWORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryDataWithSize, 0, DWORD);
        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryDataWithSize, 0, centralDirectoryDataWithSize.length);
        Assert.assertEquals(size.getValue(), extraField.getSize().getValue());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStartNumber());

        size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(56789L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(7890L);
        ZipLong diskStart = new ZipLong(11111L);
        byte[] centralDirectoryDataWithAllFields = new byte[3 * DWORD + DWORD + WORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryDataWithAllFields, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryDataWithAllFields, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryDataWithAllFields, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryDataWithAllFields, 3 * DWORD, WORD);
        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryDataWithAllFields, 0, centralDirectoryDataWithAllFields.length);
        Assert.assertEquals(size.getValue(), extraField.getSize().getValue());
        Assert.assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        Assert.assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        Assert.assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }
}