package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateAndGetLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024), new ZipEightByteInteger(512));
        byte[] localFileData = extraField.getLocalFileDataData();
        ZipShort localFileDataLength = extraField.getLocalFileDataLength();

        assertEquals(2 * DWORD, localFileDataLength.getValue());
        assertEquals(localFileDataLength.getValue(), localFileData.length);
        assertEquals(1024, ((ZipEightByteInteger) ZipShort.parse(localFileData, 0)).getValue());
        assertEquals(512, ((ZipEightByteInteger) ZipShort.parse(localFileData, DWORD)).getValue());
    }

    @Test
    public void testCreateAndGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024), new ZipEightByteInteger(512),
                new ZipEightByteInteger(4), new ZipLong(2));
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();
        ZipShort centralDirectoryLength = extraField.getCentralDirectoryLength();

        assertEquals(4 * DWORD + WORD, centralDirectoryLength.getValue());
        assertEquals(centralDirectoryLength.getValue(), centralDirectoryData.length);
        assertEquals(1024, ((ZipEightByteInteger) ZipShort.parse(centralDirectoryData, 0)).getValue());
        assertEquals(512, ((ZipEightByteInteger) ZipShort.parse(centralDirectoryData, DWORD)).getValue());
        assertEquals(4, ((ZipEightByteInteger) ZipShort.parse(centralDirectoryData, 2 * DWORD)).getValue());
        assertEquals(2, ((ZipLong) ZipShort.parse(centralDirectoryData, 3 * DWORD)).getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[] {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(new ZipEightByteInteger(8), extraField.getSize());
        assertEquals(new ZipEightByteInteger(8), extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        byte[] localFileData = new byte[] { 0x00, 0x01 };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        fail("ZipException should have been thrown");
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x02
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(new ZipEightByteInteger(8), extraField.getSize());
        assertEquals(new ZipEightByteInteger(8), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(4), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(2), extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithMissingSizes() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        fail("ZipException should have been thrown");
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x00
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(new ZipEightByteInteger(8), extraField.getSize());
        assertEquals(new ZipEightByteInteger(8), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testReParseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x02
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        byte[] rawCentralDirectoryData = extraField.getRawCentralDirectoryData();
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(new ZipEightByteInteger(8), extraField.getSize());
        assertEquals(new ZipEightByteInteger(8), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(4), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(2), extraField.getDiskStartNumber());
        assertArrayEquals(centralDirectoryData, extraField.getRawCentralDirectoryData());
    }

    @Test
    public void testReParseCentralDirectoryDataWithMissingSizes() throws ZipException {
        byte[] centralDirectoryData = new byte[] {
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        byte[] rawCentralDirectoryData = extraField.getRawCentralDirectoryData();
        extraField.reparseCentralDirectoryData(true, false, true, true);

        assertEquals(new ZipEightByteInteger(8), extraField.getSize());
        assertEquals(new ZipEightByteInteger(8), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
        assertArrayEquals(centralDirectoryData, extraField.getRawCentralDirectoryData());
    }
}