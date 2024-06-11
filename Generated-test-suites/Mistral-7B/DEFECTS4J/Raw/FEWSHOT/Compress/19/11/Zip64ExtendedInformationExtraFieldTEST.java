package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        field.setSize(size);
        field.setCompressedSize(compressedSize);

        assertEquals(16, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(20L);

        field.setSize(size);
        field.setCompressedSize(compressedSize);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        field.setDiskStartNumber(diskStart);

        assertEquals(32, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        field.setSize(size);
        field.setCompressedSize(compressedSize);

        byte[] expectedData = new byte[16];
        field.addSizes(expectedData);

        assertArrayEquals(expectedData, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(20L);

        field.setSize(size);
        field.setCompressedSize(compressedSize);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        field.setDiskStartNumber(diskStart);

        byte[] expectedData = new byte[32];
        field.addSizes(expectedData);

        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, 24, DWORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, 28, WORD);
        }

        assertArrayEquals(expectedData, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];

        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        size.writeTo(data, 0);
        compressedSize.writeTo(data, 4);

        field.parseFromLocalFileData(data, 0, data.length);

        assertEquals(100L, field.getSize().getValue());
        assertEquals(50L, field.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[32];

        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(20L);

        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, 4, DWORD);
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 12, DWORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, 16, WORD);
        }

        field.parseFromCentralDirectoryData(data, 0, data.length);

        assertEquals(100L, field.getSize().getValue());
        assertEquals(50L, field.getCompressedSize().getValue());
        assertEquals(10L, field.getRelativeHeaderOffset().getValue());
        assertEquals(20L, field.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[32];

        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(20L);

        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, 0, DWORD);
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, 4, DWORD);
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, 12, DWORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, 16, WORD);
        }

        field.parseFromCentralDirectoryData(data, 0, data.length);

        field.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(100L, field.getSize().getValue());
        assertEquals(50L, field.getCompressedSize().getValue());
        assertEquals(10L, field.getRelativeHeaderOffset().getValue());
        assertEquals(20L, field.getDiskStartNumber().getValue());
    }
}