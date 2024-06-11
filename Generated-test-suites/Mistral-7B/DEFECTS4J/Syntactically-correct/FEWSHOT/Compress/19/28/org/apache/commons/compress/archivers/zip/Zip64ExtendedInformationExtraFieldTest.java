package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
    }

    @Test
    public void testCreateWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((long) 20);
        ZipLong diskStart = new ZipLong((long) 30);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertNotNull(extraField.getRelativeHeaderOffset());
        assertNotNull(extraField.getDiskStartNumber());
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(20, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(30, extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreateWithNullSize() {
        ZipEightByteInteger size = null;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreateWithNullCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = null;
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizeValues() {
        byte[] data = new byte[2 * DWORD];
        new Zip64ExtendedInformationExtraField().parseFromLocalFileData(data, 0, 2 * DWORD);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithSizeValuesInWrongOrder() {
        byte[] data = new byte[2 * DWORD + DWORD];
        System.arraycopy(new ZipEightByteInteger(WORD, 0).getBytes(), 0, data, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 2 * DWORD).getBytes(), 0, data, DWORD, DWORD);
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(data, 0, 2 * DWORD + DWORD);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithMissingFields() {
        byte[] data = new byte[3 * DWORD];
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(data, 0, 3 * DWORD);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] data = new byte[3 * DWORD + WORD];
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, data, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 4).getBytes(), 0, data, 4, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 8).getBytes(), 0, data, 8, DWORD);
        System.arraycopy(new ZipEightByteInteger(WORD, 12).getBytes(), 0, data, 12, WORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertNotNull(extraField.getRelativeHeaderOffset());
        assertNotNull(extraField.getDiskStartNumber());
        assertEquals(1073741824L, extraField.getSize().getValue());
        assertEquals(536870912L, extraField.getCompressedSize().getValue());
        assertEquals(0, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] data = new byte[3 * DWORD + WORD];
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, data, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 4).getBytes(), 0, data, 4, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 8).getBytes(), 0, data, 8, DWORD);
        System.arraycopy(new ZipEightByteInteger(WORD, 12).getBytes(), 0, data, 12, WORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 3 * DWORD + WORD);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertNotNull(extraField.getRelativeHeaderOffset());
        assertNotNull(extraField.getDiskStartNumber());
        assertEquals(1073741824L, extraField.getSize().getValue());
        assertEquals(536870912L, extraField.getCompressedSize().getValue());
        assertEquals(0, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingFields() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 2 * DWORD);
        try {
            extraField.reparseCentralDirectoryData(true, true, true, true);
            fail();
        } catch (ZipException e) {
            // expected
        }
    }
}