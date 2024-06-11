import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(1L);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        byte[] data = new byte[] { 0, 0, 0, 100, 0, 0, 0, 50 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(data, 0, 8);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        byte[] data = new byte[] { 0, 0, 0, 100, 0, 0, 0, 50 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, 8);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        byte[] data = new byte[] { 0, 0, 0, 100, 0, 0, 0, 50 };
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(data, 0, 8);

        field.reparseCentralDirectoryData(true, true, false, false);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
}