package org.apache.commons.compress.archivers.zip;
import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(200L);
        ZipLong diskStart = new ZipLong(1L);
        
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        new Zip64ExtendedInformationExtraField(null, compressedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        new Zip64ExtendedInformationExtraField(size, null);
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), Zip64ExtendedInformationExtraField.HEADER_ID);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), extraField.getCentralDirectoryLength());
    }

    // Add more test cases for other methods here
    
}