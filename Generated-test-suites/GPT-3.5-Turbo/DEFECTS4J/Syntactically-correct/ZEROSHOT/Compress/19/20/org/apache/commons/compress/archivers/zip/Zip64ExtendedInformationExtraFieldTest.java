package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {
    
    @Test
    public void testConstructor1() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNotNull(field);
    }
    
    @Test
    public void testConstructor2() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertNotNull(field);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
    
    @Test
    public void testConstructor3() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(200);
        ZipLong diskStart = new ZipLong(1);
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertNotNull(field);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }
    
    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getLocalFileDataLength());
        
        field.setSize(new ZipEightByteInteger(1000));
        field.setCompressedSize(new ZipEightByteInteger(500));
        assertEquals(new ZipShort(16), field.getLocalFileDataLength());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getCentralDirectoryLength());
        
        field.setSize(new ZipEightByteInteger(1000));
        field.setCompressedSize(new ZipEightByteInteger(500));
        assertEquals(new ZipShort(24), field.getCentralDirectoryLength());
        
        field.setRelativeHeaderOffset(new ZipEightByteInteger(200));
        field.setDiskStartNumber(new ZipLong(1));
        assertEquals(new ZipShort(34), field.getCentralDirectoryLength());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getLocalFileDataData());
        
        field.setSize(new ZipEightByteInteger(1000));
        field.setCompressedSize(new ZipEightByteInteger(500));
        byte[] expectedData = new byte[] { -24, 3, 0, 0, -12, 1, 0, 0 };
        assertArrayEquals(expectedData, field.getLocalFileDataData());
    }
    
    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getCentralDirectoryData());
        
        field.setSize(new ZipEightByteInteger(1000));
        field.setCompressedSize(new ZipEightByteInteger(500));
        byte[] expectedData = new byte[] { -24, 3, 0, 0, -12, 1, 0, 0 };
        assertArrayEquals(expectedData, field.getCentralDirectoryData());
        
        field.setRelativeHeaderOffset(new ZipEightByteInteger(200));
        field.setDiskStartNumber(new ZipLong(1));
        expectedData = new byte[] { -24, 3, 0, 0, -12, 1, 0, 0, -56, -9, -31, 6, 0, 0, 0, 0, 1, 0, 0, 0 };
        assertArrayEquals(expectedData, field.getCentralDirectoryData());
    }
}