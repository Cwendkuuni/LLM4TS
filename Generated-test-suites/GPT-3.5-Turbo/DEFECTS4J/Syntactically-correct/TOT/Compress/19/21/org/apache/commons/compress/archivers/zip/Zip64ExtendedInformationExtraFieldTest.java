import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {
    
    private Zip64ExtendedInformationExtraField zip64;
    
    @Before
    public void setUp() {
        zip64 = new Zip64ExtendedInformationExtraField();
    }
    
    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), zip64.getHeaderId());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(new ZipShort(16), zip64.getLocalFileDataLength());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(32), zip64.getCentralDirectoryLength());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = new byte[16];
        expected[0] = 0x00;
        expected[1] = 0x01;
        assertArrayEquals(expected, zip64.getLocalFileDataData());
    }
    
    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[32];
        expected[0] = 0x00;
        expected[1] = 0x01;
        assertArrayEquals(expected, zip64.getCentralDirectoryData());
    }
    
    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[16];
        buffer[0] = 0x00;
        buffer[1] = 0x01;
        buffer[2] = 0x00;
        buffer[3] = 0x01;
        zip64.parseFromLocalFileData(buffer, 0, 16);
        assertEquals(new ZipEightByteInteger(new byte[] {0x00, 0x01}), zip64.getSize());
        assertEquals(new ZipEightByteInteger(new byte[] {0x00, 0x01}), zip64.getCompressedSize());
    }
    
    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[32];
        buffer[0] = 0x00;
        buffer[1] = 0x01;
        buffer[2] = 0x00;
        buffer[3] = 0x01;
        zip64.parseFromCentralDirectoryData(buffer, 0, 32);
        assertEquals(new ZipEightByteInteger(new byte[] {0x00, 0x01}), zip64.getSize());
        assertEquals(new ZipEightByteInteger(new byte[] {0x00, 0x01}), zip64.getCompressedSize());
    }
    
    @Test
    public void testReparseCentralDirectoryData() {
        byte[] buffer = new byte[32];
        buffer[0] = 0x00;
        buffer[1] = 0x01;
        buffer[2] = 0x00;
        buffer[3] = 0x01;
        zip64.parseFromCentralDirectoryData(buffer, 0, 32);
        zip64.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(new ZipEightByteInteger(new byte[] {0x00, 0x01}), zip64.getSize());
        assertEquals(new ZipEightByteInteger(new byte[] {0x00, 0x01}), zip64.getCompressedSize());
    }
    
    @Test
    public void testGetSize() {
        assertNull(zip64.getSize());
    }
    
    @Test
    public void testSetSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(new byte[] {0x00, 0x01});
        zip64.setSize(size);
        assertEquals(size, zip64.getSize());
    }
    
    @Test
    public void testGetCompressedSize() {
        assertNull(zip64.getCompressedSize());
    }
    
    @Test
    public void testSetCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(new byte[] {0x00, 0x01});
        zip64.setCompressedSize(compressedSize);
        assertEquals(compressedSize, zip64.getCompressedSize());
    }
    
    @Test
    public void testGetRelativeHeaderOffset() {
        assertNull(zip64.getRelativeHeaderOffset());
    }
    
    @Test
    public void testSetRelativeHeaderOffset() {
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(new byte[] {0x00, 0x01});
        zip64.setRelativeHeaderOffset(relativeHeaderOffset);
        assertEquals(relativeHeaderOffset, zip64.getRelativeHeaderOffset());
    }
    
    @Test
    public void testGetDiskStartNumber() {
        assertNull(zip64.getDiskStartNumber());
    }
    
    @Test
    public void testSetDiskStartNumber() {
        ZipLong diskStartNumber = new ZipLong(new byte[] {0x00, 0x01});
        zip64.setDiskStartNumber(diskStartNumber);
        assertEquals(diskStartNumber, zip64.getDiskStartNumber());
    }
    
}