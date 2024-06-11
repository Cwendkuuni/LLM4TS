package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;

import org.junit.Test;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, field.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLengthValidSizes() {
        ZipEightByteInteger size = ZipEightByteInteger.valueOf(1024L);
        ZipEightByteInteger compressedSize = ZipEightByteInteger.valueOf(512L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(new ZipShort(2 * ZipConstants.DWORD), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLengthNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, field.getCentralDirectoryLength());
    }

    @Test
    public void testGetCentralDirectoryLengthValidSizes() {
        ZipEightByteInteger size = ZipEightByteInteger.valueOf(1024L);
        ZipEightByteInteger compressedSize = ZipEightByteInteger.valueOf(512L);
        ZipEightByteInteger relativeHeaderOffset = ZipEightByteInteger.valueOf(123L);
        ZipLong diskStart = ZipLong.valueOf(456L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(new ZipShort(2 * ZipConstants.DWORD + DWORD + WORD), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataDataNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getLocalFileDataData());
    }

    @Test
    public void testGetLocalFileDataDataValidSizes() {
        ZipEightByteInteger size = ZipEightByteInteger.valueOf(1024L);
        ZipEightByteInteger compressedSize = ZipEightByteInteger.valueOf(512L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] expectedData = new byte[2 * ZipConstants.DWORD];
        field.addSizes(expectedData);
        assertArrayEquals(expectedData, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryDataNullSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getCentralDirectoryData());
    }

    @Test
    public void testGetCentralDirectoryDataValidSizes() {
        ZipEightByteInteger size = ZipEightByteInteger.valueOf(1024L);
        ZipEightByteInteger compressedSize = ZipEightByteInteger.valueOf(512L);
        ZipEightByteInteger relativeHeaderOffset = ZipEightByteInteger.valueOf(123L);
        ZipLong diskStart = ZipLong.valueOf(456L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        byte[] expectedData = new byte[2 * ZipConstants.DWORD + DWORD + WORD];
        field.addSizes(expectedData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, expectedData.length - WORD, WORD);
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, expectedData.length - WORD - WORD, WORD);
        }
        assertArrayEquals(expectedData, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileDataValidData() {
        ZipShort headerId = ZipShort.valueOf(0x0001);
        byte[] localFileData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, 0x00, 0x04,
            0x00, 0x08, 0x04, 0x00, 0x00, 0x04, 0x00, 0x04,
            0x00, 0x08, 0x04, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(headerId, field.getHeaderId());
        ZipEightByteInteger size = field.getSize();
        assertEquals(1024L, size.getValue());
        ZipEightByteInteger compressedSize = field.getCompressedSize();
        assertEquals(512L, compressedSize.getValue());
        ZipEightByteInteger relativeHeaderOffset = field.getRelativeHeaderOffset();
        assertEquals(1L, relativeHeaderOffset.getValue());
        ZipLong diskStart = field.getDiskStartNumber();
        assertEquals(1L, diskStart.getValue());
    }

    @Test
    public void testParseFromLocalFileDataInvalidData() {
        byte[] localFileData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, 0x00, 0x04,
            0x00, 0x08, 0x04, 0x00, 0x00, 0x04, 0x00, 0x04,
            0x00, // missing last byte
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> field.parseFromLocalFileData(localFileData, 0, localFileData.length));
    }

    @Test
    public void testParseFromCentralDirectoryDataNullData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(null, 0, 0);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataValidDataWithAllFields() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, // size
            0x00, 0x08, 0x04, 0x00, 0x00, 0x04, 0x00, 0x04, // compressed size
            0x00, 0x08, 0x04, 0x01, // relative header offset
            0x00, 0x04, 0x03, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, // disk start number
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        ZipEightByteInteger size = field.getSize();
        assertEquals(1024L, size.getValue());
        ZipEightByteInteger compressedSize = field.getCompressedSize();
        assertEquals(512L, compressedSize.getValue());
        ZipEightByteInteger relativeHeaderOffset = field.getRelativeHeaderOffset();
        assertEquals(1L, relativeHeaderOffset.getValue());
        ZipLong diskStart = field.getDiskStartNumber();
        assertEquals(3L, diskStart.getValue());
    }

    @Test
    public void testParseFromCentralDirectoryDataValidDataWithOnlySizes() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, // size
            0x00, 0x08, 0x04, 0x00, 0x00, 0x04, // compressed size
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        ZipEightByteInteger size = field.getSize();
        assertEquals(1024L, size.getValue());
        ZipEightByteInteger compressedSize = field.getCompressedSize();
        assertEquals(512L, compressedSize.getValue());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataValidDataWithOnlyRelativeHeaderOffset() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, // size
            0x00, 0x02, 0x00, 0x01, // compressed size
            0x00, 0x08, 0x04, 0x01 // relative header offset
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        ZipEightByteInteger size = field.getSize();
        assertEquals(1024L, size.getValue());
        assertNull(field.getCompressedSize());
        ZipEightByteInteger relativeHeaderOffset = field.getRelativeHeaderOffset();
        assertEquals(1L, relativeHeaderOffset.getValue());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataValidDataWithOnlyDiskStartNumber() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, // size
            0x00, 0x02, 0x00, 0x01, 0x00, 0x04, 0x00, 0x04, // compressed size
            0x00, 0x04, 0x03, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // disk start number
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        ZipEightByteInteger size = field.getSize();
        assertEquals(1024L, size.getValue());
        ZipEightByteInteger compressedSize = field.getCompressedSize();
        assertEquals(512L, compressedSize.getValue());
        ZipLong diskStart = field.getDiskStartNumber();
        assertEquals(3L, diskStart.getValue());
        assertNull(field.getRelativeHeaderOffset());
    }

    @Test
    public void testParseFromCentralDirectoryDataInvalidData() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x02, 0x00, 0x00, 0x08, 0x04, 0x00, 0x04, // size
            0x00, 0x08, 0x04, 0x00 // compressed size, missing last byte
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length));
    }

}