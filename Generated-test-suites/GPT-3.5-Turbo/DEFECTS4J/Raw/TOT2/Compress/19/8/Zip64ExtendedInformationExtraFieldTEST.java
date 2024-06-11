import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizeAndCompressedSize() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertNotNull(field);
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

        assertNotNull(field);
        assertEquals(size, field.getSize());
        assertEquals(compressedSize, field.getCompressedSize());
        assertEquals(relativeHeaderOffset, field.getRelativeHeaderOffset());
        assertEquals(diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        ZipShort headerId = Zip64ExtendedInformationExtraField.HEADER_ID;

        assertNotNull(headerId);
        assertEquals(0x0001, headerId.getValue());
    }

    // Add more test cases for each public method to cover edge cases and error scenarios

}