package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

import org.junit.Test;

/**
 * Test for {@link Zip64ExtendedInformationExtraField}
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test for constructor Zip64ExtendedInformationExtraField()
     */
    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, extraField.getHeaderId());
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for constructor Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger)
     */
    @Test
    public void testBasicConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(ZipShort.ZERO, extraField.getHeaderId());
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for constructor Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger, ZipEightByteInteger, ZipLong)
     */
    @Test
    public void testFullConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(ZipShort.ZERO, extraField.getHeaderId());
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test for getters and setters
     */
    @Test
    public void testGettersAndSetters() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.setSize(size);
        extraField.setCompressedSize(compressedSize);
        extraField.setRelativeHeaderOffset(relativeHeaderOffset);
        extraField.setDiskStartNumber(diskStart);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test for getLocalFileDataLength()
     */
    @Test
    public void testGetLocalFileDataLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size);
        assertEquals(new ZipShort(2 * DWORD), extraField.getLocalFileDataLength());
    }

    /**
     * Test for getCentralDirectoryLength()
     */
    @Test
    public void testGetCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(new ZipShort((size != null ? DWORD : 0)
                                   + (compressedSize != null ? DWORD : 0)
                                   + (relativeHeaderOffset != null ? DWORD : 0)
                                   + (diskStart != null ? WORD : 0)), extraField.getCentralDirectoryLength());
    }

    /**
     * Test for getLocalFileDataData()
     */
    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = extraField.getLocalFileDataData();
        assertEquals(2 * DWORD, data.length);
        assertFalse(extraField.hasDiskStart());

        byte[] expectedData = new byte[2 * DWORD];
        addSizes(expectedData);
        assertTrue(Arrays.equals(data, expectedData));
    }

    /**
     * Test for getCentralDirectoryData()
     */
    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        byte[] data = extraField.getCentralDirectoryData();
        assertEquals(extraField.getCentralDirectoryLength().getValue(), data.length);

        int off = 0;
        if (size != null) {
            assertTrue(Arrays.equals(size.getBytes(), Arrays.copyOfRange(data, off, off + DWORD)));
            off += DWORD;
        }
        if (compressedSize != null) {
            assertTrue(Arrays.equals(compressedSize.getBytes(), Arrays.copyOfRange(data, off, off + DWORD)));
            off += DWORD;
        }
        if (relativeHeaderOffset != null) {
            assertTrue(Arrays.equals(relativeHeaderOffset.getBytes(), Arrays.copyOfRange(data, off, off + DWORD)));
            off += DWORD;
        }
        if (diskStart != null) {
            assertTrue(Arrays.equals(diskStart.getBytes(), Arrays.copyOfRange(data, off, off + WORD)));
        }
    }

    /**
     * Test for parseFromLocalFileData()
     */
    @Test
    public void testParseFromLocalFileData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        byte[] localFileData = new byte[2 * DWORD];
        addSizes(localFileData);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for parseFromCentralDirectoryData()
     */
    @Test
    public void testParseFromCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, centralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, off, WORD);
        }

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test for reparseCentralDirectoryData()
     */
    @Test
    public void testReparseCentralDirectoryData()
        throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, centralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (relativeHeaderOffset != null) {
            System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, off, DWORD);
            off += DWORD;
        }
        if (diskStart != null) {
            System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, off, WORD);
        }

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());

        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test for reparseCentralDirectoryData() with missing size fields
     */
    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingSizes()
        throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096L);
        ZipLong diskStart = new ZipLong(0x123456789ABCDEF0L);
        byte[] centralDirectoryData = new byte[2 * DWORD]; // Only has 2 DWORD

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
    }

    /**
     * Test for parseFromCentralDirectoryData() with data not matching central directory data length
     */
    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength()
        throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        byte[] centralDirectoryData = new byte[3 * DWORD]; // Should be 3 * DWORD + WORD

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    /**
     * Helper method to add sizes to a byte array
     */
    private void addSizes(byte[] data) {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
        }
    }

}