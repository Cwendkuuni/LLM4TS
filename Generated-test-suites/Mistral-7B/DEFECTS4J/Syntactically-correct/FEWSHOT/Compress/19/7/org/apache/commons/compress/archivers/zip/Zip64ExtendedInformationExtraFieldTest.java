package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((int) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testCreateWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((int) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 0x12345678);
        ZipLong diskStart = new ZipLong((int) 0x87654321);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithNoData() {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithInvalidSizeData() {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 4);
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
                (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
                (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b,
                (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 16);
        ZipEightByteInteger originalSize = new ZipEightByteInteger(data, 0);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(data, 4);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizeData() {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 4);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithNoData() {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 0);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithSizeDataOnly() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00,
                (byte) 0x03, (byte) 0x00, (byte) 0x04, (byte) 0x00,
                (byte) 0x05, (byte) 0x00, (byte) 0x06, (byte) 0x00,
                (byte) 0x07, (byte) 0x00, (byte) 0x08, (byte) 0x00};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 32);
        assertEquals(new ZipEightByteInteger((int) 0x01020304), extraField.getSize());
        assertEquals(new ZipEightByteInteger((int) 0x05060708), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00,
                (byte) 0x03, (byte) 0x00, (byte) 0x04, (byte) 0x00,
                (byte) 0x05, (byte) 0x00, (byte) 0x06, (byte) 0x00,
                (byte) 0x07, (byte) 0x00, (byte) 0x08, (byte) 0x00,
                (byte) 0x09, (byte) 0x00, (byte) 0x0a, (byte) 0x00,
                (byte) 0x0b, (byte) 0x00, (byte) 0x0c, (byte) 0x00,
                (byte) 0x0d, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x0f};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 32);
        ZipEightByteInteger originalSize = new ZipEightByteInteger((int) 0x01020304);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 0x05060708);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x090a0000);
        ZipLong diskStart = new ZipLong(0x0b0c0d0e0f0000L);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00,
                (byte) 0x03, (byte) 0x00, (byte) 0x04, (byte) 0x00,
                (byte) 0x05, (byte) 0x00, (byte) 0x06, (byte) 0x00,
                (byte) 0x07, (byte) 0x00, (byte) 0x08, (byte) 0x00,
                (byte) 0x09, (byte) 0x00, (byte) 0x0a, (byte) 0x00,
                (byte) 0x0b, (byte) 0x00, (byte) 0x0c, (byte) 0x00,
                (byte) 0x0d, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x0f};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, 32);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        ZipEightByteInteger originalSize = extraField.getSize();
        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        ZipLong diskStart = extraField.getDiskStartNumber();
        assertEquals(originalSize, new ZipEightByteInteger((int) 0x01020304));
        assertEquals(compressedSize, new ZipEightByteInteger((int) 0x05060708));
        assertEquals(relativeHeaderOffset, new ZipEightByteInteger(0x090a0000));
        assertEquals(diskStart, new ZipLong(0x0b0c0d0e0f0000L));
    }
}