import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        assertNull(zip64.getSize());
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.setSize(new ZipEightByteInteger(100));
        assertEquals(new ZipEightByteInteger(100), zip64.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        assertNull(zip64.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.setCompressedSize(new ZipEightByteInteger(200));
        assertEquals(new ZipEightByteInteger(200), zip64.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        assertNull(zip64.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.setRelativeHeaderOffset(new ZipEightByteInteger(300));
        assertEquals(new ZipEightByteInteger(300), zip64.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        assertNull(zip64.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.setDiskStartNumber(new ZipLong(400));
        assertEquals(new ZipLong(400), zip64.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0x0001), zip64.getHeaderId());
    }
}