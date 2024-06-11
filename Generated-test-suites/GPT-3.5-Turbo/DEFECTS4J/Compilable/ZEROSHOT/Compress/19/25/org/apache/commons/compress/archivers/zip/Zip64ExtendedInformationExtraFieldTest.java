package org.apache.commons.compress.archivers.zip;
import org.junit.Before;
import org.junit.Test;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField field;

    @Before
    public void setUp() {
        field = new Zip64ExtendedInformationExtraField();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(new ZipShort(16), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(20), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expectedData = {(byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x40, (byte) 0x00};
        field.setSize(new ZipEightByteInteger(4096));
        field.setCompressedSize(new ZipEightByteInteger(8192));
        assertArrayEquals(expectedData, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expectedData = {(byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        field.setSize(new ZipEightByteInteger(4096));
        field.setCompressedSize(new ZipEightByteInteger(8192));
        field.setRelativeHeaderOffset(new ZipEightByteInteger(16384));
        field.setDiskStartNumber(new ZipLong(0));
        assertArrayEquals(expectedData, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = {(byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x40, (byte) 0x00};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(4096), field.getSize());
        assertEquals(new ZipEightByteInteger(8192), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = {(byte) 0x01, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(4096), field.getSize());
        assertEquals(new ZipEightByteInteger(8192), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(16384), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0), field.getDiskStartNumber());
    }

    @Test
    public void testSetSize() {
        field.setSize(new ZipEightByteInteger(4096));
        assertEquals(new ZipEightByteInteger(4096), field.getSize());
    }

    @Test
    public void testSetCompressedSize() {
        field.setCompressedSize(new ZipEightByteInteger(8192));
        assertEquals(new ZipEightByteInteger(8192), field.getCompressedSize());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        field.setRelativeHeaderOffset(new ZipEightByteInteger(16384));
        assertEquals(new ZipEightByteInteger(16384), field.getRelativeHeaderOffset());
    }

    @Test
    public void testSetDiskStartNumber() {
        field.setDiskStartNumber(new ZipLong(0));
        assertEquals(new ZipLong(0), field.getDiskStartNumber());
    }
}