package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndParseZip64ExtendedInformationExtraField() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(0x100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(0x80L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x200L);
        ZipLong diskStart = new ZipLong(0x300L);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(
            originalSize, compressedSize, relativeHeaderOffset, diskStart);

        byte[] expectedLocalFileData = new byte[2 * DWORD];
        addSizes(expectedLocalFileData);
        byte[] actualLocalFileData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedLocalFileData, actualLocalFileData);

        byte[] expectedCentralDirectoryData = new byte[getCentralDirectoryLength(zip64ExtraField).getValue()];
        int off = addSizes(expectedCentralDirectoryData);
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedCentralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedCentralDirectoryData, off, WORD);
            off += WORD;
        }
        assertArrayEquals(expectedCentralDirectoryData, zip64ExtraField.getCentralDirectoryData());

        byte[] localFileData = new byte[2 * DWORD];
        zip64ExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize.getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), zip64ExtraField.getCompressedSize().getValue());

        byte[] centralDirectoryData = new byte[getCentralDirectoryLength(zip64ExtraField).getValue()];
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize.getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), zip64ExtraField.getCompressedSize().getValue());
        if (relativeHeaderOffset != null) {
            assertEquals(relativeHeaderOffset.getValue(), zip64ExtraField.getRelativeHeaderOffset().getValue());
        }
        if (diskStart != null) {
            assertEquals(diskStart.getValue(), zip64ExtraField.getDiskStartNumber().getValue());
        }
    }

    @Test
    public void testEmptyZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        byte[] expectedLocalFileData = new byte[0];
        byte[] actualLocalFileData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedLocalFileData, actualLocalFileData);

        byte[] expectedCentralDirectoryData = new byte[0];
        zip64ExtraField.parseFromCentralDirectoryData(expectedCentralDirectoryData, 0, expectedCentralDirectoryData.length);
        assertArrayEquals(expectedCentralDirectoryData, zip64ExtraField.getCentralDirectoryData());

        byte[] localFileData = new byte[2 * DWORD];
        try {
            zip64ExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
            assertTrue(false);
        } catch (ZipException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSizesInZip64ExtendedInformationExtraFieldConstructor() {
        new Zip64ExtendedInformationExtraField(null, null);
    }

    private int getCentralDirectoryLength(Zip64ExtendedInformationExtraField zip64ExtraField) {
        int length = 0;
        if (zip64ExtraField.getSize() != null) {
            length += DWORD;
        }
        if (zip64ExtraField.getCompressedSize() != null) {
            length += DWORD;
        }
        if (zip64ExtraField.getRelativeHeaderOffset() != null) {
            length += DWORD;
        }
        if (zip64ExtraField.getDiskStartNumber() != null) {
            length += WORD;
        }
        return length;
    }
}