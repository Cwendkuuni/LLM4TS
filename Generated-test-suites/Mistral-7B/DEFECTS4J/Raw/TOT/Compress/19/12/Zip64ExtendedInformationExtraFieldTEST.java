package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Tests for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testEmptyFields() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        assertNull(e.getSize());
        assertNull(e.getCompressedSize());
        assertNull(e.getRelativeHeaderOffset());
        assertNull(e.getDiskStartNumber());
    }

    @Test
    public void testAllFields() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1L),
            new ZipEightByteInteger(2L),
            new ZipEightByteInteger(3L),
            new ZipLong(4L));

        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertEquals(new ZipEightByteInteger(2L), e.getCompressedSize());
        assertEquals(new ZipEightByteInteger(3L), e.getRelativeHeaderOffset());
        assertEquals(new ZipLong(4L), e.getDiskStartNumber());

        assertEquals(1, e.getLocalFileDataLength().getValue());
        assertEquals(20, e.getCentralDirectoryLength().getValue());
        assertArrayEquals(new byte[] { 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00,
                                        0x00, 0x00, 0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03,
                                        0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04,
                                        0x00, 0x00, 0x04, 0x00 }, e.getLocalFileDataData());
        assertArrayEquals(new byte[] { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 2, 0, 0, 2, 0,
                                        0, 3, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0,
                                        0, 4, 0, 0 }, e.getCentralDirectoryData());
    }

    @Test
    public void testMissingSizeValues() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1L), new ZipEightByteInteger(2L));
        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertFalse(e.hasCompressedSize());
        assertNull(e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
        assertNull(e.getRelativeHeaderOffset());
        assertNull(e.getDiskStartNumber());
    }

    @Test
    public void testMissingCompressedSizeValues() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1L), null, new ZipEightByteInteger(3L),
            new ZipLong(4L));
        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertTrue(e.hasCompressedSize());
        assertNull(e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
        assertNull(e.getRelativeHeaderOffset());
        assertEquals(new ZipLong(4L), e.getDiskStartNumber());
    }

    @Test
    public void testMissingRelativeHeaderOffsetValues() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1L), new ZipEightByteInteger(2L), null,
            new ZipLong(4L));
        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertTrue(e.hasCompressedSize());
        assertEquals(new ZipEightByteInteger(2L), e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
        assertNull(e.getRelativeHeaderOffset());
        assertEquals(new ZipLong(4L), e.getDiskStartNumber());
    }

    @Test
    public void testMissingDiskStartNumberValues() {
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(1L), new ZipEightByteInteger(2L),
            new ZipEightByteInteger(3L), null);
        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertTrue(e.hasCompressedSize());
        assertEquals(new ZipEightByteInteger(2L), e.getCompressedSize());
        assertTrue(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
        assertEquals(new ZipEightByteInteger(3L), e.getRelativeHeaderOffset());
        assertNull(e.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[] { 1, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 2 };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromLocalFileData(data, 0, data.length);

        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertTrue(e.hasCompressedSize());
        assertEquals(new ZipEightByteInteger(2L), e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileDataMissingSizeValues() {
        byte[] data = new byte[] { 0, 0, 0, 0, 2, 0, 0, 2 };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        assertThrows(ZipException.class, () -> e.parseFromLocalFileData(data, 0, data.length));
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] data = new byte[] { 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00,
                                    0x00, 0x00, 0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03,
                                    0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04,
                                    0x00, 0x00, 0x04, 0x00 };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromCentralDirectoryData(data, 0, data.length);

        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertTrue(e.hasCompressedSize());
        assertEquals(new ZipEightByteInteger(2L), e.getCompressedSize());
        assertTrue(e.hasRelativeHeaderOffset());
        assertEquals(new ZipLong(3L), e.getRelativeHeaderOffset());
        assertTrue(e.hasDiskStartNumber());
        assertEquals(new ZipLong(4L), e.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataMissingSizeValues() {
        byte[] data = new byte[] { 0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00,
                                    0x00, 0x00, 0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03 };
        Zip64ExtendedInformationExtraField e = new Zip64ExtendedInformationExtraField();
        e.parseFromCentralDirectoryData(data, 0, data.length);

        assertEquals(new ZipEightByteInteger(1L), e.getSize());
        assertTrue(e.hasCompressedSize());
        assertNull(e.getCompressedSize());
        assertFalse(e.hasRelativeHeaderOffset());
        assertFalse(e.hasDiskStartNumber());
    }
}