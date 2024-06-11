package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(50);
        ZipLong diskStartNumber = new ZipLong(1);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null);
    }

    @Test
    public void testCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(4, field.getCentralDirectoryLength().getValue());
    }

    // Add more test cases for other methods as needed

}