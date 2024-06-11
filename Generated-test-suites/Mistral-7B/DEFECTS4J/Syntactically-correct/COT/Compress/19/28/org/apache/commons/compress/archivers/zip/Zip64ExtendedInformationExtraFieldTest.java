package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

import java.util.Arrays;

/**
 * Test for Zip64ExtendedInformationExtraField class.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test constructor with valid original and compressed size.
     */
    @Test
    public void testValidConstruction() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x23456789);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    /**
     * Test constructor with null original or compressed size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullSizes() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    /**
     * Test getLocalFileDataLength method.
     */
    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x23456789);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(new ZipShort(2 * ZipConstants.DWORD), extraField.getLocalFileDataLength());
    }

    /**
     * Test getCentralDirectoryLength method.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x23456789);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(new ZipShort((originalSize.getValue() + compressedSize.getValue())
                                 + (extraField.relativeHeaderOffset != null ? ZipConstants.DWORD : 0)
                                 + (extraField.diskStart != null ? ZipConstants.WORD : 0)),
                extraField.getCentralDirectoryLength());
    }

    /**
     * Test parseFromLocalFileData method with valid data.
     */
    @Test
    public void testParseFromLocalFileDataValidData() {
        byte[] localFileData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                           0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11,
                                           0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19};
        ZipEightByteInteger originalSize = new ZipEightByteInteger(localFileData, 0);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(localFileData, 8);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    /**
     * Test parseFromLocalFileData method with invalid data.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() {
        byte[] localFileData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    /**
     * Test parseFromCentralDirectoryData method with valid data and no fields.
     */
    @Test
    public void testParseFromCentralDirectoryDataNoFields() {
        byte[] centralDirectoryData = {};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        // No fields should be set
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    /**
     * Test parseFromCentralDirectoryData method with valid data and all fields.
     */
    @Test
    public void testParseFromCentralDirectoryDataAllFields() {
        byte[] centralDirectoryData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                                                0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12,
                                                0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c,
                                                0x1d, 0x1e, 0x1f};
        ZipEightByteInteger originalSize = new ZipEightByteInteger(centralDirectoryData, 0);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(centralDirectoryData, 8);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(centralDirectoryData, 16);
        ZipLong diskStartNumber = new ZipLong(centralDirectoryData, 24);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    /**
     * Test parseFromCentralDirectoryData method with valid data and missing fields.
     */
    @Test
    public void testParseFromCentralDirectoryDataMissingFields() {
        byte[] centralDirectoryData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                                                0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    /**
     * Test reparseCentralDirectoryData method with valid data.
     */
    @Test
    public void testReparseCentralDirectoryDataValidData() {
        byte[] centralDirectoryData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                                                0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12,
                                                0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c,
                                                0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger(centralDirectoryData, 0);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger(centralDirectoryData, 8);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(centralDirectoryData, 16);
        ZipLong expectedDiskStartNumber = new ZipLong(centralDirectoryData, 24);
        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStartNumber, extraField.getDiskStartNumber());
    }

    /**
     * Test reparseCentralDirectoryData method with invalid data.
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataInvalidData() {
        byte[] centralDirectoryData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                                                0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12,
                                                0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c,
                                                0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
    }

    /**
     * Test getLocalFileDataData method.
     */
    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x23456789);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        byte[] expectedData = new byte[]{0x12, 0x34, 0x56, 0x78, 0x23, 0x45, 0x67, 0x89};
        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test getCentralDirectoryData method.
     */
    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x23456789);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x34567890);
        ZipLong diskStartNumber = new ZipLong(0x80000001L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                relativeHeaderOffset, diskStartNumber);
        byte[] expectedData = Arrays.concat(originalSize.getBytes(), compressedSize.getBytes(),
                relativeHeaderOffset.getBytes(), diskStartNumber.getBytes());
        byte[] actualData = extraField.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }
}