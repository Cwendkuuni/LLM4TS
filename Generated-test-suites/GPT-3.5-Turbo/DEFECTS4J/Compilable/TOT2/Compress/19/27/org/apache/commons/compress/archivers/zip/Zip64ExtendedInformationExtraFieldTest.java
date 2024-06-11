package org.apache.commons.compress.archivers.zip;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField zipField;

    @Before
    public void setUp() {
        zipField = new Zip64ExtendedInformationExtraField();
    }

    @Test
    public void testConstructor_withSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testConstructor_withAllFields() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10);
        ZipLong diskStart = new ZipLong(1);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), zipField.getHeaderId());
    }

    // Add more test cases for other public methods to cover edge cases and exceptions

}