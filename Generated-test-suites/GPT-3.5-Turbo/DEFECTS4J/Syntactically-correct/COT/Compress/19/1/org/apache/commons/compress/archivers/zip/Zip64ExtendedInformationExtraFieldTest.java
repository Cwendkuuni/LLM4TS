
import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    // Test constructor 1
    @Test
    public void testConstructor1() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertNotNull(field);
    }

    // Test constructor 2
    @Test
    public void testConstructor2() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(56789012);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    // Test constructor 3
    @Test
    public void testConstructor3() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(56789012);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(34567890);
        ZipLong diskStart = new ZipLong(98765432);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    // Test getHeaderId
    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }

    // Test getLocalFileDataLength
    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getLocalFileDataLength());
    }

    // Test getCentralDirectoryLength
    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), field.getCentralDirectoryLength());
    }

    // Test getLocalFileDataData
    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getLocalFileDataData());
    }

    // Test getCentralDirectoryData
    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(new byte[0], field.getCentralDirectoryData());
    }

    // Test parseFromLocalFileData
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(56789012);
        byte[] buffer = new byte[16];
        size.getBytes(buffer, 0);
        compressedSize.getBytes(buffer, 8);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, 16);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    // Test parseFromCentralDirectoryData
    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(56789012);
        byte[] buffer = new byte[20];
        size.getBytes(buffer, 0);
        compressedSize.getBytes(buffer, 8);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 20);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }

    // Test reparseCentralDirectoryData
    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(56789012);
        byte[] buffer = new byte[20];
        size.getBytes(buffer, 0);
        compressedSize.getBytes(buffer, 8);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 20);
        field.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
    }
}
