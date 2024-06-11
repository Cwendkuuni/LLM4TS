import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals(new ZipShort(20), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = new byte[16];
        byte[] actual = field.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[20];
        byte[] actual = field.getCentralDirectoryData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[16];
        int offset = 0;
        int length = 16;
        try {
            field.parseFromLocalFileData(buffer, offset, length);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = 20;
        try {
            field.parseFromCentralDirectoryData(buffer, offset, length);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReparseCentralDirectoryData() {
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        try {
            field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSize() {
        assertNull(field.getSize());
    }

    @Test
    public void testSetSize() {
        ZipEightByteInteger size = new ZipEightByteInteger();
        field.setSize(size);
        assertEquals(size, field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        assertNull(field.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        ZipEightByteInteger compressedSize = new ZipEightByteInteger();
        field.setCompressedSize(compressedSize);
        assertEquals(compressedSize, field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        assertNull(field.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        ZipEightByteInteger rho = new ZipEightByteInteger();
        field.setRelativeHeaderOffset(rho);
        assertEquals(rho, field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        ZipLong ds = new ZipLong();
        field.setDiskStartNumber(ds);
        assertEquals(ds, field.getDiskStartNumber());
    }

    @Test
    public void testAddSizes() {
        byte[] data = new byte[16];
        int off = field.addSizes(data);
        assertEquals(8, off);
    }

}