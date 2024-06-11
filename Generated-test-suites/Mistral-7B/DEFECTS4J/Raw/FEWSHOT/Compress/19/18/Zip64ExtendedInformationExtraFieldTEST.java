package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testZip64ExtendedInformationExtraField() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 1234);
        ZipLong diskStart = new ZipLong((long) 5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(HEADER_ID, extraField.getHeaderId());
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
        assertEquals(2 * DWORD, extraField.getLocalFileDataLength().getValue());
        assertEquals(2 * DWORD + DWORD, extraField.getCentralDirectoryLength().getValue());

        byte[] localFileData = extraField.getLocalFileDataData();
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        // test parsing from local file data
        Zip64ExtendedInformationExtraField parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(extraField, parsedExtraField);

        // test parsing from central directory data
        byte[] centralDirectoryDataWithSize = new byte[centralDirectoryData.length + DWORD];
        System.arraycopy(centralDirectoryData, 0, centralDirectoryDataWithSize, 0, centralDirectoryData.length);
        size.getBytes(centralDirectoryDataWithSize, centralDirectoryData.length);
        parsedExtraField.parseFromCentralDirectoryData(centralDirectoryDataWithSize, 0, centralDirectoryDataWithSize.length);
        assertEquals(extraField, parsedExtraField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZip64ExtendedInformationExtraFieldNullSizes() {
        ZipEightByteInteger size = null;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZip64ExtendedInformationExtraFieldNullCompressedSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = null;
        new Zip64ExtendedInformationExtraField(size, compressedSize);
    }

    @Test(expected = IllegalStateException.class)
    public void testZip64ExtendedInformationExtraFieldParseFromCentralDirectoryDataNoSizeData() {
        byte[] centralDirectoryData = new byte[WORD];
        new Zip64ExtendedInformationExtraField().parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }
}