package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testNewInstanceWithBothSizes() {
        ZipEightByteInteger origSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(origSize, compressedSize);
        assertNotNull(zip64ExtraField.getSize());
        assertNotNull(zip64ExtraField.getCompressedSize());
        assertEquals(origSize.getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), zip64ExtraField.getCompressedSize().getValue());
    }

    @Test
    public void testNewInstanceWithOnlySizes() {
        ZipEightByteInteger origSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(origSize, compressedSize);

        byte[] localFileData = zip64ExtraField.getLocalFileDataData();
        assertEquals(localFileData.length, 2 * DWORD);

        ZipEightByteInteger localSize = new ZipEightByteInteger(localFileData, 0);
        assertEquals(origSize.getValue(), localSize.getValue());

        ZipEightByteInteger localCompressedSize = new ZipEightByteInteger(localFileData, DWORD);
        assertEquals(compressedSize.getValue(), localCompressedSize.getValue());
    }

    @Test
    public void testNewInstanceWithAllFields() {
        ZipEightByteInteger origSize = new ZipEightByteInteger((short) 0x12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 0x9abcdef0);
        ZipEightByteInteger rho = new ZipEightByteInteger((short) 0x00001234);
        ZipLong ds = new ZipLong((int) 0x11223344);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField(origSize, compressedSize, rho, ds);
        assertNotNull(zip64ExtraField.getSize());
        assertNotNull(zip64ExtraField.getCompressedSize());
        assertNotNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNotNull(zip64ExtraField.getDiskStartNumber());
        assertEquals(origSize.getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), zip64ExtraField.getCompressedSize().getValue());
        assertEquals(rho.getValue(), zip64ExtraField.getRelativeHeaderOffset().getValue());
        assertEquals(ds.getValue(), zip64ExtraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[2 * DWORD];
        addSizes(localFileData);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertNotNull(zip64ExtraField.getSize());
        assertNotNull(zip64ExtraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(localFileData, 0).getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(new ZipEightByteInteger(localFileData, DWORD).getValue(), zip64ExtraField.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];

        // Test with all fields present
        System.arraycopy(new ZipEightByteInteger(WORD, 0).getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryData, 3 * DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(WORD, 0).getBytes(), 0, centralDirectoryData, 4 * DWORD, WORD);

        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertNotNull(zip64ExtraField.getSize());
        assertNotNull(zip64ExtraField.getCompressedSize());
        assertNotNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNotNull(zip64ExtraField.getDiskStartNumber());
        assertEquals(new ZipEightByteInteger(centralDirectoryData, 0).getValue(), zip64ExtraField.getSize().getValue());
        assertEquals(new ZipEightByteInteger(centralDirectoryData, DWORD).getValue(), zip64ExtraField.getCompressedSize().getValue());
        assertEquals(new ZipEightByteInteger(centralDirectoryData, 2 * DWORD).getValue(), zip64ExtraField.getRelativeHeaderOffset().getValue());
        assertEquals(new ZipLong(centralDirectoryData, 3 * DWORD).getValue(), zip64ExtraField.getDiskStartNumber().getValue());

        // Test with only size fields present
        byte[] centralDirectoryDataSize = new byte[3 * DWORD];
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataSize, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataSize, DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataSize, 2 * DWORD, DWORD);

        zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryDataSize, 0, centralDirectoryDataSize.length);

        assertNotNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
        assertEquals(new ZipEightByteInteger(centralDirectoryDataSize, 0).getValue(), zip64ExtraField.getSize().getValue());
        assertNull(zip64ExtraField.getCompressedSize());

        // Test with only compressed size fields present
        byte[] centralDirectoryDataCompressedSize = new byte[3 * DWORD];
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataCompressedSize, DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataCompressedSize, 2 * DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataCompressedSize, 3 * DWORD, DWORD);

        zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryDataCompressedSize, 0, centralDirectoryDataCompressedSize.length);

        assertNull(zip64ExtraField.getSize());
        assertNotNull(zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
        assertEquals(new ZipEightByteInteger(centralDirectoryDataCompressedSize, DWORD).getValue(), zip64ExtraField.getCompressedSize().getValue());
        assertNull(zip64ExtraField.getSize());

        // Test with only relative header offset fields present
        byte[] centralDirectoryDataRho = new byte[3 * DWORD + WORD];
        System.arraycopy(new ZipEightByteInteger(WORD, 0).getBytes(), 0, centralDirectoryDataRho, 0, WORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataRho, WORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataRho, 2 * DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataRho, 3 * DWORD, DWORD);

        zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryDataRho, 0, centralDirectoryDataRho.length);

        assertNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
        assertNotNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNull(zip64ExtraField.getDiskStartNumber());
        assertEquals(new ZipEightByteInteger(centralDirectoryDataRho, WORD).getValue(), zip64ExtraField.getRelativeHeaderOffset().getValue());
        assertNull(zip64ExtraField.getSize());

        // Test with only disk start number fields present
        byte[] centralDirectoryDataDiskStart = new byte[3 * DWORD + WORD];
        System.arraycopy(new ZipEightByteInteger(WORD, 0).getBytes(), 0, centralDirectoryDataDiskStart, 0, WORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataDiskStart, DWORD, DWORD);
        System.arraycopy(new ZipEightByteInteger(DWORD, 0).getBytes(), 0, centralDirectoryDataDiskStart, 2 * DWORD, DWORD);
        System.arraycopy(new ZipLong(0, 0).getBytes(), 0, centralDirectoryDataDiskStart, 3 * DWORD, WORD);

        zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryDataDiskStart, 0, centralDirectoryDataDiskStart.length);

        assertNull(zip64ExtraField.getSize());
        assertNull(zip64ExtraField.getCompressedSize());
        assertNull(zip64ExtraField.getRelativeHeaderOffset());
        assertNotNull(zip64ExtraField.getDiskStartNumber());
        assertEquals(new ZipLong(centralDirectoryDataDiskStart, 3 * DWORD).getValue(), zip64ExtraField.getDiskStartNumber().getValue());
        assertNull(zip64ExtraField.getSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() throws ZipException {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, data, off, WORD);
            off += WORD;
        }
    }
}