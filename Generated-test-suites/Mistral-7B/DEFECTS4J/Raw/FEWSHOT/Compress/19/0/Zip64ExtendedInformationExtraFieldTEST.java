package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;
import static org.apache.commons.compress.archivers.zip.ZipConstants.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {
    @Test
    public void testCreateAndParseZip64ExtendedInformationExtraField() {
        ZipEightByteInteger origSize = new ZipEightByteInteger((int) 1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((int) 5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(12);
        ZipLong diskStart = new ZipLong(13);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(origSize, compressedSize, relativeHeaderOffset, diskStart);

        byte[] expectedData = new byte[2 * DWORD + DWORD + WORD];
        int offset = 0;
        if (origSize != null) {
            System.arraycopy(origSize.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, expectedData, offset, WORD);
            offset += WORD;
        }

        byte[] actualData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);

        Zip64ExtendedInformationExtraField parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromLocalFileData(actualData, 0, actualData.length);

        assertEquals(origSize, parsedExtraField.getSize());
        assertEquals(compressedSize, parsedExtraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, parsedExtraField.getRelativeHeaderOffset());
        assertEquals(diskStart, parsedExtraField.getDiskStartNumber());
    }

    @Test
    public void testCreateAndParseZip64ExtendedInformationExtraFieldWithoutAllFields() {
        ZipEightByteInteger origSize = new ZipEightByteInteger((int) 1234);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(origSize, null, null, null);

        byte[] expectedData = new byte[2 * DWORD];
        if (origSize != null) {
            System.arraycopy(origSize.getBytes(), 0, expectedData, 0, DWORD);
        }

        byte[] actualData = zip64ExtraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);

        Zip64ExtendedInformationExtraField parsedExtraField = new Zip64ExtendedInformationExtraField();
        parsedExtraField.parseFromLocalFileData(actualData, 0, actualData.length);

        assertEquals(origSize, parsedExtraField.getSize());
        assertNull(parsedExtraField.getCompressedSize());
        assertNull(parsedExtraField.getRelativeHeaderOffset());
        assertNull(parsedExtraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateZip64ExtendedInformationExtraFieldWithNullSizes() {
        new Zip64ExtendedInformationExtraField(null, null);
    }
}