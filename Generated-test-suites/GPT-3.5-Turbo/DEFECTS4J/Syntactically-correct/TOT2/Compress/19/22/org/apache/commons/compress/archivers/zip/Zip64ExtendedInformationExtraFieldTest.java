import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10);
        ZipLong diskStart = new ZipLong(1);
        
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        
        assertEquals(size, zipField.getSize());
        assertEquals(compressedSize, zipField.getCompressedSize());
        assertEquals(relativeHeaderOffset, zipField.getRelativeHeaderOffset());
        assertEquals(diskStart, zipField.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[20];
        buffer[0] = 0x64; // size - 100
        buffer[1] = 0x00; 
        buffer[8] = 0x32; // compressed size - 50
        buffer[9] = 0x00; 
        
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        zipField.parseFromLocalFileData(buffer, 0, 20);
        
        assertEquals(new ZipEightByteInteger(100), zipField.getSize());
        assertEquals(new ZipEightByteInteger(50), zipField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        zipField.setCompressedSize(new ZipEightByteInteger(50));
        
        zipField.reparseCentralDirectoryData(false, true, false, false);
        
        assertEquals(new ZipEightByteInteger(50), zipField.getCompressedSize());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        byte[] expected = {0x64, 0x00, 0x32, 0x00};
        
        assertArrayEquals(expected, zipField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10);
        ZipLong diskStart = new ZipLong(1);
        
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        
        byte[] expected = {0x64, 0x00, 0x32, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x00};
        
        assertArrayEquals(expected, zipField.getCentralDirectoryData());
    }
}