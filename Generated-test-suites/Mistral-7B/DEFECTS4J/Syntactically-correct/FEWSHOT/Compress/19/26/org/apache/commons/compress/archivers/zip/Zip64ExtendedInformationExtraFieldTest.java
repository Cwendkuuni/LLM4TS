package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] expectedEmptyExtraField = new byte[0];
        byte[] actualEmptyExtraField = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedEmptyExtraField, actualEmptyExtraField);
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldWithSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, null, null, null);
        byte[] expectedSizeData = new byte[DWORD];
        size.getBytes(expectedSizeData, 0);
        byte[] actualSizeData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedSizeData, actualSizeData);
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldWithCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(null, compressedSize, null, null);
        byte[] expectedCompressedSizeData = new byte[DWORD];
        compressedSize.getBytes(expectedCompressedSizeData, 0);
        byte[] actualCompressedSizeData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedCompressedSizeData, actualCompressedSizeData);
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, compressedSize, null, null);
        byte[] expectedData = new byte[2 * DWORD];
        size.getBytes(expectedData, 0);
        compressedSize.getBytes(expectedData, DWORD);
        byte[] actualData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZip64ExtendedInformationExtraFieldWithoutBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, null);
    }

    @Test
    public void testZip64ExtendedInformationExtraFieldWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((long) 4096);
        ZipLong diskStart = new ZipLong((long) 8192);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        byte[] expectedData = new byte[3 * DWORD + WORD];
        size.getBytes(expectedData, 0);
        compressedSize.getBytes(expectedData, DWORD);
        relativeHeaderOffset.getBytes(expectedData, 2 * DWORD);
        diskStart.getBytes(expectedData, 3 * DWORD);
        byte[] actualData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        size.getBytes(localFileData, 0);
        compressedSize.getBytes(localFileData, DWORD);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(size.getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), zip64ExtraField.getCompressedSize().getValue());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(8192L);
        size.getBytes(centralDirectoryData, 0);
        compressedSize.getBytes(centralDirectoryData, DWORD);
        relativeHeaderOffset.getBytes(centralDirectoryData, 2 * DWORD);
        diskStart.getBytes(centralDirectoryData, 3 * DWORD);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size.getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), zip64ExtraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), zip64ExtraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), zip64ExtraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithNoSizeFields() {
        byte[] centralDirectoryData = new byte[WORD];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyRelativeHeaderOffsetAndDiskStart() {
        byte[] centralDirectoryData = new byte[WORD + DWORD];
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(8192L);
        relativeHeaderOffset.getBytes(centralDirectoryData, 0);
        diskStart.getBytes(centralDirectoryData, WORD);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(relativeHeaderOffset.getValue(), zip64ExtraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), zip64ExtraField.getDiskStartNumber().getValue());
        assertNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
    }
}