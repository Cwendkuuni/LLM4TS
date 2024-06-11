package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        ZipExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] expectedData = {(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12,
                (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87};
        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = {(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12,
                (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87,
                (byte) 0x54, (byte) 0x32, (byte) 0x10, (byte) 0x20};
        ZipExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((short) 0x87654321);
        assertEquals(expectedSize, extraField.size);
        assertEquals(expectedCompressedSize, extraField.compressedSize);
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x87654321);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x4321);
        ZipLong diskStart = new ZipLong((short) 0x1020);
        ZipExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize,
                relativeHeaderOffset, diskStart);
        byte[] expectedData = {(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12,
                (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87,
                (byte) 0x21, (byte) 0x43, (byte) 0x04, (byte) 0x00,
                (byte) 0x02, (byte) 0x10, (byte) 0x20};
        byte[] actualData = extraField.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = {(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12,
                (byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87,
                (byte) 0x21, (byte) 0x43, (byte) 0x04, (byte) 0x00};
        ZipExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0,
                centralDirectoryData.length);
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((short) 0x87654321);
        assertEquals(expectedSize, extraField.size);
        assertEquals(expectedCompressedSize, extraField.compressedSize);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        extraField.size = size;
        assertEquals(new ZipShort(8), extraField.getLocalFileDataLength());

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.compressedSize = size;
        assertEquals(new ZipShort(8), extraField.getLocalFileDataLength());

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.relativeHeaderOffset = new ZipEightByteInteger((short) 0x4321);
        assertEquals(new ZipShort(4), extraField.getLocalFileDataLength());

        extraField = new Zip64ExtendedInformationExtraField();
        extraField.diskStart = new ZipLong((short) 0x1020);
        assertEquals(new ZipShort(2), extraField.getLocalFileDataLength());

        extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), extraField.getCentralDirectoryLength());

        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x12345678);
        extraField.size = size;
        assertEquals(new ZipShort(8), extraField.getCentralDirectoryLength());

        extraField.compressedSize = size;
        assertEquals(new ZipShort(16), extraField.getCentralDirectoryLength());

        extraField.relativeHeaderOffset = new ZipEightByteInteger((short) 0x4321);
        assertEquals(new ZipShort(12), extraField.getCentralDirectoryLength());

        extraField.diskStart = new ZipLong((short) 0x1020);
        assertEquals(new ZipShort(2), extraField.getCentralDirectoryLength());

        size = new ZipEightByteInteger((short) 0x12345678);
        extraField.size = size;
        extraField.compressedSize = size;
        assertEquals(new ZipShort(24), extraField.getCentralDirectoryLength());

        extraField.relativeHeaderOffset = new ZipEightByteInteger((short) 0x4321);
        extraField.diskStart = new ZipLong((short) 0x1020);
        assertEquals(new ZipShort(28), extraField.getCentralDirectoryLength());
    }
}