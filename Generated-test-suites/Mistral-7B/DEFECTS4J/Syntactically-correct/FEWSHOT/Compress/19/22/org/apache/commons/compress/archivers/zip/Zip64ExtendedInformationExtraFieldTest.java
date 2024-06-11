package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField emptyExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, emptyExtraField.getHeaderId());
        assertNull(emptyExtraField.getSize());
        assertNull(emptyExtraField.getCompressedSize());
        assertNull(emptyExtraField.getRelativeHeaderOffset());
        assertNull(emptyExtraField.getDiskStart());
    }

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x10, (short) 0x20);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x30, (short) 0x40);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(ZipShort.ZERO, extraField.getHeaderId());
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, extraField.getLocalFileDataLength());

        extraField.setSize(new ZipEightByteInteger((short) 0x10, (short) 0x20));
        extraField.setCompressedSize(new ZipEightByteInteger((short) 0x30, (short) 0x40));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((short) 0x50, (short) 0x60));
        extraField.setDiskStart(new ZipLong((short) 0x70, (short) 0x80, (short) 0x90, (short) 0xA0));
        assertEquals(new ZipShort(2 * DWORD), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, extraField.getCentralDirectoryLength());

        extraField.setSize(new ZipEightByteInteger((short) 0x10, (short) 0x20));
        extraField.setCompressedSize(new ZipEightByteInteger((short) 0x30, (short) 0x40));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((short) 0x50, (short) 0x60));
        extraField.setDiskStart(new ZipLong((short) 0x70, (short) 0x80, (short) 0x90, (short) 0xA0));
        assertEquals(new ZipShort((2 * DWORD) + (DWORD) + (DWORD) + (WORD)), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.setSize(new ZipEightByteInteger((short) 0x10, (short) 0x20));
        extraField.setCompressedSize(new ZipEightByteInteger((short) 0x30, (short) 0x40));
        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);

        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((short) 0x50, (short) 0x60));
        byte[] extendedData = new byte[2 * DWORD + DWORD];
        addSizes(extendedData);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, extendedData, 2 * DWORD, DWORD);
        actualData = extraField.getLocalFileDataData();
        assertArrayEquals(extendedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x10, (short) 0x20);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x30, (short) 0x40);

        // Without both sizes
        System.arraycopy(new byte[DWORD], 0, localFileData, 0, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(ZipShort.ZERO, extraField.getSize());
        assertEquals(ZipShort.ZERO, extraField.getCompressedSize());

        // With both sizes
        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, DWORD, DWORD);
        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        // Without any data
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // With only size data
        System.arraycopy(new byte[DWORD], 0, centralDirectoryData, 0, DWORD);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(ZipEightByteInteger.ZERO, extraField.getSize());

        // With only compressed size data
        System.arraycopy(new byte[DWORD], 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(new byte[DWORD], 0, centralDirectoryData, DWORD, DWORD);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(ZipEightByteInteger.ZERO, extraField.getCompressedSize());

        // With both size and offset data
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x10, (short) 0x20);
        ZipEightByteInteger offset = new ZipEightByteInteger((short) 0x30, (short) 0x40);
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(offset.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(offset, extraField.getRelativeHeaderOffset());

        // With all data
        ZipLong diskStart = new ZipLong((short) 0x50, (short) 0x60, (short) 0x70, (short) 0x80);
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(offset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(offset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() throws ZipException {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, off, WORD);
            off += WORD;
        }
    }
}