import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Zip64ExtendedInformationExtraFieldTest {
    
    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(16), field.getLocalFileDataLength());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(24), field.getCentralDirectoryLength());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] expected = new byte[16];
        assertEquals(expected, field.getLocalFileDataData());
    }
    
    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] expected = new byte[24];
        assertEquals(expected, field.getCentralDirectoryData());
    }
    
    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(new ZipEightByteInteger(10));
        assertEquals(new ZipEightByteInteger(10), field.getSize());
    }
    
    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setCompressedSize(new ZipEightByteInteger(20));
        assertEquals(new ZipEightByteInteger(20), field.getCompressedSize());
    }
    
    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setRelativeHeaderOffset(new ZipEightByteInteger(30));
        assertEquals(new ZipEightByteInteger(30), field.getRelativeHeaderOffset());
    }
    
    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setDiskStartNumber(new ZipLong(40));
        assertEquals(new ZipLong(40), field.getDiskStartNumber());
    }
    
    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[16];
        buffer[0] = 0x01; buffer[2] = 0x02;
        field.parseFromLocalFileData(buffer, 0, 8);
        assertEquals(new ZipEightByteInteger(buffer, 0), field.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), field.getCompressedSize());
    }
    
    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[24];
        buffer[0] = 0x01; buffer[2] = 0x02;
        field.parseFromCentralDirectoryData(buffer, 0, 12);
        assertEquals(new ZipEightByteInteger(buffer, 0), field.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(buffer, 16), field.getRelativeHeaderOffset());
    }
    
    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[24];
        buffer[0] = 0x01; buffer[2] = 0x02;
        field.parseFromCentralDirectoryData(buffer, 0, 12);
        field.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(new ZipEightByteInteger(buffer, 0), field.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), field.getCompressedSize());
        assertEquals(new ZipEightByteInteger(buffer, 16), field.getRelativeHeaderOffset());
        assertEquals(null, field.getDiskStartNumber());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseFromLocalFileDataInvalidLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[4];
        field.parseFromLocalFileData(buffer, 0, 4);
    }
    
    @Test(expected=ZipException.class)
    public void testParseFromLocalFileDataMissingSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[16];
        field.parseFromLocalFileData(buffer, 0, 16);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetSizeInvalidArgument() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(null);
    }
    
}