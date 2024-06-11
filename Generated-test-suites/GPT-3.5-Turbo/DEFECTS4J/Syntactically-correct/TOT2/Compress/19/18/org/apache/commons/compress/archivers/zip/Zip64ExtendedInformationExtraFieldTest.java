import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(50L);
        ZipLong diskStart = new ZipLong(1L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), Zip64ExtendedInformationExtraField.HEADER_ID);
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getCentralDirectoryLength());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];
        field.parseFromLocalFileData(data, 0, 16);

        // Test your conditions here
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];
        field.parseFromCentralDirectoryData(data, 0, 16);

        // Test your conditions here
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.reparseCentralDirectoryData(true, true, true, true);

        // Test your conditions here
    }

    @Test
    public void testEdgeCases() {
        // Test edge cases here
    }

    @Test
    public void testExceptionHandling() {
        // Test exception handling scenarios here
    }
}