import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0x0001, extraField.getHeaderId().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(4, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(12, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(98765432);
        
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        byte[] data = extraField.getLocalFileDataData();
        
        assertEquals(16, data.length);
        assertEquals(0x8E, data[0]); 
        assertEquals(0x00, data[1]);
        assertEquals(0xBC, data[8]);
        assertEquals(0x4A, data[15]);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[] { 0x8E, 0x00, 0x00, 0x00, 0xBC, 0x4A };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(12345678, extraField.getSize().getValue());
        assertEquals(98765432, extraField.getCompressedSize().getValue());
    }

    @Test
    public void testRootElementConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(12345678);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(98765432);
        ZipEightByteInteger relHeaderOffset = new ZipEightByteInteger(54321);
        ZipLong diskStart = new ZipLong(7);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relHeaderOffset, diskStart);

        assertEquals(12345678, extraField.getSize().getValue());
        assertEquals(98765432, extraField.getCompressedSize().getValue());
        assertEquals(54321, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(7, extraField.getDiskStartNumber().getValue());
        
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] data = new byte[] { 0x8E, 0x00, 0xBC, 0x4A, 0x00, 0xD4 };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);

        assertEquals(12345678, extraField.getSize().getValue());
        assertEquals(98765432, extraField.getCompressedSize().getValue());
        assertEquals(7, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testInvalidParseFromLocalFileData() {
        byte[] data = new byte[] { 0x8E, 0x00, 0xBC };

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromLocalFileData(data, 0, data.length);
            fail("Expected ZipException");
        } catch (ZipException e) {
            // Expected
        }
    }

}