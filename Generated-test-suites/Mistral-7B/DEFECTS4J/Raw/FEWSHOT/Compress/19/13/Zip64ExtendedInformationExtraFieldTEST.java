package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmpty() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = extraField.getLocalFileDataData();
        assertArrayEquals(new byte[0], data);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewWithBothSizes() {
        long size = 123456789;
        long compressedSize = 567890123;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(size), new ZipEightByteInteger(compressedSize));

        byte[] data = extraField.getLocalFileDataData();
        assertArrayEquals(new byte[]{
                (byte) (size >>> 24), (byte) (size >>> 16),
                (byte) (size >>> 8), (byte) size,
                (byte) (compressedSize >>> 24), (byte) (compressedSize >>> 16),
                (byte) (compressedSize >>> 8), (byte) compressedSize
        }, data);
        assertEquals(new ZipEightByteInteger(size), extraField.getSize());
        assertEquals(new ZipEightByteInteger(compressedSize), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testNewWithAllFields() {
        long size = 123456789;
        long compressedSize = 567890123;
        long relativeHeaderOffset = 12345;
        long diskStart = 67890;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(size), new ZipEightByteInteger(compressedSize),
                new ZipEightByteInteger(relativeHeaderOffset), new ZipLong(diskStart));

        byte[] data = extraField.getLocalFileDataData();
        assertArrayEquals(new byte[]{
                (byte) (size >>> 24), (byte) (size >>> 16),
                (byte) (size >>> 8), (byte) size,
                (byte) (compressedSize >>> 24), (byte) (compressedSize >>> 16),
                (byte) (compressedSize >>> 8), (byte) compressedSize,
                (byte) (relativeHeaderOffset >>> 24), (byte) (relativeHeaderOffset >>> 16),
                (byte) (relativeHeaderOffset >>> 8), (byte) relativeHeaderOffset.intValue(),
                (byte) (diskStart.getValue() >>> 24), (byte) (diskStart.getValue() >>> 16)
        }, data);
        assertEquals(new ZipEightByteInteger(size), extraField.getSize());
        assertEquals(new ZipEightByteInteger(compressedSize), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(relativeHeaderOffset), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(diskStart), extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        long size = 123456789;
        long compressedSize = 567890123;
        byte[] data = new byte[]{
                (byte) (size >>> 24), (byte) (size >>> 16),
                (byte) (size >>> 8), (byte) size,
                (byte) (compressedSize >>> 24), (byte) (compressedSize >>> 16),
                (byte) (compressedSize >>> 8), (byte) compressedSize
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(new ZipEightByteInteger(size), extraField.getSize());
        assertEquals(new ZipEightByteInteger(compressedSize), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        long size = 123456789;
        long compressedSize = 567890123;
        long relativeHeaderOffset = 12345;
        long diskStart = 67890;
        byte[] centralDirectoryData = new byte[]{
                0, 0, 0, 0, // Tag
                2, (byte) (size >>> 24), (byte) (size >>> 16),
                (byte) (size >>> 8), (byte) size.byteValue(), // Original Size
                2, (byte) (compressedSize >>> 24), (byte) (compressedSize >>> 16),
                (byte) (compressedSize >>> 8), (byte) compressedSize.byteValue(), // Compressed Size
                2, (byte) (relativeHeaderOffset >>> 24), (byte) (relativeHeaderOffset >>> 16),
                (byte) (relativeHeaderOffset >>> 8), (byte) relativeHeaderOffset.intValue(), // Relative Header Offset
                2, (byte) (diskStart.getValue() >>> 24), (byte) (diskStart.getValue() >>> 16)
        };
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(new ZipEightByteInteger(size), extraField.getSize());
        assertEquals(new ZipEightByteInteger(compressedSize), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(relativeHeaderOffset), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(diskStart), extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithMissingFields() throws ZipException {
        byte[] centralDirectoryData = new byte[]{2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0};

        assertThrows(ZipException.class, () -> {
            Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
            extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        });
    }

    @Test
    public void testParseFromCentralDirectoryDataWithPartialData() throws ZipException {
        byte[] centralDirectoryData = new byte[]{2, 0, 0, 0, 1, 0, 0, 0, 1, 3, 0, 0, 0};

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertTrue(extraField.getDiskStart() == null);
    }

    @Test
    public void testGetCentralDirectoryData() {
        long size = 123456789;
        long compressedSize = 567890123;
        long relativeHeaderOffset = 12345;
        long diskStart = 67890;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(size), new ZipEightByteInteger(compressedSize),
                new ZipEightByteInteger(relativeHeaderOffset), new ZipLong(diskStart));

        byte[] data = extraField.getCentralDirectoryData();
        assertEquals(24, data.length);
    }

    @Test
    public void testGetCentralDirectoryDataWithMissingFields() {
        long size = 123456789;
        long compressedSize = 567890123;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(size), new ZipEightByteInteger(compressedSize));

        byte[] data = extraField.getCentralDirectoryData();
        assertEquals(16, data.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        long size = 123456789;
        long compressedSize = 567890123;
        long relativeHeaderOffset = 12345;
        long diskStart = 67890;
        byte[] centralDirectoryData = new byte[]{
                0, 0, 0, 0, // Tag
                2, (byte) (size >>> 24), (byte) (size >>> 16),
                (byte) (size >>> 8), (byte) size.byteValue(), // Original Size
                2, (byte) (compressedSize >>> 24), (byte) (compressedSize >>> 16),
                (byte) (compressedSize >>> 8), (byte) compressedSize.byteValue(), // Compressed Size
                2, (byte) (relativeHeaderOffset >>> 24), (byte) (relativeHeaderOffset >>> 16),
                (byte) (relativeHeaderOffset >>> 8), (byte) relativeHeaderOffset.intValue(), // Relative Header Offset
                2, (byte) (diskStart.getValue() >>> 24), (byte) (diskStart.getValue() >>> 16)
        };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(new ZipEightByteInteger(size), extraField.getSize());
        assertEquals(new ZipEightByteInteger(compressedSize), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(relativeHeaderOffset), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(diskStart), extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithMissingFields() throws ZipException {
        long size = 123456789;
        long compressedSize = 567890123;
        byte[] centralDirectoryData = new byte[]{
                0, 0, 0, 0, // Tag
                2, (byte) (size >>> 24), (byte) (size >>> 16),
                (byte) (size >>> 8), (byte) size.byteValue(), // Original Size
                2, (byte) (compressedSize >>> 24), (byte) (compressedSize >>> 16)
        };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);

        assertEquals(new ZipEightByteInteger(size), extraField.getSize());
        assertEquals(new ZipEightByteInteger(compressedSize), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }
}