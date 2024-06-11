package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

import java.io.ByteArrayInputStream;
import java.util.zip.DataFormatException;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test constructor with both size values.
     */
    @org.junit.Test
    public void testConstructionWithBothSizeValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                originalSize, compressedSize);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertTrue(extraField.getRelativeHeaderOffset() == null);
        assertTrue(extraField.getDiskStart() == null);
    }

    /**
     * Test constructor with all size values.
     */
    @org.junit.Test
    public void testConstructionWithAllSizeValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(7890);
        ZipLong diskStart = new ZipLong(123456789);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                originalSize, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test {@link Zip64ExtendedInformationExtraField#parseFromLocalFileData(byte[], int, int)}
     * method with data containing both size values.
     */
    @org.junit.Test
    public void testParseFromLocalFileDataWithBothSizeValues() throws ZipException {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);

        Zip64ExtendedInformationExtraField.addSizes(data);
        System.arraycopy(originalSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertTrue(extraField.getRelativeHeaderOffset() == null);
        assertTrue(extraField.getDiskStart() == null);
    }

    /**
     * Test {@link Zip64ExtendedInformationExtraField#parseFromLocalFileData(byte[], int, int)}
     * method with insufficient data.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInsufficientData() throws ZipException {
        byte[] data = new byte[DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);

        Zip64ExtendedInformationExtraField.addSizes(data);
        System.arraycopy(originalSize.getBytes(), 0, data, 0, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        fail("Expected ZipException due to insufficient data.");
    }

    /**
     * Test {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}
     * method with data containing all fields.
     */
    @org.junit.Test
    public void testParseFromCentralDirectoryDataWithAllFields() throws ZipException, DataFormatException {
        byte[] data = new byte[3 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(7890);
        ZipLong diskStart = new ZipLong(123456789);

        System.arraycopy(originalSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}
     * method with data containing only disk start number.
     */
    @org.junit.Test
    public void testParseFromCentralDirectoryDataWithDiskStartNumberOnly() throws ZipException, DataFormatException {
        byte[] data = new byte[3 * DWORD];
        ZipLong diskStart = new ZipLong(123456789);

        System.arraycopy(diskStart.getBytes(), 0, data, 2 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        assertTrue(extraField.getSize() == null);
        assertTrue(extraField.getCompressedSize() == null);
        assertTrue(extraField.getRelativeHeaderOffset() == null);
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test {@link Zip64ExtendedInformationExtraField#reparseCentralDirectoryData(boolean, boolean, boolean, boolean)}
     * method with all fields present.
     */
    @org.junit.Test
    public void testReparseCentralDirectoryDataWithAllFields() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(7890);
        ZipLong diskStart = new ZipLong(123456789);

        System.arraycopy(originalSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test {@link Zip64ExtendedInformationExtraField#reparseCentralDirectoryData(boolean, boolean, boolean, boolean)}
     * method with only compressed size and disk start number present.
     */
    @org.junit.Test
    public void testReparseCentralDirectoryDataWithCompressedSizeAndDiskStartNumberOnly() throws ZipException {
        byte[] data = new byte[3 * DWORD];
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(67890);
        ZipLong diskStart = new ZipLong(123456789);

        System.arraycopy(compressedSize.getBytes(), 0, data, 0, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, 2 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);

        extraField.reparseCentralDirectoryData(false, false, false, true);

        assertTrue(extraField.getSize() == null);
        assertTrue(compressedSize.equals(extraField.getCompressedSize()));
        assertTrue(extraField.getRelativeHeaderOffset() == null);
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }
}