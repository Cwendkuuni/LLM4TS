import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField field;
    
    @Before
    public void setUp() {
        field = new Zip64ExtendedInformationExtraField();
    }
    
    @Test
    public void testGetHeaderId() {
        assertEquals(0x0001, field.getHeaderId().getValue());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(8, field.getLocalFileDataLength().getValue());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(16, field.getCentralDirectoryLength().getValue());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = new byte[] {0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, field.getLocalFileDataData());
    }
    
    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, field.getCentralDirectoryData());
    }
    
    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }
    
    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }
    
    @Test
    public void testReparseCentralDirectoryData() {
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }
}