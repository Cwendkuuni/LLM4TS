package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndParseZip64ExtraField() {
        ZipEightByteInteger size = new ZipEightByteInteger((int) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(20);
        ZipLong diskStart = new ZipLong(42);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        byte[] expectedLocalFileData = new byte[2 * DWORD];
        zip64ExtraField.addSizes(expectedLocalFileData);

        byte[] expectedCentralDirectoryData = new byte[3 * DWORD + WORD];
        zip64ExtraField.addSizes(expectedCentralDirectoryData);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedCentralDirectoryData, expectedCentralDirectoryData.length - WORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, expectedCentralDirectoryData, expectedCentralDirectoryData.length - WORD * 2, WORD * 2);

        byte[] localFileData = zip64ExtraField.getLocalFileDataData();
        assertEquals(expectedLocalFileData, localFileData);

        byte[] centralDirectoryData = zip64ExtraField.getCentralDirectoryData();
        assertEquals(expectedCentralDirectoryData, centralDirectoryData);

        Zip64ExtendedInformationExtraField parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromCentralDirectoryData(expectedCentralDirectoryData, 0, expectedCentralDirectoryData.length);

        assertEquals(size, parsedExtraField.getSize());
        assertEquals(compressedSize, parsedExtraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, parsedExtraField.getRelativeHeaderOffset());
        assertEquals(diskStart, parsedExtraField.getDiskStartNumber());
    }

    @Test
    public void testParseEmptyLocalFileData() {
        byte[] emptyLocalFileData = new byte[0];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromLocalFileData(emptyLocalFileData, 0, emptyLocalFileData.length);
        assertNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseLocalFileDataMissingSizeFields() {
        byte[] invalidLocalFileData = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromLocalFileData(invalidLocalFileData, 0, invalidLocalFileData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseCentralDirectoryDataMissingAllFields() {
        byte[] missingFieldData = new byte[0];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(missingFieldData, 0, missingFieldData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseCentralDirectoryDataMissingUncompressedSizeField() {
        byte[] missingUncompressedSizeData = new byte[DWORD];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(missingUncompressedSizeData, 0, missingUncompressedSizeData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseCentralDirectoryDataMissingCompressedSizeField() {
        byte[] missingCompressedSizeData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(missingCompressedSizeData, 0, missingCompressedSizeData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseCentralDirectoryDataMissingRelativeHeaderOffsetField() {
        byte[] missingRelativeHeaderOffsetData = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(missingRelativeHeaderOffsetData, 0, missingRelativeHeaderOffsetData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseCentralDirectoryDataMissingDiskStartField() {
        byte[] missingDiskStartData = new byte[3 * DWORD + WORD - 2];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(missingDiskStartData, 0, missingDiskStartData.length);
    }

    @Test
    public void testParseCentralDirectoryDataWithPartialFields() {
        byte[] partialData = new byte[2 * DWORD + WORD - 2];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(partialData, 0, partialData.length);
        assertNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
    }

    @Test
    public void testParseCentralDirectoryDataWithAllFields() {
        byte[] allFieldsData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(allFieldsData, 0, allFieldsData.length);

        ZipEightByteInteger expectedSize = new ZipEightByteInteger((int) 1024);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((int) 512);
        ZipEightByteInteger expectedRelativeHeaderOffset = new ZipEightByteInteger(20);
        ZipLong expectedDiskStart = new ZipLong(42);

        assertEquals(expectedSize, zip64ExtraField.getSize());
        assertEquals(expectedCompressedSize, zip64ExtraField.getCompressedSize());
        assertEquals(expectedRelativeHeaderOffset, zip64ExtraField.getRelativeHeaderOffset());
        assertEquals(expectedDiskStart, zip64ExtraField.getDiskStartNumber());
    }
}