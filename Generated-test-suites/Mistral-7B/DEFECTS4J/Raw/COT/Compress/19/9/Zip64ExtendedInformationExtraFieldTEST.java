package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;

/**
 * Test cases for {@link Zip64ExtendedInformationExtraField} class.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithNullSizes() {
        try {
            new Zip64ExtendedInformationExtraField(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(1L));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1L), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf("0x0001"), testField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLengthWithPresentSizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1L), new ZipEightByteInteger(2L));
        assertEquals(ZipShort.valueOf(2 * ZipConstants.DWORD), testField.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataLengthWithEmptySizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0), testField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithPresentSizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1L), new ZipEightByteInteger(2L), null, null);
        assertEquals(ZipShort.valueOf(1 * ZipConstants.DWORD + 2 * ZipConstants.DWORD), testField.getCentralDirectoryLength());
    }

    @Test
    public void testGetCentralDirectoryLengthWithEmptySizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0), testField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataDataWithPresentSizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1L), new ZipEightByteInteger(2L));
        byte[] expectedData = new byte[2 * ZipConstants.DWORD];
        testField.addSizes(expectedData);
        byte[] actualData = testField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetLocalFileDataDataWithEmptySizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        byte[] actualData = testField.getLocalFileDataData();
        byte[] expectedEmptyData = new byte[0];
        assertArrayEquals(expectedEmptyData, actualData);
    }

    @Test
    public void testGetCentralDirectoryDataWithPresentSizes() {
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1L), new ZipEightByteInteger(2L), null, null);
        byte[] expectedData = new byte[testField.getCentralDirectoryLength().getValue()];
        testField.addSizes(expectedData);
        byte[] actualData = testField.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileDataWithPresentSizes() {
        byte[] localFileData = new byte[] { 0x00, 0x01, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00,
                0x03, 0x00, 0x03, 0x00, 0x04, 0x00, 0x04, 0x00 };
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        testField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(new ZipEightByteInteger(1L), testField.getSize());
        assertEquals(new ZipEightByteInteger(2L), testField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithNoSizes() {
        byte[] localFileData = new byte[] { 0x00, 0x01, 0x00, 0x00 };
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        testField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Expected ZipException");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithMissingFields() {
        byte[] centralDirectoryData = new byte[] { 0x00, 0x01, 0x00, 0x04, 0x02, 0x00, 0x03, 0x00,
                0x03, 0x00 };
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        testField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Expected ZipException");
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidFieldOrder() {
        byte[] centralDirectoryData = new byte[] { 0x00, 0x03, 0x00, 0x03, 0x00, 0x04, 0x02, 0x00,
                0x01, 0x00, 0x01, 0x00 };
        Zip64ExtendedInformationExtraField testField = new Zip64ExtendedInformationExtraField();
        testField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Expected ZipException");
    }
}