package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testCreateWithAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((int) 0x01234567);
        ZipLong diskStart = new ZipLong((long) 0x80000001);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize,
                relativeHeaderOffset, diskStart);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullSize() {
        ZipEightByteInteger size = null;

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size,
                new ZipEightByteInteger((long) 512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullCompressedSize() {
        ZipEightByteInteger compressedSize = null;

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger((long) 1024),
                compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithMissingSizeField() {
        byte[] buffer = new byte[2 * DWORD];

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithMissingCompressedSizeField() {
        byte[] buffer = new byte[2 * DWORD + DWORD];

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidSizeFieldLength() {
        byte[] buffer = new byte[2 * DWORD + DWORD + 1];

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        assertEquals(ZipShort.valueOf(0x0001), extraField.getHeaderId());
        assertNull(extraField.getLocalFileDataData());
        assertNull(extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] buffer = new byte[3 * DWORD + DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);

        assertEquals(ZipShort.valueOf(0x0001), extraField.getHeaderId());

        ZipEightByteInteger size = extraField.getSize();
        assertEquals((long) 1024, size.getValue());

        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        assertEquals((long) 512, compressedSize.getValue());

        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertEquals((int) 0x01234567, relativeHeaderOffset.getValue());

        ZipLong diskStart = extraField.getDiskStartNumber();
        assertEquals((long) 0x80000001, diskStart.getValue());

        byte[] localFileData = extraField.getLocalFileDataData();
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        assertEquals(2 * DWORD, localFileData.length);
        assertEquals(3 * DWORD + DWORD + WORD, centralDirectoryData.length);

        byte[] expectedLocalFileData = new byte[2 * DWORD];
        byte[] expectedCentralDirectoryData = new byte[3 * DWORD + DWORD + WORD];

        addSizes(expectedLocalFileData, size.getBytes());
        addSizes(expectedCentralDirectoryData, size.getBytes(), compressedSize.getBytes(),
                relativeHeaderOffset.getBytes(), diskStart.getBytes());

        assertArrayEquals(expectedLocalFileData, localFileData);
        assertArrayEquals(expectedCentralDirectoryData, centralDirectoryData);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(ZipShort.valueOf(0x0001), extraField.getHeaderId());

        ZipEightByteInteger size = extraField.getSize();
        assertEquals((long) 1024, size.getValue());

        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        assertEquals((long) 512, compressedSize.getValue());

        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertEquals((int) 0x01234567, relativeHeaderOffset.getValue());

        ZipLong diskStart = extraField.getDiskStartNumber();
        assertEquals((long) 0x80000001, diskStart.getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutSizeField() throws ZipException {
        byte[] buffer = new byte[3 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, false, true, true);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutCompressedSizeField() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(false, true, true, true);

        ZipEightByteInteger size = extraField.getSize();
        assertEquals((long) 1024, size.getValue());

        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        assertNull(compressedSize);

        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertEquals((int) 0x01234567, relativeHeaderOffset.getValue());

        ZipLong diskStart = extraField.getDiskStartNumber();
        assertEquals((long) 0x80000001, diskStart.getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutRelativeHeaderOffsetField() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);

        ZipEightByteInteger size = extraField.getSize();
        assertEquals((long) 1024, size.getValue());

        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        assertEquals((long) 512, compressedSize.getValue());

        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertNull(relativeHeaderOffset);

        ZipLong diskStart = extraField.getDiskStartNumber();
        assertEquals((long) 0x80000001, diskStart.getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutDiskStartField() throws ZipException {
        byte[] buffer = new byte[3 * DWORD + DWORD + DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);

        ZipEightByteInteger size = extraField.getSize();
        assertEquals((long) 1024, size.getValue());

        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        assertEquals((long) 512, compressedSize.getValue());

        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertEquals((int) 0x01234567, relativeHeaderOffset.getValue());

        ZipLong diskStart = extraField.getDiskStartNumber();
        assertNull(diskStart);
    }

    private void addSizes(byte[] data, ZipEightByteInteger... sizes) {
        int off = 0;
        for (ZipEightByteInteger size : sizes) {
            System.arraycopy(size.getBytes(), 0, data, off, size.size());
            off += size.size();
        }
    }
}