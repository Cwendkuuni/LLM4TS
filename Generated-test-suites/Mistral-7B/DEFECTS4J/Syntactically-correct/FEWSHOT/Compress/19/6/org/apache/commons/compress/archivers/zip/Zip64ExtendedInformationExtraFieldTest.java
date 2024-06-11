package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndGetSize() {
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((long) 1024);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(expectedSize, null);
        ZipEightByteInteger actualSize = extraField.getSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testCreateAndGetCompressedSize() {
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, expectedCompressedSize);
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(expectedCompressedSize, actualCompressedSize);
    }

    @Test
    public void testCreateAndGetRelativeHeaderOffset() {
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger((long) 1234);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null, expectedRelativeHeaderOffset, null);
        ZipEightByteInteger actualRelativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);
    }

    @Test
    public void testCreateAndGetDiskStartNumber() {
        ZipLong expectedDiskStartNumber = new ZipLong((long) 5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null, null, expectedDiskStartNumber);
        ZipLong actualDiskStartNumber = extraField.getDiskStartNumber();
        assertEquals(expectedDiskStartNumber, actualDiskStartNumber);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        System.arraycopy(expectedSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(expectedCompressedSize.getBytes(), 0, localFileData, DWORD, DWORD);
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedCompressedSize, actualCompressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataNoSizeValues() {
        byte[] localFileData = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Expected exception: " + Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataInvalidSizeValues() {
        byte[] localFileData = new byte[2 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Expected exception: " + Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null);
        assertEquals(0, extraField.getLocalFileDataLength().getValue());
        extraField.setSize(new ZipEightByteInteger((long) 1024));
        extraField.setCompressedSize(new ZipEightByteInteger((long) 512));
        assertEquals(2 * DWORD, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(null, null);
        assertEquals(0, extraField.getCentralDirectoryLength().getValue());
        extraField.setSize(new ZipEightByteInteger((long) 1024));
        extraField.setCompressedSize(new ZipEightByteInteger((long) 512));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger((long) 1234));
        extraField.setDiskStartNumber(new ZipLong((long) 5678));
        assertEquals(2 * DWORD + DWORD + WORD, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[2 * DWORD + DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        byte[] actualData = extraField.getCentralDirectoryData();
        assertEquals(centralDirectoryData.length, actualData.length);
        assertEquals(centralDirectoryData, actualData);
    }

    @Test
    public void testParseFromCentralDirectoryDataNoSizeValues() {
        byte[] centralDirectoryData = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataInvalidLength() {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataValidData() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        ZipEightByteInteger actualRelativeHeaderOffset = extraField.getRelativeHeaderOffset();
        ZipLong actualDiskStartNumber = extraField.getDiskStartNumber();
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger((long) 1234);
        ZipLong expectedDiskStartNumber = new ZipLong((long) 5678);
        if (actualSize != null) {
            assertEquals(expectedSize, actualSize);
        } else {
            fail("Unexpected null value for size");
        }
        if (actualCompressedSize != null) {
            assertEquals(expectedCompressedSize, actualCompressedSize);
        } else {
            fail("Unexpected null value for compressed size");
        }
        if (actualRelativeHeaderOffset != null) {
            assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);
        } else {
            fail("Unexpected null value for relative header offset");
        }
        if (actualDiskStartNumber != null) {
            assertEquals(expectedDiskStartNumber, actualDiskStartNumber);
        } else {
            fail("Unexpected null value for disk start number");
        }
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        ZipEightByteInteger actualSize = extraField.getSize();
        ZipEightByteInteger actualCompressedSize = extraField.getCompressedSize();
        ZipEightByteInteger actualRelativeHeaderOffset = extraField.getRelativeHeaderOffset();
        ZipLong actualDiskStartNumber = extraField.getDiskStartNumber();
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger((long) 1234);
        ZipLong expectedDiskStartNumber = new ZipLong((long) 5678);
        if (actualSize != null) {
            assertEquals(expectedSize, actualSize);
        } else {
            fail("Unexpected null value for size");
        }
        if (actualCompressedSize != null) {
            assertEquals(expectedCompressedSize, actualCompressedSize);
        } else {
            fail("Unexpected null value for compressed size");
        }
        if (actualRelativeHeaderOffset != null) {
            assertEquals(expectedRelativeHeaderOffset, actualRelativeHeaderOffset);
        } else {
            fail("Unexpected null value for relative header offset");
        }
        if (actualDiskStartNumber != null) {
            assertEquals(expectedDiskStartNumber, actualDiskStartNumber);
        } else {
            fail("Unexpected null value for disk start number");
        }
    }
}