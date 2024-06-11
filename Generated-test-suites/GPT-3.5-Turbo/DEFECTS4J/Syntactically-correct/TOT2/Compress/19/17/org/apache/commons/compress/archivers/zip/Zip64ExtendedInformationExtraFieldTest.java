import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithoutArguments() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();
        // Test if the object is instantiated correctly
        assertNotNull(zipField);
    }

    @Test
    public void testConstructorWithArguments() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(800);
        ZipEightByteInteger headerOffset = new ZipEightByteInteger(200);
        ZipLong diskStart = new ZipLong(1);

        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField(size, compressedSize, headerOffset, diskStart);

        // Test if the object is instantiated correctly with provided arguments
        assertNotNull(zipField);
    }

    @Test
    public void testParsingFromLocalFileData() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();

        byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        zipField.parseFromLocalFileData(data, 0, data.length);

        // Test if the data is correctly parsed from local file data
        assertNotNull(zipField.getSize());
        assertNotNull(zipField.getCompressedSize());
    }

    @Test
    public void testParsingFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();

        byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        zipField.parseFromCentralDirectoryData(data, 0, data.length);

        // Test if the data is correctly parsed from central directory data
        assertNotNull(zipField.getSize());
        assertNotNull(zipField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField zipField = new Zip64ExtendedInformationExtraField();

        byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        zipField.parseFromCentralDirectoryData(data, 0, data.length);
        zipField.reparseCentralDirectoryData(true, true, true, true);

        // Test if the central directory data is correctly reparsed
        assertNotNull(zipField.getSize());
        assertNotNull(zipField.getCompressedSize());
        assertNotNull(zipField.getRelativeHeaderOffset());
        assertNotNull(zipField.getDiskStartNumber());
    }
}