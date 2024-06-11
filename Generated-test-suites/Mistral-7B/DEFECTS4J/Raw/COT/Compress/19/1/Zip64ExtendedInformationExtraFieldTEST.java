package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField()}.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger)}.
     */
    @org.junit.Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger, ZipEightByteInteger, ZipLong)}.
     */
    @org.junit.Test
    public void testConstructorWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStart = new ZipLong(0x23456789);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getHeaderId()}.
     */
    @org.junit.Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0x0001), field.getHeaderId());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getLocalFileDataLength()}.
     */
    @org.junit.Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Zip64ExtendedInformationExtraField fieldWithSizes = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L));
        assertEquals(ZipShort.valueOf(0), field.getLocalFileDataLength());
        assertEquals(ZipShort.valueOf(2 * DWORD), fieldWithSizes.getLocalFileDataLength());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getCentralDirectoryLength()}.
     */
    @org.junit.Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField fieldWithoutSizes = new Zip64ExtendedInformationExtraField();
        Zip64ExtendedInformationExtraField fieldWithSizes = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L));
        Zip64ExtendedInformationExtraField fieldWithAllFields = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L), new ZipEightByteInteger(0x1234), new ZipLong(0x23456789));
        assertEquals(ZipShort.valueOf(0), fieldWithoutSizes.getCentralDirectoryLength());
        assertEquals(ZipShort.valueOf(2 * DWORD), fieldWithSizes.getCentralDirectoryLength());
        assertEquals(ZipShort.valueOf(2 * DWORD + DWORD + DWORD + WORD), fieldWithAllFields.getCentralDirectoryLength());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getLocalFileDataData()}.
     */
    @org.junit.Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L));
        byte[] data = field.getLocalFileDataData();
        assertEquals(2 * DWORD, data.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals(0, data[i] & 0xFF);
            assertEquals(i < DWORD ? (i % 2 == 0 ? (byte) 0x00 : (byte) 0xFF) : (byte) 0x00, data[i] >> 8);
        }
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getCentralDirectoryData()}.
     */
    @org.junit.Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField fieldWithAllFields = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(12345L), new ZipEightByteInteger(6789L), new ZipEightByteInteger(0x1234), new ZipLong(0x23456789));
        byte[] data = fieldWithAllFields.getCentralDirectoryData();
        assertEquals(2 * DWORD + DWORD + DWORD + WORD, data.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals(0, data[i] & 0xFF);
            assertEquals(i < DWORD ? (i % 2 == 0 ? (byte) 0x00 : (byte) 0xFF) : (i % 8 == 0 ? (byte) 0x00 : (byte) 0xFF), data[i] >> 8);
        }
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromLocalFileData(byte[], int, int)}.
     */
    @org.junit.Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        assertEquals(new ZipEightByteInteger(12345L), field.getSize());
        assertEquals(new ZipEightByteInteger(6789L), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}.
     */
    @org.junit.Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[] { 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00,
            0x00, 0x23, 0x45, 0x67, 0x89, 0x00, 0x00, 0x00, 0x00 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(new ZipEightByteInteger(12345L), field.getSize());
        assertEquals(new ZipEightByteInteger(6789L), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0x1234), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x23456789), field.getDiskStartNumber());

        byte[] expectedData = new byte[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00,
            0x00, 0x23, 0x45, 0x67, 0x89, 0x00, 0x00, 0x00, 0x00 };
        assertEquals(expectedData, field.getLocalFileDataData());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#reparseCentralDirectoryData(boolean, boolean, boolean, boolean)}.
     */
    @org.junit.Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] data = new byte[] { 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00,
            0x00, 0x23, 0x45, 0x67, 0x89, 0x00, 0x00, 0x00, 0x00 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        byte[] rawData = field.getRawCentralDirectoryData();

        field.reparseCentralDirectoryData(true, true, true, true);
        Zip64ExtendedInformationExtraField reParsedField = new Zip64ExtendedInformationExtraField();
        reParsedField.parseFromCentralDirectoryData(rawData, 0, rawData.length);
        assertEquals(field.getSize(), reParsedField.getSize());
        assertEquals(field.getCompressedSize(), reParsedField.getCompressedSize());
        assertEquals(field.getRelativeHeaderOffset(), reParsedField.getRelativeHeaderOffset());
        assertEquals(field.getDiskStartNumber(), reParsedField.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidData() throws ZipException {
        byte[] data = new byte[] { 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
    }
}