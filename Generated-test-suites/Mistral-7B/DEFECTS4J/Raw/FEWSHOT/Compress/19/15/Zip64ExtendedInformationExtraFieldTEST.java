package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x3456);
        ZipLong diskStart = new ZipLong((int) 0x7890);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[16];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, 4, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataMissingSizes() {
        byte[] localFileData = new byte[8];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("Should have thrown ZipException");
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[32];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x3456);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, 4, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataMissingSizes() {
        byte[] centralDirectoryData = new byte[16];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("Should have thrown ZipException");
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[32];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 0x1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((short) 0x3456);
        ZipLong diskStart = new ZipLong((int) 0x7890);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, 4, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 12, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 16, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }
}