package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {
    @Test
    public void testNewInstanceWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithOnlySizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, null, null);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingFromLocalFileData() {
        byte[] localFileData = new byte[8 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, localFileData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, offset, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, 8 * DWORD);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParsingFromCentralDirectoryDataWithAllFields() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, offset, WORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testParsingFromCentralDirectoryDataWithOnlySizes() {
        byte[] centralDirectoryData = new byte[3 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length));
    }

    @Test
    public void testParsingFromCentralDirectoryDataWithOnlyOffset() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        int offset = 0;
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length));
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        addSizes(data);
        assertArrayEquals(data, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(1234L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234L);
        ZipLong diskStart = new ZipLong(0x5678L);
        byte[] data = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        int offset = addSizes(data);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, offset, DWORD);
            offset += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, offset, WORD);
            offset += WORD;
        }
        assertArrayEquals(data, extraField.getCentralDirectoryData());
    }
}