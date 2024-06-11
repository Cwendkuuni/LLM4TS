import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0x0001, field.getHeaderId().getValue());
    }

    @Test
    public void testLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getLocalFileDataData().length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(0, field.getCentralDirectoryData().length);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        try {
            field.parseFromLocalFileData(new byte[]{}, 0, 0);
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            // Exception expected
        }
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        try {
            field.parseFromCentralDirectoryData(new byte[]{}, 0, 0);
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            // Exception expected
        }
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        try {
            field.reparseCentralDirectoryData(true, true, true, true);
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            // Exception expected
        }
    }

    @Test
    public void testGettersAndSetters() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(new ZipEightByteInteger(100));
        assertEquals(100, field.getSize().getValue());

        field.setCompressedSize(new ZipEightByteInteger(50));
        assertEquals(50, field.getCompressedSize().getValue());

        field.setRelativeHeaderOffset(new ZipEightByteInteger(200));
        assertEquals(200, field.getRelativeHeaderOffset().getValue());

        field.setDiskStartNumber(new ZipLong(1));
        assertEquals(1, field.getDiskStartNumber().getValue());
    }
}