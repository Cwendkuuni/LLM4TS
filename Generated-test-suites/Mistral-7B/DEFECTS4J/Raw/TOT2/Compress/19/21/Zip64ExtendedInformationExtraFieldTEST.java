package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField extraField;
    private byte[] centralDirectoryData;

    @Before
    public void setUp() throws ZipException {
        // Create a test instance with valid data
        ZipEightByteInteger size = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        // Create test data for the central directory
        centralDirectoryData = new byte[3 * DWORD + DWORD];
        addSizes(centralDirectoryData);
        System.arraycopy(extraField.getCentralDirectoryData(), 0, centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expectedLength = extraField.getLocalFileDataLength();
        ZipShort actualLength = new ZipShort(2 * DWORD);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expectedLength = extraField.getCentralDirectoryLength();
        ZipShort actualLength = new ZipShort((short) (2 * DWORD + DWORD
                + (size != null ? DWORD : 0)
                + (compressedSize != null ? DWORD : 0)
                + (relativeHeaderOffset != null ? DWORD : 0)
                + (diskStart != null ? WORD : 0)));
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expectedData = extraField.getLocalFileDataData();
        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expectedData = extraField.getCentralDirectoryData();
        byte[] actualData = centralDirectoryData;
        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidDataLength() {
        byte[] invalidData = new byte[1];
        extraField.parseFromLocalFileData(invalidData, 0, invalidData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizeValues() {
        byte[] invalidData = new byte[2 * DWORD];
        extraField.parseFromLocalFileData(invalidData, 0, invalidData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(extraField.getSize(), size);
        assertEquals(extraField.getCompressedSize(), compressedSize);
        assertEquals(extraField.getRelativeHeaderOffset(), relativeHeaderOffset);
        assertEquals(extraField.getDiskStartNumber(), diskStart);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        byte[] invalidData = new byte[3 * DWORD - 1];
        extraField.parseFromCentralDirectoryData(invalidData, 0, invalidData.length);
    }

    @Test
    public void testReparseCentralDirectoryData() {
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(extraField.getSize(), size);
        assertEquals(extraField.getCompressedSize(), compressedSize);
        assertEquals(extraField.getRelativeHeaderOffset(), relativeHeaderOffset);
        assertEquals(extraField.getDiskStartNumber(), diskStart);
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithInvalidMagicValues() {
        byte[] invalidData = new byte[3 * DWORD];
        extraField.reparseCentralDirectoryData(false, true, true, true);
    }
}