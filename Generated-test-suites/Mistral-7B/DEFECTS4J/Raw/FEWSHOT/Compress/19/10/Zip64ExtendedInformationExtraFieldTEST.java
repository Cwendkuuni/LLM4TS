package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 5678);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        assertEquals(2 * ZipConstants.DWORD, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((long) 9765);
        ZipLong diskStart = new ZipLong((long) 1123);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        field.setDiskStartNumber(diskStart);
        assertEquals((2 * ZipConstants.DWORD) + ZipConstants.DWORD + ZipConstants.WORD, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[2 * ZipConstants.DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 5678);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        int offset = 0;
        System.arraycopy(size.getBytes(), 0, localFileData, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, offset, ZipConstants.DWORD);
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(size.getValue(), field.getSize().getValue());
        assertEquals(compressedSize.getValue(), field.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 5678);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        int offset = 0;
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, offset, ZipConstants.DWORD);
        offset += ZipConstants.DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, ZipConstants.DWORD);
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size.getValue(), field.getSize().getValue());
        assertEquals(compressedSize.getValue(), field.getCompressedSize().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithNoSizes() throws ZipException {
        byte[] localFileData = new byte[0];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromCentralDirectoryDataWithNoSizes() throws ZipException {
        byte[] centralDirectoryData = new byte[0];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 5678);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        byte[] expectedData = new byte[2 * ZipConstants.DWORD];
        addSizes(expectedData);
        byte[] actualData = field.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((long) 9765);
        ZipLong diskStart = new ZipLong((long) 1123);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        field.setDiskStartNumber(diskStart);
        byte[] expectedData = new byte[field.getCentralDirectoryLength().getValue()];
        int offset = addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, offset, ZipConstants.DWORD);
            offset += ZipConstants.DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, offset, ZipConstants.WORD);
            offset += ZipConstants.WORD;
        }
        byte[] actualData = field.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        return off;
    }
}