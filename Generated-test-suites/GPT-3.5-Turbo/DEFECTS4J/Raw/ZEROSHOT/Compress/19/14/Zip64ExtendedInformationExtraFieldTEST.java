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
        assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(new ZipShort(16), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(24), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        assertArrayEquals(expected, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        assertArrayEquals(expected, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = {
            0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03
        };
        field.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(1), field.getSize());
        assertEquals(new ZipEightByteInteger(2), field.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = {
            0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03
        };
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(1), field.getSize());
        assertEquals(new ZipEightByteInteger(2), field.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] buffer = {
            0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03
        };
        field.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        field.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(new ZipEightByteInteger(1), field.getSize());
        assertEquals(new ZipEightByteInteger(2), field.getCompressedSize());
    }

    @Test
    public void testGetSize() {
        field.setSize(new ZipEightByteInteger(1));
        assertEquals(new ZipEightByteInteger(1), field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        field.setCompressedSize(new ZipEightByteInteger(2));
        assertEquals(new ZipEightByteInteger(2), field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        field.setRelativeHeaderOffset(new ZipEightByteInteger(3));
        assertEquals(new ZipEightByteInteger(3), field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        field.setDiskStartNumber(new ZipLong(4));
        assertEquals(new ZipLong(4), field.getDiskStartNumber());
    }

    @Test
    public void testAddSizes() {
        byte[] data = new byte[12];
        int off = field.addSizes(data);
        assertEquals(8, off);
        byte[] expected = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        assertArrayEquals(expected, data);
    }
}