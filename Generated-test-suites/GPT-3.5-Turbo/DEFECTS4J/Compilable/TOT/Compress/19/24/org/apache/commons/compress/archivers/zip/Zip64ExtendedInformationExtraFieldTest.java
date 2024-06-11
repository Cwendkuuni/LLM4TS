package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructor_default() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testConstructor_withSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234567890);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(9876543210L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testConstructor_withAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234567890);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(9876543210L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1357924680);
        ZipLong diskStartNumber = new ZipLong(246813579);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(9876543210L);
        new Zip64ExtendedInformationExtraField(null, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1234567890);
        new Zip64ExtendedInformationExtraField(size, null);
    }

    // Add more test cases to cover edge cases and error scenarios

}