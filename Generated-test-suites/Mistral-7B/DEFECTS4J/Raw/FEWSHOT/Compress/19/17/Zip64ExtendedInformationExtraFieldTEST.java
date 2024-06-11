package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(HEADER_ID, field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        field.setSize(size);
        assertEquals(new ZipShort(2 * DWORD), field.getLocalFileDataLength());

        field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x456);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        assertEquals(new ZipShort((short) (2 * DWORD + DWORD + DWORD + WORD)),
                     field.getCentralDirectoryLength());

        field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x456);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        assertArrayEquals(expectedData, field.getLocalFileDataData());

        field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x456);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x789);
        ZipLong diskStart = new ZipLong((short) 0x1011);
        field.setSize(size);
        field.setCompressedSize(compressedSize);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        field.setDiskStartNumber(diskStart);
        byte[] expectedData = new byte[2 * DWORD + DWORD + DWORD + WORD];
        int offset = addSizes(expectedData);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(diskStart.getBytes(), 0, expectedData, offset, WORD);
        assertArrayEquals(expectedData, field.getCentralDirectoryData());

        field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getCentralDirectoryData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = new byte[DWORD];
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x456);
        System.arraycopy(size.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, DWORD, DWORD);
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMismatchedLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = new byte[DWORD + DWORD];
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x456);
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());

        byte[] centralDirectoryDataWithOffset = new byte[3 * DWORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x789);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());

        byte[] centralDirectoryDataWithDiskStart = new byte[3 * DWORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        ZipLong diskStart = new ZipLong((short) 0x1011);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 2 * DWORD + DWORD, WORD);
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x123);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x456);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x789);
        ZipLong diskStart = new ZipLong((short) 0x1011);
        System.arraycopy(size.getBytes(), 0, rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, rawCentralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, rawCentralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, rawCentralDirectoryData, 2 * DWORD + DWORD, WORD);
        field.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());

        size = new ZipEightByteInteger((short) 0x123);
        compressedSize = new ZipEightByteInteger((short) 0x456);
        relativeHeaderOffset = new ZipEightByteInteger((short) 0x789);
        diskStart = null;
        System.arraycopy(size.getBytes(), 0, rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, rawCentralDirectoryData, DWORD, DWORD);
        field.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        field.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());

        size = null;
        compressedSize = new ZipEightByteInteger((short) 0x456);
        relativeHeaderOffset = new ZipEightByteInteger((short) 0x789);
        diskStart = new ZipLong((short) 0x1011);
        System.arraycopy(compressedSize.getBytes(), 0, rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, rawCentralDirectoryData, DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, rawCentralDirectoryData, 2 * DWORD, WORD);
        field.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        field.reparseCentralDirectoryData(false, true, true, true);
        assertNull(field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());

        size = new ZipEightByteInteger((short) 0x123);
        compressedSize = null;
        relativeHeaderOffset = new ZipEightByteInteger((short) 0x789);
        diskStart = new ZipLong((short) 0x1011);
        System.arraycopy(size.getBytes(), 0, rawCentralDirectoryData, 0, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, rawCentralDirectoryData, DWORD, DWORD);
        field.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        field.reparseCentralDirectoryData(true, false, true, true);
        assertEquals(size, field.getSize());
        assertNull(field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }
}