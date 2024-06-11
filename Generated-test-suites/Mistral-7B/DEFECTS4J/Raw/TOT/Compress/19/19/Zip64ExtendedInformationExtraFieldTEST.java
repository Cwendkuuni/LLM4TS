package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test creating a new instance with valid input.
     */
    @Test
    public void testCreateWithValidInput() {
        ZipEightByteInteger size = new ZipEightByteInteger(0x12345678L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x65432101L);
        ZipLong diskStart = new ZipLong(0x1001L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
            size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(ZipShort.valueOf(0x0001), extraField.getHeaderId());
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test creating a new instance with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullInput() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[] {
            0, 0, 0, 0, // tag
            0, 0, 0, 0, // size of this extra block
            0, 0, 0, 0, 0, 0, 0, 0, // original size
            0, 0, 0, 0, 0, 0, 0, 0, // compressed size
            0, 0, 0, 0, 0, 0, 0, 0, // relative header offset
            0, 0, // disk start number
            0, 0 // disk start number (continued)
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(ZipShort.valueOf(0x0001), extraField.getHeaderId());
        assertEquals(new ZipEightByteInteger(0x12345678L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(0x87654321L), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new long[] { 0x65432101, 0 }),
            extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x1001L), extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from central directory data with valid input.
     */
    @Test
    public void testParseFromCentralDirectoryDataWithValidInput() {
        byte[] centralDirectoryData = new byte[] {
            0, 0, 0, 0, // tag
            0, 0, 0, 0, // size of this extra block
            0, 0, 0, 0, 0, 0, 0, 0, // original size
            0, 0, 0, 0, 0, 0, 0, 0, // compressed size
            0, 0, 0, 0, 0, 0, 0, 0, // relative header offset
            0, 0, 0, 0, // disk start number
            0, 0 // disk start number (continued)
        };
        byte[] rawCentralDirectoryData = new byte[] { 0x01, 0x00, 0x01, 0x00,
            0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01,
            0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01,
            0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01,
            0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01,
            0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x31, 0x01, 0x00, 0x01, 0x00,
            0x31, 0x01
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0,
            rawCentralDirectoryData.length);
        assertEquals(new ZipEightByteInteger(0x12345678L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(0x87654321L), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new long[] { 0x65432101, 0 }),
            extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x1001L), extraField.getDiskStartNumber());
    }

    /**
     * Test parsing from central directory data with invalid input.
     */
    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidInput() {
        byte[] centralDirectoryData = new byte[] {
            0, 0, 0, 0, // tag
            0, 0, 0, 0, // size of this extra block
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // original size (too long)
            0, 0, 0, 0, 0, 0, 0, 0, // compressed size
            0, 0, 0, 0, 0, 0, 0, 0, // relative header offset
            0, 0 // disk start number
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
            centralDirectoryData.length);
        fail(); // should not reach here
    }
}