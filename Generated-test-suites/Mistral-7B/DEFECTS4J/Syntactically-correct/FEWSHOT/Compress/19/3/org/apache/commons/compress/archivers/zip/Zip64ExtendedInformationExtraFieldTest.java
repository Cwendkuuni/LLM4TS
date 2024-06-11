package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateZip64ExtendedInformationExtraField() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, zip64ExtraField.getSize());
        assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testCreateZip64ExtendedInformationExtraFieldWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStartNumber = new ZipLong(0x12345678L);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        assertEquals(size, zip64ExtraField.getSize());
        assertEquals(compressedSize, zip64ExtraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, zip64ExtraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateZip64ExtendedInformationExtraFieldWithNullSize() {
        ZipEightByteInteger size = null;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateZip64ExtendedInformationExtraFieldWithNullCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = null;
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        byte[] localFileData = new byte[10];
        try {
            new Zip64ExtendedInformationExtraField().parseFromLocalFileData(localFileData, 0, 5);
        } catch (ZipException e) {
            assertEquals("Zip64 extended information must contain both size values in the local file header.", e.getMessage());
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizeFields() {
        byte[] localFileData = new byte[8];
        try {
            new Zip64ExtendedInformationExtraField().parseFromLocalFileData(localFileData, 0, 8);
        } catch (ZipException e) {
            assertEquals("Zip64 extended information must contain both size values in the local file header.", e.getMessage());
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        byte[] centralDirectoryData = new byte[3];
        try {
            new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, 3);
        } catch (ZipException e) {
            assertEquals("central directory zip64 extended information extra field's length doesn't match central directory data. Expected length 24 but is 3", e.getMessage());
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithMissingFields() {
        byte[] centralDirectoryData = new byte[16];
        try {
            new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, 16);
        } catch (ZipException e) {
            assertEquals("central directory zip64 extended information extra field's length doesn't match central directory data. Expected length 24 but is 16", e.getMessage());
        }
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[24];
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, 24);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(2 * DWORD, zip64ExtraField.getLocalFileDataLength().getValue());

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField zip64ExtraFieldWithSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(2 * DWORD, zip64ExtraFieldWithSizes.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, zip64ExtraField.getCentralDirectoryLength().getValue());

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipExtendedInformationExtraField zip64ExtraFieldWithSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals((size != null ? DWORD : 0) + (compressedSize != null ? DWORD : 0), zip64ExtraFieldWithSizes.getCentralDirectoryLength().getValue());

        ZipEightByteInteger size2 = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize2 = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStartNumber = new ZipLong(0x12345678L);
        Zip64ExtendedInformationExtraField zip64ExtraFieldWithAllFields = new Zip64ExtendedInformationExtraField(size2, compressedSize2, relativeHeaderOffset, diskStartNumber);
        assertEquals((size2 != null ? DWORD : 0) + (compressedSize2 != null ? DWORD : 0) + (relativeHeaderOffset != null ? DWORD : 0) + (diskStartNumber != null ? WORD : 0), zip64ExtraFieldWithAllFields.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] actualLocalFileData = zip64ExtraField.getLocalFileDataData();
        assertEquals(0, actualLocalFileData.length);

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField zip64ExtraFieldWithSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] expectedLocalFileData = new byte[2 * DWORD];
        addSizes(expectedLocalFileData);
        byte[] actualLocalFileDataWithSizes = zip64ExtraFieldWithSizes.getLocalFileDataData();
        assertArrayEquals(expectedLocalFileData, actualLocalFileDataWithSizes);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] actualCentralDirectoryData = zip64ExtraField.getCentralDirectoryData();
        assertEquals(0, actualCentralDirectoryData.length);

        ZipEightByteInteger size = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipExtendedInformationExtraField zip64ExtraFieldWithSizes = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] expectedCentralDirectoryData = new byte[((size != null ? DWORD : 0) + (compressedSize != null ? DWORD : 0) + (relativeHeaderOffset != null ? DWORD : 0) + (diskStartNumber != null ? WORD : 0))];
        int offset = addSizes(expectedCentralDirectoryData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedCentralDirectoryData, offset, DWORD);
            offset += DWORD;
        }
        if (diskStartNumber != null) {
            System.arraycopy(diskStartNumber.getBytes(), 0, expectedCentralDirectoryData, offset, WORD);
            offset += WORD;
        }
        byte[] actualCentralDirectoryDataWithSizes = zip64ExtraFieldWithSizes.getCentralDirectoryData();
        assertArrayEquals(expectedCentralDirectoryData, actualCentralDirectoryDataWithSizes);
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
    }
}