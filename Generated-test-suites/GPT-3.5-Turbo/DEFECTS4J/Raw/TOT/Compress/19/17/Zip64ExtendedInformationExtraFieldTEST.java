import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(100);
        ZipLong diskStartNumber = new ZipLong(1);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);
        
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        ZipShort headerId = new ZipShort(0x0001);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        
        assertEquals(headerId, extraField.getHeaderId());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        ZipShort length = new ZipShort(16);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(500));
        
        assertEquals(length, extraField.getLocalFileDataLength());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort length = new ZipShort(16);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(500));
        
        assertEquals(length, extraField.getCentralDirectoryLength());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = {0,0,0,0,0,0,3,-24,0,0,1,-120};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(500));
        
        assertArrayEquals(expected, extraField.getLocalFileDataData());
    }
    
    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = {0,0,0,0,0,0,3,-24,0,0,1,-120,0,0,0,0};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1000), new ZipEightByteInteger(500));
        
        assertArrayEquals(expected, extraField.getCentralDirectoryData());
    }
    
    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = {0,0,0,0,0,0,3,-24,0,0,1,-120};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
        
        assertEquals(new ZipEightByteInteger(1000), extraField.getSize());
        assertEquals(new ZipEightByteInteger(500), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }
    
    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = {0,0,0,0,0,0,3,-24,0,0,1,-120,0,0,0,0};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        
        assertEquals(new ZipEightByteInteger(1000), extraField.getSize());
        assertEquals(new ZipEightByteInteger(500), extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }
    
    @Test
    public void testReparseCentralDirectoryData() {
        byte[] buffer = {0,0,0,0,0,0,0,0,0,0,3,-24,0,0,1,-120,0,0,0,0};
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        
        extraField.reparseCentralDirectoryData(true, true, true, true);
        
        assertEquals(new ZipEightByteInteger(1000), extraField.getSize());
        assertEquals(new ZipEightByteInteger(500), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(100), extraField.getRelativeHeaderOffset());
        assertEquals(new ZipLong(1), extraField.getDiskStartNumber());
    }

}