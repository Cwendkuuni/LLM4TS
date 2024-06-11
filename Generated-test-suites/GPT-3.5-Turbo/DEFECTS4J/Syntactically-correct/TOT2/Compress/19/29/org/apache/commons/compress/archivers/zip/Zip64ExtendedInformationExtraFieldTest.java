package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), zip64ExtraField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), zip64ExtraField.getLocalFileDataLength());
        zip64ExtraField.setSize(new ZipEightByteInteger(100));
        zip64ExtraField.setCompressedSize(new ZipEightByteInteger(50));
        assertEquals(new ZipShort(16), zip64ExtraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), zip64ExtraField.getCentralDirectoryLength());
        zip64ExtraField.setSize(new ZipEightByteInteger(100));
        zip64ExtraField.setCompressedSize(new ZipEightByteInteger(50));
        zip64ExtraField.setRelativeHeaderOffset(new ZipEightByteInteger(20));
        zip64ExtraField.setDiskStartNumber(new ZipLong(2));
        assertEquals(new ZipShort(28), zip64ExtraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.setSize(new ZipEightByteInteger(100));
        zip64ExtraField.setCompressedSize(new ZipEightByteInteger(50));

        byte[] expected = {100, 0, 0, 0, 50, 0, 0, 0};
        assertArrayEquals(expected, zip64ExtraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.setSize(new ZipEightByteInteger(100));
        zip64ExtraField.setCompressedSize(new ZipEightByteInteger(50));
        zip64ExtraField.setRelativeHeaderOffset(new ZipEightByteInteger(20));
        zip64ExtraField.setDiskStartNumber(new ZipLong(2));

        byte[] expected = {100, 0, 0, 0, 50, 0, 0, 0, 20, 0, 0, 0, 2, 0, 0, 0};
        assertArrayEquals(expected, zip64ExtraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {100, 0, 0, 0, 50, 0, 0, 0};
        zip64ExtraField.parseFromLocalFileData(data, 0, 8);

        assertEquals(new ZipEightByteInteger(100), zip64ExtraField.getSize());
        assertEquals(new ZipEightByteInteger(50), zip64ExtraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {100, 0, 0, 0, 50, 0, 0, 0, 20, 0, 0, 0, 2, 0, 0, 0};
        zip64ExtraField.parseFromCentralDirectoryData(data, 0, 16);

        assertEquals(new ZipEightByteInteger(100), zip64ExtraField.getSize());
        assertEquals(new ZipEightByteInteger(50), zip64ExtraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(20), zip64ExtraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(2), zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {100, 0, 0, 0, 50, 0, 0, 0, 20, 0, 0, 0, 2, 0, 0, 0};
        zip64ExtraField.parseFromCentralDirectoryData(data, 0, 16);
        zip64ExtraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(new ZipEightByteInteger(100), zip64ExtraField.getSize());
        assertEquals(new ZipEightByteInteger(50), zip64ExtraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(20), zip64ExtraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(2), zip64ExtraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingSize() throws ZipException {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {100, 0, 0, 0};
        zip64ExtraField.parseFromLocalFileData(data, 0, 4);
    }

}