package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {
    
    @Test
    public void testConstructorAndGetters() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(100),
                new ZipEightByteInteger(200),
                new ZipEightByteInteger(300),
                new ZipLong(400));
        
        assertNotNull(field.getHeaderId());
        assertEquals(2 * Util.DWORD + Util.WORD, field.getLocalFileDataLength().getValue());
        assertEquals(3 * Util.DWORD + Util.WORD, field.getCentralDirectoryLength().getValue());
        assertEquals(8, field.getLocalFileDataData().length);
        assertEquals(16, field.getCentralDirectoryData().length);
        assertEquals(new ZipEightByteInteger(100), field.getSize());
        assertEquals(new ZipEightByteInteger(200), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(300), field.getRelativeHeaderOffset());
        assertEquals(new ZipLong(400), field.getDiskStartNumber());
    }
    
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = new byte[16];
        buffer[2] = 100;
        buffer[3] = 0;
        buffer[9] = 200;
        buffer[10] = 0;
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        
        assertEquals(new ZipEightByteInteger(100), field.getSize());
        assertEquals(new ZipEightByteInteger(200), field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }
    
    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[12];
        buffer[0] = 100;
        buffer[1] = 0;
        buffer[4] = 200;
        buffer[5] = 0;
        buffer[8] = 1;
        buffer[9] = 0;
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        
        assertEquals(new ZipEightByteInteger(100), field.getSize());
        assertEquals(new ZipEightByteInteger(200), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(1), field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }
    
    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[12];
        buffer[0] = 100;
        buffer[1] = 0;
        buffer[4] = 200;
        buffer[5] = 0;
        buffer[8] = 1;
        buffer[9] = 0;
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        
        assertEquals(new ZipEightByteInteger(100), field.getSize());
        assertEquals(new ZipEightByteInteger(200), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(1), field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }
}