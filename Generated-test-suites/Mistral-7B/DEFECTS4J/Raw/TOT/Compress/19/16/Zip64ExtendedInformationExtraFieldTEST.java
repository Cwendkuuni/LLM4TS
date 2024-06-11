package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test creating an instance with valid sizes.
     */
    @Test
    public void testValidSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                originalSize, compressedSize);

        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());
    }

    /**
     * Test creating an instance with valid sizes and offset.
     */
    @Test
    public void testValidSizesAndOffset() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x9abcdef0);
        ZipEightByteInteger offset = new ZipEightByteInteger(0x87654321);
        ZipLong diskStart = new ZipLong(0x0123456789abcdefL);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                originalSize, compressedSize,
                new ZipEightByteInteger(offset.getValue() >> 16),
                new ZipEightByteInteger(offset.getValue() & 0xFFFF),
                diskStart);

        Assert.assertEquals(originalSize, extraField.getSize());
        Assert.assertEquals(compressedSize, extraField.getCompressedSize());
        Assert.assertEquals(new ZipEightByteInteger(offset.getValue()),
                extraField.getRelativeHeaderOffset());
        Assert.assertEquals(diskStart, extraField.getDiskStart());
    }

    /**
     * Test creating an instance with only sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSizesOnly() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x9abcdef0);
        new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
    }

    /**
     * Test creating an instance with only offset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOffsetOnly() {
        ZipEightByteInteger offset = new ZipEightByteInteger(0x87654321);
        new Zip64ExtendedInformationExtraField(null, null, offset, null);
    }

    /**
     * Test creating an instance with only disk start.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDiskStartOnly() {
        ZipLong diskStart = new ZipLong(0x0123456789abcdefL);
        new Zip64ExtendedInformationExtraField(null, null, null, diskStart);
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[] {
                0, 0, 0, 0, // tag
                2, 0, 0, 0, // size of this "extra" block

                0x12, 0, 0, 0, 0x34, 0, 0, 0x56, 0x78, 0, 0, 0, 0, // original size
                0x9a, 0, 0, 0, 0xbc, 0, 0, 0xde, 0xf0, 0, 0, 0, 0, // compressed size
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        Assert.assertEquals(new ZipEightByteInteger(0x12345678), extraField.getSize());
        Assert.assertEquals(new ZipEightByteInteger(0x9abcdef0), extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with all fields.
     */
    @Test
    public void testParseFromCentralDirectoryDataAllFields() throws ZipException {
        byte[] centralDirectoryData = new byte[] {
                0x02, 0, 0, 0, // length of this extra field

                0x12, 0, 0, 0, 0x34, 0, 0, 0x56, 0x78, 0, 0, 0, 0, // original size
                0x9a, 0, 0, 0, 0xbc, 0, 0, 0xde, 0xf0, 0, 0, 0, 0, // compressed size
                0x21, 0, 0, 0, 0x43, 0, 0, 0x65, 0x87, 0, 0, 0, 0, // relative header offset

                0x67, 0x89, 0xab, 0xcd, 0xef, 0xde, 0xad, 0xbc, 0x01, 0x23, 0x45, 0x67, 0x89, 0xabc, 0xdef, 0xef, 0xde, 0xad, 0xbc, 0x0L // disk start
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assert.assertEquals(new ZipEightByteInteger(0x12345678), extraField.getSize());
        Assert.assertEquals(new ZipEightByteInteger(0x9abcdef0), extraField.getCompressedSize());
        Assert.assertEquals(new ZipEightByteInteger(0x87654321), extraField.getRelativeHeaderOffset());
        Assert.assertEquals(new ZipLong(0x0123456789abcdefL), extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with only sizes.
     */
    @Test
    public void testParseFromCentralDirectoryDataOnlySizes() throws ZipException {
        byte[] centralDirectoryData = new byte[] {
                0x06, 0, 0, 0, // length of this extra field

                0x12, 0, 0, 0, 0x34, 0, 0, 0x56, 0x78, 0, 0, 0, 0, // original size
                0x9a, 0, 0, 0, 0xbc, 0, 0, 0xde, 0xf0, 0, 0, 0, 0 // compressed size
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assert.assertEquals(new ZipEightByteInteger(0x12345678), extraField.getSize());
        Assert.assertEquals(new ZipEightByteInteger(0x9abcdef0), extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with only offset.
     */
    @Test
    public void testParseFromCentralDirectoryDataOnlyOffset() throws ZipException {
        byte[] centralDirectoryData = new byte[] {
                0x08, 0, 0, 0, // length of this extra field

                0, 0, 0, 0, 0, 0, 0, 0, // original size
                0, 0, 0, 0, 0, 0, 0, 0, // compressed size
                0x21, 0, 0, 0, 0x43, 0, 0, 0x65, 0x87, 0, 0, 0, 0 // relative header offset
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertEquals(new ZipEightByteInteger(0x87654321), extraField.getRelativeHeaderOffset());
        Assert.assertNull(extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with only disk start.
     */
    @Test
    public void testParseFromCentralDirectoryDataOnlyDiskStart() throws ZipException {
        byte[] centralDirectoryData = new byte[] {
                0x04, 0, 0, 0, // length of this extra field

                0, 0, 0, 0, 0, 0, 0, 0, // original size
                0, 0, 0, 0, 0, 0, 0, 0, // compressed size

                0x67, 0x89, 0xab, 0xcd, 0xef, 0xde, 0xad, 0xbc, 0x01, 0x23, 0x45, 0x67, 0x89, 0xabc, 0xdef, 0xef, 0xde, 0xad, 0xbc, 0xL // disk start
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assert.assertNull(extraField.getSize());
        Assert.assertNull(extraField.getCompressedSize());
        Assert.assertNull(extraField.getRelativeHeaderOffset());
        Assert.assertEquals(new ZipLong(0x0123456789abcdefL), extraField.getDiskStart());
    }

    /**
     * Test parsing from central directory data with no fields.
     */
    @Test
    public void testParseFromCentralDirectoryDataNoFields() throws ZipException {
        byte[] centralDirectoryData = new byte[] {0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}