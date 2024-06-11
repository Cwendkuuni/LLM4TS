import org.junit.*;
import static org.junit.Assert.*;

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
        assertEquals(new ZipShort(16), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] expected = {0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] expected = {0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0, 0};
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.reparseCentralDirectoryData(true, true, true, true);
        assertNull(field.getSize());
        assertNull(field.getCompressedSize());
        assertNull(field.getRelativeHeaderOffset());
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger(1234567890L);
        field.setSize(size);
        assertEquals(size, field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(1234567890L);
        field.setCompressedSize(compressedSize);
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(1234567890L);
        field.setRelativeHeaderOffset(relativeHeaderOffset);
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipLong diskStartNumber = new ZipLong(12345L);
        field.setDiskStartNumber(diskStartNumber);
        assertEquals(diskStartNumber, field.getDiskStartNumber());
    }


}