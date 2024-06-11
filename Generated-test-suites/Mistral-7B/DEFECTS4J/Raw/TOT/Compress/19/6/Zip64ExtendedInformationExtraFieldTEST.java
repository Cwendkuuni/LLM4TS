package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Test class for {@link Zip64ExtendedInformationExtraField}.
 */
public class Zip64ExtendedInformationExtraFieldTest {

    /**
     * Test for the constructor {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger)}.
     */
    @Test
    public void test_constructor_ZipEightByteInteger_ZipEightByteInteger() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for the constructor {@link Zip64ExtendedInformationExtraField#Zip64ExtendedInformationExtraField(ZipEightByteInteger, ZipEightByteInteger, ZipEightByteInteger, ZipLong)}.
     */
    @Test
    public void test_constructor_ZipEightByteInteger_ZipEightByteInteger_ZipEightByteInteger_ZipLong() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStart = new ZipLong(0x12345678);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#getLocalFileDataLength()}.
     */
    @Test
    public void test_getLocalFileDataLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, null, null, null);

        assertEquals(2 * ZipConstants.DWORD, extraField.getLocalFileDataLength().getValue());
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#getCentralDirectoryLength()}.
     */
    @Test
    public void test_getCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStart = new ZipLong(0x12345678);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(size.getValue() + compressedSize.getValue() + relativeHeaderOffset.getValue() + diskStart.getValue() / ZipConstants.WORD,
                     extraField.getCentralDirectoryLength().getValue());
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#getLocalFileDataData()}.
     */
    @Test
    public void test_getLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        byte[] localFileData = extraField.getLocalFileDataData();

        assertEquals(2 * ZipConstants.DWORD, localFileData.length);
        assertEquals(size.getBytes(), new byte[] { 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xED,
                                                (byte) 0xDC, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xCE });
        assertEquals(compressedSize.getBytes(), new byte[] { 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xEE,
                                                          (byte) 0xFA, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xDB });
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#getCentralDirectoryData()}.
     */
    @Test
    public void test_getCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5678);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStart = new ZipLong(0x12345678);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        assertEquals(size.getValue() + compressedSize.getValue() + relativeHeaderOffset.getValue() + diskStart.getValue() / ZipConstants.WORD,
                     centralDirectoryData.length);
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#parseFromLocalFileData(byte[], int, int)}.
     */
    @Test
    public void test_parseFromLocalFileData() throws ZipException, IOException, DataFormatException {
        byte[] localFileData = new byte[] { 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xED,
                                           (byte) 0xDC, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xCE,
                                           0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xEE,
                                           (byte) 0xFA, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xDB };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(localFileData);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(new ZipEightByteInteger(1234), extraField.getSize());
        assertEquals(new ZipEightByteInteger(5678), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#parseFromCentralDirectoryData(byte[], int, int)}.
     */
    @Test
    public void test_parseFromCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[] { 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xED,
                                                    (byte) 0xDC, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xCE,
                                                    0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xEE,
                                                    (byte) 0xFA, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xDB,
                                                    0, 0, 0, 0, 0, 0, 0, 0, 0 };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#reparseCentralDirectoryData(boolean, boolean, boolean, boolean)}.
     */
    @Test
    public void test_reparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[] { 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xED,
                                                    (byte) 0xDC, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xCE,
                                                    0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xEE,
                                                    (byte) 0xFA, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xDB,
                                                    0, 0, 0, 0, 0, 0, 0, 0, 0 };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(new ZipEightByteInteger(1234), extraField.getSize());
        assertEquals(new ZipEightByteInteger(5678), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0x1234), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0x12345678), extraField.getDiskStartNumber());
    }

    /**
     * Test for the method {@link Zip64ExtendedInformationExtraField#reparseCentralDirectoryData(boolean, boolean, boolean, boolean)} with false parameters.
     */
    @Test
    public void test_reparseCentralDirectoryData_false() throws ZipException {
        byte[] centralDirectoryData = new byte[] { 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xED,
                                                    (byte) 0xDC, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xCE,
                                                    0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xEE,
                                                    (byte) 0xFA, 0, 0, 0, (byte) 0xFB, 0, 0, 0, (byte) 0xDB,
                                                    0, 0, 0, 0, 0, 0, 0, 0, 0 };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, false, false, false);

        assertNull(extraField.getSize());
        assertNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStart());
    }

    /**
     * Test for the constructor {@link Zip64ExtendedInformationExtraField()} and the method {@link Zip64ExtendedInformationExtraField#getLocalFileDataLength()}.
     */
    @Test(expected = IllegalStateException.class)
    public void test_constructor_Zip64ExtendedInformationExtraField_IllegalStateException() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getLocalFileDataLength().getValue());
    }

    /**
     * Test for the constructor {@link Zip64ExtendedInformationExtraField()} and the method {@link Zip64ExtendedInformationExtraField#getCentralDirectoryLength()}.
     */
    @Test(expected = IllegalStateException.class)
    public void test_constructor_Zip64ExtendedInformationExtraField_IllegalStateException_2() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0, extraField.getCentralDirectoryLength().getValue());
    }
}