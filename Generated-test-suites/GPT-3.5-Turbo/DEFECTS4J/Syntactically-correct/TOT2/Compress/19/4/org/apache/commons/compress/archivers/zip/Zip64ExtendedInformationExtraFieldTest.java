import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {
    
    @Test
    public void testConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
    
    @Test
    public void testSetSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.setSize(size);
        
        assertEquals(size, field.getSize());
    }
    
    @Test
    public void testSetCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.setCompressedSize(compressedSize);
        
        assertEquals(compressedSize, field.getCompressedSize());
    }
    
    @Test
    public void testSetRelativeHeaderOffset() {
        ZipEightByteInteger offset = new ZipEightByteInteger(500L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.setRelativeHeaderOffset(offset);
        
        assertEquals(offset, field.getRelativeHeaderOffset());
    }
    
    @Test
    public void testSetDiskStartNumber() {
        ZipLong diskStart = new ZipLong(3);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.setDiskStartNumber(diskStart);
        
        assertEquals(diskStart, field.getDiskStartNumber());
    }
    
    @Test
    public void testParseFromLocalFileData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);
        byte[] data = new byte[16];
        size.putLongValue(data, 0);
        compressedSize.putLongValue(data, 8);
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.parseFromLocalFileData(data, 0, 16);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
    
    @Test
    public void testParseFromCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);
        byte[] data = new byte[16];
        size.putLongValue(data, 0);
        compressedSize.putLongValue(data, 8);
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.parseFromCentralDirectoryData(data, 0, 16);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
    
    @Test
    public void testReparseCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);
        byte[] data = new byte[16];
        size.putLongValue(data, 0);
        compressedSize.putLongValue(data, 8);
        
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        
        field.parseFromCentralDirectoryData(data, 0, 16);
        field.reparseCentralDirectoryData(true, true, false, false);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
}