import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(200L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(100L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(50L);
        ZipLong diskStartNumber = new ZipLong(2L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStartNumber);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x01, 0x00, 0x20, 0x00, 0x30, 0x00, 0x10};

        extraField.parseFromLocalFileData(data, 0, data.length);

        ZipEightByteInteger size = new ZipEightByteInteger(32L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(48L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(16L);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x00, 0x01, 0x00, 0x10, 0x00, 0x20};

        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);

        ZipEightByteInteger size = new ZipEightByteInteger(16L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(32L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(48L);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
    }
}