package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234);
        ZipLong diskStart = new ZipLong(5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize,
                relativeHeaderOffset, diskStart);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(DWORD, extraField.getLocalFileDataLength().getSize());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertNull(extraField.getCentralDirectoryLength());
        extraField.setSize(new ZipEightByteInteger(1));
        extraField.setCompressedSize(new ZipEightByteInteger(2));
        assertEquals(3 * DWORD, extraField.getCentralDirectoryLength());
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(3));
        assertEquals(4 * DWORD, extraField.getCentralDirectoryLength());
        extraField.setDiskStart(new ZipLong(4));
        assertEquals(5 * DWORD + WORD, extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], extraField.getLocalFileDataData());
        extraField.setSize(new ZipEightByteInteger(1));
        extraField.setCompressedSize(new ZipEightByteInteger(2));
        byte[] data = extraField.getLocalFileDataData();
        assertEquals(2 * DWORD, data.length);
        addSizes(data);
        assertEquals(1, data[0] & 0xFF);
        assertEquals(1, data[1] & 0xFF);
        assertEquals(2, data[2] & 0xFF);
        assertEquals(2, data[3] & 0xFF);
        assertEquals(1, data[4] & 0xFF);
        assertEquals(1, data[5] & 0xFF);
        assertEquals(2, data[6] & 0xFF);
        assertEquals(2, data[7] & 0xFF);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());
        extraField.setSize(new ZipEightByteInteger(1));
        extraField.setCompressedSize(new ZipEightByteInteger(2));
        byte[] data = extraField.getCentralDirectoryData();
        assertEquals(DWORD * 3 + WORD, data.length);
        int off = 0;
        if (extraField.getSize() != null) {
            addSizes(data, off);
        }
        if (extraField.getCompressedSize() != null) {
            off += DWORD;
            addSizes(data, off);
        }
        if (extraField.getRelativeHeaderOffset() != null) {
            off += DWORD;
            System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0,
                    data, off, DWORD);
        }
        if (extraField.getDiskStart() != null) {
            off += DWORD;
            System.arraycopy(extraField.getDiskStart().getBytes(), 0,
                    data, off, WORD);
        }
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[2 * DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        addSizes(data);
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(size.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromLocalFileData(data, 0, data.length));
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(extraField.getSize(), new ZipEightByteInteger(1));
        assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger(2));
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithInvalidData() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD - 1];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> extraField.parseFromCentralDirectoryData(data, 0, data.length));
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStart() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(5), extraField.getDiskStart());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] data = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(extraField.getSize(), new ZipEightByteInteger(1));
        assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger(2));
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    private void addSizes(byte[] data, int off) {
        if (extraField.getSize() != null) {
            System.arraycopy(extraField.getSize().getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (extraField.getCompressedSize() != null) {
            System.arraycopy(extraField.getCompressedSize().getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (extraField.getRelativeHeaderOffset() != null) {
            System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (extraField.getDiskStart() != null) {
            System.arraycopy(extraField.getDiskStart().getBytes(), 0, data, off, WORD);
        }
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertArrayEquals(expected, actual, "Expected and actual arrays are not equal");
    }
}