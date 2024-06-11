package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithBothSizeValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertNotNull(extraField);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testNewInstanceWithOnlySizeValue() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size);
        assertNotNull(extraField);
        assertEquals(size, extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithAllValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x01234567);
        ZipLong diskStart = new ZipLong((int) 0x80000001);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertNotNull(extraField);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullSizeValue() {
        ZipEightByteInteger size = null;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, new ZipEightByteInteger((short) 0x9abcdef0));
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullCompressedSizeValue() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = null;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeSizeValue() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0xffffff80);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeCompressedSizeValue() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0xffffff80);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(new ZipShort(2 * DWORD), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(new ZipShort((originalSize.getValue() + compressedSize.getValue() + DWORD)
                + (relativeHeaderOffset != null ? DWORD : 0)
                + (diskStart != null ? WORD : 0)), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        byte[] localFileData = extraField.getLocalFileDataData();
        assertEquals(2 * DWORD, localFileData.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();
        assertEquals((originalSize.getValue() + compressedSize.getValue() + DWORD)
                + (relativeHeaderOffset != null ? DWORD : 0)
                + (diskStart != null ? WORD : 0), centralDirectoryData.length);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        System.arraycopy(expectedOriginalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, localFileData, DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidDataLength() throws ZipException {
        byte[] localFileData = new byte[2 * DWORD - 2];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Should throw ZipException");
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger((short) 0x01234567);
        ZipLong expectedDiskStart = new ZipLong((int) 0x80000001);
        System.arraycopy(expectedOriginalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(expectedRelativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(expectedOriginalSize, extraField.getSize());
        assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidDataLength() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Should throw ZipException");
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        if (hasUncompressedSize) {
            ZipEightByteInteger expectedOriginalSize = new ZipEightByteInteger((short) 0x12345678);
            assertEquals(expectedOriginalSize, extraField.getSize());
        } else {
            assertNull(extraField.getSize());
        }
        if (hasCompressedSize) {
            ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
            assertEquals(expectedCompressedSize, extraField.getCompressedSize());
        } else {
            assertNull(extraField.getCompressedSize());
        }
        if (hasRelativeHeaderOffset) {
            ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger((short) 0x01234567);
            assertEquals(expectedRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        } else {
            assertNull(extraField.getRelativeHeaderOffset());
        }
        if (hasDiskStart) {
            ZipLong expectedDiskStart = new ZipLong((int) 0x80000001);
            assertEquals(expectedDiskStart, extraField.getDiskStartNumber());
        } else {
            assertNull(extraField.getDiskStartNumber());
        }
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoUncompressedSize() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        boolean hasUncompressedSize = false;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertNull(extraField.getSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoCompressedSize() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = false;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertNull(extraField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoRelativeHeaderOffset() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = true;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertNull(extraField.getRelativeHeaderOffset());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoDiskStart() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = false;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertNull(extraField.getDiskStartNumber());
    }
}