package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipExtendedDataReader;
import org.apache.commons.compress.archivers.zip.ZipExtendedDataWriter;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for the {@link Zip64ExtendedInformationExtraField} class.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Tests that creating an instance with both size values set correctly
     * sets the values in the instance.
     */
    @org.junit.Test
    public void testCreateWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x87654321);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, e.getSize());
        assertEquals(compressedSize, e.getCompressedSize());
    }

    /**
     * Tests that creating an instance with neither size value sets the values
     * in the instance to null.
     */
    @org.junit.Test
    public void testCreateWithNoSizes() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        assertNull(e.getSize());
        assertNull(e.getCompressedSize());
    }

    /**
     * Tests that parsing the local file data correctly sets the size values
     * in the instance.
     */
    @org.junit.Test
    public void testParseLocalFileData() throws IOException {
        byte[] localData = new byte[] {
            0, 0, (byte) 0xFF, (byte) 0xFF, // tag (0001)
            0, 0, 0, 0, // size of this "extra" block
            0, 0, 0, 0, 0x12, 0, 0, 0, // original size
            0, 0, 0, 0, 0x34, 0, 0, 0, // compressed size
            0, 0, 0, 0, // relative header offset
            0, 0, // disk start number
        };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromLocalFileData(localData, 0, localData.length);
        assertEquals(new ZipEightByteInteger(0x12345678), e.getSize());
        assertEquals(new ZipEightByteInteger(0x87654321), e.getCompressedSize());
    }

    /**
     * Tests that parsing the local file data with only the tag and size
     * sets the values in the instance to null.
     */
    @org.junit.Test
    public void testParseLocalFileDataWithOnlyTagAndSize() throws IOException {
        byte[] localData = new byte[] {
            0, 0, (byte) 0xFF, (byte) 0xFF, // tag (0001)
            0, 0, 0, 0, // size of this "extra" block
        };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromLocalFileData(localData, 0, localData.length);
        assertNull(e.getSize());
        assertNull(e.getCompressedSize());
    }

    /**
     * Tests that writing and parsing the local file data correctly sets the
     * size values in the instance and returns the correct data.
     */
    @org.junit.Test
    public void testWriteAndReadLocalFileData() throws IOException {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x12345678),
            new ZipEightByteInteger(0x87654321)
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipExtendedDataWriter writer = new ZipExtendedDataWriter(out);
        writer.writeField(e);
        byte[] localData = out.toByteArray();
        Zip64ExtendedInformationExtraField e2 = new Zip64ExtendedInformationExtraField();
        e2.parseFromLocalFileData(localData, 0, localData.length);
        assertEquals(e.getSize(), e2.getSize());
        assertEquals(e.getCompressedSize(), e2.getCompressedSize());
    }

    /**
     * Tests that writing and parsing the central directory data correctly
     * sets the size values in the instance and returns the correct data.
     */
    @org.junit.Test
    public void testWriteAndReadCentralDirectoryData() throws IOException {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(0x12345678),
            new ZipEightByteInteger(0x87654321),
            new ZipEightByteInteger(0x0BADF00D),
            new ZipLong(0xDEADBEEF)
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipExtendedDataWriter writer = new ZipExtendedDataWriter(out);
        writer.writeField(e);
        byte[] centralData = out.toByteArray();
        Zip64ExtendedInformationExtraField e2 = new Zip64ExtendedInformationExtraField();
        e2.parseFromCentralDirectoryData(centralData, 0, centralData.length);
        assertEquals(e.getSize(), e2.getSize());
        assertEquals(e.getCompressedSize(), e2.getCompressedSize());
        assertEquals(e.getRelativeHeaderOffset(), e2.getRelativeHeaderOffset());
        assertEquals(e.getDiskStartNumber(), e2.getDiskStartNumber());
    }

    /**
     * Tests that parsing the central directory data with only the tag and
     * size sets the values in the instance to null.
     */
    @org.junit.Test
    public void testParseCentralDirectoryDataWithOnlyTagAndSize() throws IOException {
        byte[] centralData = new byte[] {
            0, 0, (byte) 0xFF, (byte) 0xFF, // tag (0001)
            0, 0, 0, 0, // size of this "extra" block
        };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromCentralDirectoryData(centralData, 0, centralData.length);
        assertNull(e.getSize());
        assertNull(e.getCompressedSize());
        assertNull(e.getRelativeHeaderOffset());
        assertNull(e.getDiskStartNumber());
    }

    /**
     * Tests that parsing the central directory data with only the tag sets
     * the values in the instance to null.
     */
    @org.junit.Test
    public void testParseCentralDirectoryDataWithOnlyTag() throws IOException {
        byte[] centralData = new byte[] {
            0, 0, (byte) 0xFF, (byte) 0xFF, // tag (0001)
        };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromCentralDirectoryData(centralData, 0, centralData.length);
        assertNull(e.getSize());
        assertNull(e.getCompressedSize());
        assertNull(e.getRelativeHeaderOffset());
        assertNull(e.getDiskStartNumber());
    }
}