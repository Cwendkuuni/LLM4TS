package org.apache.commons.compress.archivers.zip; // Make sure the package matches the class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testDefaultConstructor() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
    }

    @Test
    public void testSizesConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        Zip64ExtendedInformationExtraField field =
                new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
    }

    @Test
    public void testAllFieldsConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4321L);
        ZipLong diskStart = new ZipLong(0x1234567890L);
        Zip64ExtendedInformationExtraField field =
                new Zip64ExtendedInformationExtraField(size, compressedSize,
                        relativeHeaderOffset, diskStart);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L));
        assertEquals(ZipShort.valueOf(2 * ZipConstants.DWORD), field.getLocalFileDataLength());
        field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L),
                new ZipEightByteInteger(4321L), new ZipLong(0x1234567890L));
        assertEquals(ZipShort.valueOf(2 * ZipConstants.DWORD + DWORD + WORD),
                field.getCentralDirectoryLength());
        field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L));
        byte[] data = field.getLocalFileDataData();
        assertEquals(2 * ZipConstants.DWORD, data.length);
        Zip64ExtendedInformationExtraField field2 =
                new Zip64ExtendedInformationExtraField();
        byte[] data2 = field2.getLocalFileDataData();
        assertEquals(0, data2.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L),
                new ZipEightByteInteger(4321L), new ZipLong(0x1234567890L));
        byte[] data = field.getCentralDirectoryData();
        assertEquals(2 * ZipConstants.DWORD + DWORD + WORD, data.length);
        Zip64ExtendedInformationExtraField field2 =
                new Zip64ExtendedInformationExtraField();
        byte[] data2 = field2.getCentralDirectoryData();
        assertEquals(0, data2.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        Zip64ExtendedInformationExtraField field =
                new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        assertEquals(new ZipEightByteInteger(12345L), field.getSize());
        assertEquals(new ZipEightByteInteger(6789L), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
        byte[] data2 = {
                0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        field = new Zip64ExtendedInformationExtraField();
        try {
            field.parseFromLocalFileData(data2, 0, data2.length);
            assert false; // Should throw an exception
        } catch (ZipException e) {
            // Expected
        }
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] data = {
                0x02, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x03, 0x04, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x01
        };
        Zip64ExtendedInformationExtraField field =
                new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(new ZipEightByteInteger(12345L), field.getSize());
        assertEquals(new ZipEightByteInteger(6789L), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(4321L), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x1234567890L), field.getDiskStartNumber());
        try {
            field.parseFromCentralDirectoryData(new byte[0], 0, 0);
            assert false; // Should throw an exception
        } catch (ZipException e) {
            // Expected
        }
        try {
            field.parseFromCentralDirectoryData(data, 1, data.length);
            assert false; // Should throw an exception
        } catch (ZipException e) {
            // Expected
        }
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] data = {
                0x02, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x03, 0x04, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x01
        };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(12345L), field.getSize());
        assertEquals(new ZipEightByteInteger(6789L), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(4321L), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x1234567890L), field.getDiskStartNumber());
        data = new byte[0];
        try {
            field.reparseCentralDirectoryData(true, true, true, true);
            assert false; // Should throw an exception
        } catch (ZipException e) {
            // Expected
        }
        data = new byte[] {
                0x02, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x03, 0x04, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x01,
                0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x01
        };
        field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(false, true, true, true);
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
    }
}