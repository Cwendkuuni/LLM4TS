package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(2 * ZipConstants.DWORD, field.getLocalFileDataLength().getValue());

        Zip64ExtendedInformationExtraField fieldWithSizes = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(new ZipLong(100)),
                new ZipEightByteInteger(new ZipLong(50)));
        assertEquals(2 * ZipConstants.DWORD, fieldWithSizes.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getCentralDirectoryLength().getValue());

        Zip64ExtendedInformationExtraField fieldWithSizes = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(new ZipLong(100)),
                new ZipEightByteInteger(new ZipLong(50)),
                null,
                null);
        assertEquals(2 * ZipConstants.DWORD + ZipConstants.DWORD, fieldWithSizes.getCentralDirectoryLength().getValue());

        Zip64ExtendedInformationExtraField fieldWithAllFields = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(new ZipLong(100)),
                new ZipEightByteInteger(new ZipLong(50)),
                new ZipEightByteInteger(new ZipLong(10)),
                new ZipLong(15));
        assertEquals(2 * ZipConstants.DWORD + ZipConstants.DWORD + ZipConstants.WORD, fieldWithAllFields.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] localFileData = new byte[2 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(new ZipLong(100));
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(new ZipLong(50));

        System.arraycopy(size.getBytes(), 0, localFileData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, ZipConstants.DWORD, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());

        byte[] localFileDataWithAllFields = new byte[2 * ZipConstants.DWORD + ZipConstants.DWORD];
        System.arraycopy(size.getBytes(), 0, localFileDataWithAllFields, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileDataWithAllFields, ZipConstants.DWORD, ZipConstants.DWORD);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(new ZipLong(10));
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, localFileDataWithAllFields, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        ZipLong diskStart = new ZipLong(15);
        System.arraycopy(diskStart.getBytes(), 0, localFileDataWithAllFields, 2 * ZipConstants.DWORD + ZipConstants.DWORD, ZipConstants.WORD);

        field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(localFileDataWithAllFields, 0, localFileDataWithAllFields.length);
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD];
        ZipEightByteInteger size = new ZipEightByteInteger(new ZipLong(100));
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(new ZipLong(50));

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStart());

        byte[] centralDirectoryDataWithAllFields = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        System.arraycopy(size.getBytes(), 0, centralDirectoryDataWithAllFields, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryDataWithAllFields, ZipConstants.DWORD, ZipConstants.DWORD);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(new ZipLong(10));
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryDataWithAllFields, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        ZipLong diskStart = new ZipLong(15);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryDataWithAllFields, 2 * ZipConstants.DWORD + ZipConstants.DWORD, ZipConstants.WORD);

        field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryDataWithAllFields, 0, centralDirectoryDataWithAllFields.length);
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());

        byte[] centralDirectoryDataWithOnlyDiskStart = new byte[3 * ZipConstants.DWORD];
        System.arraycopy(centralDirectoryDataWithAllFields, 0, centralDirectoryDataWithOnlyDiskStart, 0, 3 * ZipConstants.DWORD - ZipConstants.WORD);
        ZipLong diskStartOnly = new ZipLong(20);
        System.arraycopy(diskStartOnly.getBytes(), 0, centralDirectoryDataWithOnlyDiskStart, 3 * ZipConstants.DWORD - ZipConstants.WORD, ZipConstants.WORD);

        field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryDataWithOnlyDiskStart, 0, centralDirectoryDataWithOnlyDiskStart.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(20), field.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidDataLength() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD - 1];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        ZipEightByteInteger size = new ZipEightByteInteger(new ZipLong(100));
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(new ZipLong(50));
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(new ZipLong(10));
        ZipLong diskStart = new ZipLong(15);

        System.arraycopy(size.getBytes(), 0, centralDirectoryData, 0, ZipConstants.DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * ZipConstants.DWORD, ZipConstants.DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 2 * ZipConstants.DWORD + ZipConstants.DWORD, ZipConstants.WORD);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());

        field.reparseCentralDirectoryData(true, false, true, true);
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
        assertEquals(null, field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());

        field.reparseCentralDirectoryData(false, true, true, true);
        assertEquals(null, field.getSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());

        field.reparseCentralDirectoryData(false, false, true, true);
        assertEquals(null, field.getSize());
        assertEquals(null, field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());

        field.reparseCentralDirectoryData(false, false, false, true);
        assertEquals(null, field.getSize());
        assertEquals(null, field.getCompressedSize());
        assertEquals(null, field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(15), field.getDiskStartNumber());

        field.reparseCentralDirectoryData(false, false, false, false);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());

        field = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(new ZipLong(100)));
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());

        field.setSize(new ZipEightByteInteger(new ZipLong(100)));
        assertEquals(new ZipEightByteInteger(new ZipLong(100)), field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getCompressedSize());

        field = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(new ZipLong(100)), new ZipEightByteInteger(new ZipLong(50)));
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getCompressedSize());

        field.setCompressedSize(new ZipEightByteInteger(new ZipLong(50)));
        assertEquals(new ZipEightByteInteger(new ZipLong(50)), field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getRelativeHeaderOffset());

        field = new Zip64ExtendedInformationExtraField(null, null, new ZipEightByteInteger(new ZipLong(10)));
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getRelativeHeaderOffset());

        field.setRelativeHeaderOffset(new ZipEightByteInteger(new ZipLong(10)));
        assertEquals(new ZipEightByteInteger(new ZipLong(10)), field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getDiskStartNumber());

        field = new Zip64ExtendedInformationExtraField(null, null, null, new ZipLong(15));
        assertEquals(new ZipLong(15), field.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getDiskStartNumber());

        field.setDiskStartNumber(new ZipLong(15));
        assertEquals(new ZipLong(15), field.getDiskStartNumber());
    }
}