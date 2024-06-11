package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Test class for Zip64ExtendedInformationExtraField.java
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test for create Zip64ExtendedInformationExtraField by constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        long size = 12345678L;
        long compressedSize = 654321L;
        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField(
                        new ZipEightByteInteger((int)size),
                        new ZipEightByteInteger((int)compressedSize));
        assertEquals(size, extraField.getSize().getValue());
        assertEquals(compressedSize, extraField.getCompressedSize().getValue());
    }

    /**
     * Test for getLocalFileDataData method.
     */
    @org.junit.Test
    public void testGetLocalFileDataData() {
        long size = 12345678L;
        long compressedSize = 654321L;

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField(
                        new ZipEightByteInteger((int)size),
                        new ZipEightByteInteger((int)compressedSize));

        byte[] expectedData = new byte[2 * DWORD];
        extraField.addSizes(expectedData);

        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test for parseFromLocalFileData method.
     */
    @org.junit.Test
    public void testParseFromLocalFileData() throws ZipException {
        long size = 12345678L;
        long compressedSize = 654321L;

        byte[] data = new byte[2 * DWORD];
        System.arraycopy(new ZipEightByteInteger((int)size).getBytes(), 0, data, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger((int)compressedSize).getBytes(), 0, data, DWORD, DWORD);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ZipEntry entry = new ZipEntry("testEntry");
        ZipFile zipFile = new ZipFile(new ByteArrayOutputStream(1024));

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(size, extraField.getSize().getValue());
        assertEquals(compressedSize, extraField.getCompressedSize().getValue());

        zipFile.close();
    }

    /**
     * Test for getCentralDirectoryData method.
     */
    @org.junit.Test
    public void testGetCentralDirectoryData() {
        long size = 12345678L;
        long compressedSize = 654321L;

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField(
                        new ZipEightByteInteger((int)size),
                        new ZipEightByteInteger((int)compressedSize));

        byte[] expectedData = new byte[2 * DWORD + DWORD];
        int offset = extraField.addSizes(expectedData);
        if (extraField.getRelativeHeaderOffset() != null) {
            System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0,
                    expectedData, offset, DWORD);
            offset += DWORD;
        }
        if (extraField.getDiskStartNumber() != null) {
            System.arraycopy(extraField.getDiskStartNumber().getBytes(), 0,
                    expectedData, offset, WORD);
        }

        byte[] actualData = extraField.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test for parseFromCentralDirectoryData method.
     */
    @org.junit.Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        long size = 12345678L;
        long compressedSize = 654321L;
        long relativeHeaderOffset = 123456L;
        long diskStartNumber = 12345L;

        byte[] data = new byte[4 * DWORD + WORD];

        ZipShort headerId = new ZipShort(0x0001);

        if (size > 0) {
            System.arraycopy(new ZipEightByteInteger((int)size).getBytes(), 0, data, 0, DWORD);
            data[DWORD] = (byte) (headerId.getValue() & 0xFF);
            data[DWORD + 1] = (byte) ((headerId.getValue() >> 8) & 0xFF);
        }
        if (compressedSize > 0) {
            System.arraycopy(new ZipEightByteInteger((int)compressedSize).getBytes(), 0, data, 2 * DWORD, DWORD);
            data[2 * DWORD] = (byte) (headerId.getValue() & 0xFF);
            data[2 * DWORD + 1] = (byte) ((headerId.getValue() >> 8) & 0xFF);
        }
        if (relativeHeaderOffset > 0) {
            System.arraycopy(new ZipEightByteInteger((int)relativeHeaderOffset).getBytes(), 0, data, 3 * DWORD, DWORD);
            data[3 * DWORD] = (byte) (headerId.getValue() & 0xFF);
            data[3 * DWORD + 1] = (byte) ((headerId.getValue() >> 8) & 0xFF);
            data[3 * DWORD + 2] = (byte) ((headerId.getValue() >> 16) & 0xFF);
            data[3 * DWORD + 3] = (byte) ((headerId.getValue() >> 24) & 0xFF);
        }
        if (diskStartNumber > 0) {
            System.arraycopy(new ZipLong(diskStartNumber).getBytes(), 0, data, 4 * DWORD, WORD);
            data[4 * DWORD] = (byte) (headerId.getValue() & 0xFF);
            data[4 * DWORD + 1] = (byte) ((headerId.getValue() >> 8) & 0xFF);
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ZipEntry entry = new ZipEntry("testEntry");
        ZipFile zipFile = new ZipFile(new ByteArrayOutputStream(1024));

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(inputStream, 0, data.length);

        assertEquals(size, extraField.getSize().getValue());
        assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber().getValue());

        zipFile.close();
    }

    /**
     * Test for reparseCentralDirectoryData method.
     */
    @org.junit.Test
    public void testReparseCentralDirectoryData() throws ZipException {
        long size = 12345678L;
        long compressedSize = 654321L;
        long relativeHeaderOffset = 123456L;
        long diskStartNumber = 12345L;

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(new byte[0], 0, 0);

        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(size, extraField.getSize().getValue());
        assertEquals(compressedSize, extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber().getValue());

        extraField.reparseCentralDirectoryData(false, false, false, false);
        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    /**
     * Test for LFH_MUST_HAVE_BOTH_SIZES_MSG.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testLFH_MUST_HAVE_BOTH_SIZES_MSG() {
        long size = 12345678L;
        long compressedSize = 654321L;

        byte[] data = new byte[2 * DWORD];
        System.arraycopy(new ZipEightByteInteger((int)size).getBytes(), 0, data, 0, DWORD);
        System.arraycopy(new ZipEightByteInteger((int)size).getBytes(), 0, data, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField =
                new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        throw new ZipException(Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }
}