package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipExtendedInfo;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Test cases for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test constructor with only size and compressedSize.
     */
    @org.junit.Test
    public void testValidConstructionWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertNotNull(e);
        assertEquals(size, e.getSize());
        assertEquals(compressedSize, e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
    }

    /**
     * Test constructor with all four fields.
     */
    @org.junit.Test
    public void testValidConstructionWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(42);
        ZipLong diskStart = new ZipLong(0x1234567890L);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertNotNull(e);
        assertEquals(size, e.getSize());
        assertEquals(compressedSize, e.getCompressedSize());
        assertEquals(relativeHeaderOffset, e.getRelativeHeaderOffset());
        assertEquals(diskStart, e.getDiskStartNumber());
    }

    /**
     * Test constructor with missing size field.
     */
    @org.junit.Test
    public void testInvalidConstructionWithoutSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        assertThrows(IllegalArgumentException.class, () -> new Zip64ExtendedInformationExtraField(null, compressedSize));
    }

    /**
     * Test constructor with missing compressedSize field.
     */
    @org.junit.Test
    public void testInvalidConstructionWithoutCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        assertThrows(IllegalArgumentException.class, () -> new Zip64ExtendedInformationExtraField(size, null));
    }

    /**
     * Test parsing valid data from local file header.
     */
    @org.junit.Test
    public void testParseLocalFileData() {
        byte[] data = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromLocalFileData(data, 0, data.length);
        assertEquals(size, e.getSize());
        assertEquals(compressedSize, e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
    }

    /**
     * Test parsing valid data from central directory
     */
    @org.junit.Test
    public void testParseCentralDirectoryData() {
        byte[] data = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD * 2, ZipConstants.DWORD);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(size, e.getSize());
        assertEquals(compressedSize, e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
    }

    /**
     * Test parsing data with missing size field in central directory
     */
    @org.junit.Test
    public void testParseCentralDirectoryDataMissingSize() {
        byte[] data = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        System.arraycopy(compressedSize.getBytes(), 0, data, 0, ZipConstants.DWORD);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> e.parseFromCentralDirectoryData(data, 0, data.length));
    }

    /**
     * Test parsing data with missing compressedSize field in central directory
     */
    @org.junit.Test
    public void testParseCentralDirectoryDataMissingCompressedSize() {
        byte[] data = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        System.arraycopy(size.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> e.parseFromCentralDirectoryData(data, 0, data.length));
    }

    /**
     * Test parsing data with all four fields in central directory
     */
    @org.junit.Test
    public void testParseCentralDirectoryDataAllFields() {
        byte[] data = new byte[5 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(42);
        ZipLong diskStart = new ZipLong(0x1234567890L);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD * 2, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, ZipConstants.DWORD * 3, ZipConstants.DWORD);
        System.arraycopy(diskStart.getBytes(), 0, data, data.length - ZipConstants.WORD, ZipConstants.WORD);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(size, e.getSize());
        assertEquals(compressedSize, e.getCompressedSize());
        assertEquals(relativeHeaderOffset, e.getRelativeHeaderOffset());
        assertEquals(diskStart, e.getDiskStartNumber());
    }

    /**
     * Test parsing data with missing diskStart field in central directory
     */
    @org.junit.Test
    public void testParseCentralDirectoryDataMissingDiskStart() {
        byte[] data = new byte[4 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(42);
        System.arraycopy(size.getBytes(), 0, data, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, data, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, ZipConstants.DWORD * 2, ZipConstants.DWORD);
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> e.parseFromCentralDirectoryData(data, 0, data.length));
    }
}