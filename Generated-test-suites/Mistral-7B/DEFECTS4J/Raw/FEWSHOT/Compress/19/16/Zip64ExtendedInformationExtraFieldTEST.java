package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithBothSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x11223344);
        ZipLong diskStart = new ZipLong((short) 0x87654321);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullSize() {
        ZipEightByteInteger size = null;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = null;
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[4 * DWORD];

        // Test with both sizes present
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        System.arraycopy(size.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Test with only one size present
        localFileData[0] = (byte) 0xFF;
        localFileData[1] = (byte) 0xFF;
        localFileData[2] = (byte) 0xFF;
        localFileData[3] = (byte) 0xFF;

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        byte[] localFileData = new byte[2 * DWORD];

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * DWORD];

        // Test with all fields present
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x11223344);
        ZipLong diskStart = new ZipLong((short) 0x87654321);

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());

        // Test with only size and compressed size present
        centralDirectoryData[3 * DWORD] = (byte) 0xFF;
        centralDirectoryData[3 * DWORD + 1] = (byte) 0xFF;
        centralDirectoryData[3 * DWORD + 2] = (byte) 0xFF;
        centralDirectoryData[3 * DWORD + 3] = (byte) 0xFF;

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Test with only relative header offset and disk start present
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 2 * DWORD, WORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 3 * DWORD, DWORD);

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());

        // Test with only size present
        centralDirectoryData[0] = (byte) 0xFF;
        centralDirectoryData[1] = (byte) 0xFF;
        centralDirectoryData[2] = (byte) 0xFF;
        centralDirectoryData[3] = (byte) 0xFF;

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        byte[] centralDirectoryData = new byte[2 * DWORD];

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];

        // Test with all fields present
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x11223344);
        ZipLong diskStart = new ZipLong((short) 0x87654321);

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());

        // Test with only size and compressed size present
        centralDirectoryData[3 * DWORD] = (byte) 0xFF;
        centralDirectoryData[3 * DWORD + 1] = (byte) 0xFF;
        centralDirectoryData[3 * DWORD + 2] = (byte) 0xFF;
        centralDirectoryData[3 * DWORD + 3] = (byte) 0xFF;

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        // Test with only relative header offset and disk start present
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 2 * DWORD, WORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 3 * DWORD, DWORD);

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, true, true);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());

        // Test with only size present
        centralDirectoryData[0] = (byte) 0xFF;
        centralDirectoryData[1] = (byte) 0xFF;
        centralDirectoryData[2] = (byte) 0xFF;
        centralDirectoryData[3] = (byte) 0xFF;

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, false, false);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }
}