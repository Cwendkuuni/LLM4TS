package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStart = new ZipLong(5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getLocalFileDataLength().getValue());
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        assertEquals(2 * DWORD, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getCentralDirectoryLength().getValue());
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(1234));
        extraField.setDiskStart(new ZipLong(5678));
        assertEquals(2 * DWORD + DWORD + DWORD + WORD, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] expectedData = new byte[2 * DWORD];
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));
        addSizes(expectedData);
        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] expectedData = new byte[2 * DWORD + DWORD + DWORD + WORD];
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(1234));
        extraField.setDiskStart(new ZipLong(5678));
        int offset = addSizes(expectedData);
        if (extraField.getRelativeHeaderOffset() != null) {
            System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (extraField.getDiskStart() != null) {
            System.arraycopy(extraField.getDiskStart().getBytes(), 0, expectedData, offset, WORD);
            offset += WORD;
        }
        byte[] actualData = extraField.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[2 * DWORD];
        addSizes(data);
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1024 * 1024, extraField.getSize().getValue());
        assertEquals(512 * 1024, extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[2 * DWORD + DWORD];
        addSizes(data);
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(1024 * 1024, extraField.getSize().getValue());
        assertEquals(512 * 1024, extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[2 * DWORD + DWORD + DWORD + WORD];
        int offset = addSizes(data);
        addRelativeHeaderOffset(data, offset);
        addDiskStart(data, offset);
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(1024 * 1024, extraField.getSize().getValue());
        assertEquals(512 * 1024, extraField.getCompressedSize().getValue());
        assertEquals(1234, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(5678, extraField.getDiskStartNumber().getValue());
    }

    private void addSizes(byte[] data) {
        addSizes(data, 0);
    }

    private void addSizes(byte[] data, int offset) {
        ZipEightByteInteger size = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        System.arraycopy(size.getBytes(), 0, data, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, data, offset, DWORD);
    }

    private void addRelativeHeaderOffset(byte[] data, int offset) {
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, offset, DWORD);
    }

    private void addDiskStart(byte[] data, int offset) {
        ZipLong diskStart = new ZipLong(5678);
        System.arraycopy(diskStart.getBytes(), 0, data, offset, WORD);
    }
}