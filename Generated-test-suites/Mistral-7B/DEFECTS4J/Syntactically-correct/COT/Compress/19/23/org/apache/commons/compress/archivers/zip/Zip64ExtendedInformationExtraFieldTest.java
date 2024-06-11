package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Test class for Zip64ExtendedInformationExtraField
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getHeaderId()}.
     */
    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.valueOf(0x0001), field.getHeaderId());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getLocalFileDataLength()}.
     */
    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(2 * ZipConstants.DWORD), field.getLocalFileDataLength());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getCentralDirectoryLength()}.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort((2 * ZipConstants.DWORD)
                + (ZipConstants.DWORD)
                + (ZipConstants.DWORD)
                + (ZipConstants.WORD)), field.getCentralDirectoryLength());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getLocalFileDataData()}.
     */
    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(ZipConstants.DWORD, 1),
                new ZipEightByteInteger(ZipConstants.DWORD, 2));
        byte[] data = field.getLocalFileDataData();
        assertNotNull(data);
        assertEquals(2 * ZipConstants.DWORD, data.length);
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#getCentralDirectoryData()}.
     */
    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(ZipConstants.DWORD, 1),
                new ZipEightByteInteger(ZipConstants.DWORD, 2),
                new ZipEightByteInteger(ZipConstants.DWORD, 3),
                new ZipLong(ZipConstants.WORD, 4));
        byte[] data = field.getCentralDirectoryData();
        assertNotNull(data);
        assertEquals((2 * ZipConstants.DWORD)
                + (ZipConstants.DWORD)
                + (ZipConstants.DWORD)
                + (ZipConstants.WORD), data.length);
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromLocalFileData(byte[], int, int)}.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{
                0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, data.length);
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 1), field.getSize());
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 2), field.getCompressedSize());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}.
     */
    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] data = new byte[]{
                0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05};
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 1), field.getSize());
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 2), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 3), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(ZipConstants.WORD, 4), field.getDiskStartNumber());
    }

    /**
     * Test method for {@link Zip64ExtendedInformationExtraField#reparseCentralDirectoryData(boolean, boolean, boolean, boolean)}.
     */
    @Test
    public void testReparseCentralDirectoryData() {
        byte[] data = new byte[]{
                0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, // size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08, // compressedSize
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0B, // relativeHeaderOffset
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0C}; // diskStartNumber

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 1), field.getSize());
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 2), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(ZipConstants.DWORD, 3), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(ZipConstants.WORD, 4), field.getDiskStartNumber());
    }

    /**
     * Test method for edge case with no data in local file header.
     */
    @Test
    public void testParseFromLocalFileDataEdgeCase() {
        byte[] data = new byte[0];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        try {
            field.parseFromLocalFileData(data, 0, data.length);
            assert false;
        } catch (ZipException e) {
            assertEquals("Zip64 extended information must contain both size values in the local file header.", e.getMessage());
        }
    }

    /**
     * Test method for edge case with invalid size in central directory data.
     */
    @Test
    public void testParseFromCentralDirectoryDataEdgeCase() {
        byte[] data = new byte[]{
                0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, // invalid size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x06}; // other data

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        try {
            field.parseFromCentralDirectoryData(data, 0, data.length);
            assert false;
        } catch (ZipException e) {
            assertEquals("Invalid size in zip64 extended information extra field in central directory.", e.getMessage());
        }
    }

    /**
     * Test method for edge case with null size in constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEdgeCaseSizeNull() {
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(ZipConstants.DWORD, 1));
    }

    /**
     * Test method for edge case with null compressed size in constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEdgeCaseCompressedSizeNull() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(ZipConstants.DWORD, 1), null);
    }

    /**
     * Test method for edge case with null relative header offset in constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEdgeCaseRelativeHeaderOffsetNull() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(ZipConstants.DWORD, 1),
                new ZipEightByteInteger(ZipConstants.DWORD, 2), null);
    }

    /**
     * Test method for edge case with null disk start number in constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEdgeCaseDiskStartNumberNull() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(ZipConstants.DWORD, 1),
                new ZipEightByteInteger(ZipConstants.DWORD, 2),
                new ZipEightByteInteger(ZipConstants.DWORD, 3), null);
    }
}