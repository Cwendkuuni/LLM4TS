package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(800));
        assertNotNull(extraField);
        assertEquals(new ZipEightByteInteger(1000), extraField.getSize());
        assertEquals(new ZipEightByteInteger(800), extraField.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(800), new ZipEightByteInteger(50), new ZipLong(1));
        assertNotNull(extraField);
        assertEquals(new ZipEightByteInteger(1000), extraField.getSize());
        assertEquals(new ZipEightByteInteger(800), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(50), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(1), extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[] {0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromLocalFileData(data, 0, 16);
        assertEquals(new ZipEightByteInteger(65536), extraField.getSize());
        assertEquals(new ZipEightByteInteger(65536), extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[] {0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(data, 0, 16);
        assertEquals(new ZipEightByteInteger(65536), extraField.getSize());
        assertEquals(new ZipEightByteInteger(65536), extraField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[] {0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(data, 0, 16);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(new ZipEightByteInteger(65536), extraField.getSize());
        assertEquals(new ZipEightByteInteger(65536), extraField.getCompressedSize());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(800));
        byte[] expected = {0, 3, -24, 3, 0, 0, 0, 0};
        assertArrayEquals(expected, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(800), new ZipEightByteInteger(50), new ZipLong(1));
        byte[] expected = {0, 3, -24, 3, 0, 0, 0, 0, 0, 3, -24, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        assertArrayEquals(expected, extraField.getCentralDirectoryData());
    }
}