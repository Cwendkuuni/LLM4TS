import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import java.util.zip.ZipException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorWithTwoSizes() {
        ZipEightByteInteger size = new ZipEightByteInteger(100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50);
        
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testConstructorWithAllValues() {
        ZipEightByteInteger size = new ZipEightByteInteger(200);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(100);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(300);
        ZipLong diskStart = new ZipLong(1);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataData() {
        ZipEightByteInteger size = new ZipEightByteInteger(300);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(150);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);

        byte[] data = extraField.getLocalFileDataData();

        assertNotNull(data);
        assertEquals(16, data.length); // 8 bytes for size and 8 bytes for compressed size
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 100};
        extraField.parseFromLocalFileData(buffer, 0, 16);

        assertEquals(new ZipEightByteInteger(200), extraField.getSize());
        assertEquals(new ZipEightByteInteger(100), extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataThrowsExceptionWhenLengthIsNotEnough() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0, 200};
        extraField.parseFromCentralDirectoryData(buffer, 0, 8);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 100};
        extraField.parseFromCentralDirectoryData(buffer, 0, 16);

        assertEquals(new ZipEightByteInteger(200), extraField.getSize());
        assertEquals(new ZipEightByteInteger(100), extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataThrowsExceptionWhenLengthDoesntMatchExpectedLength() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.reparseCentralDirectoryData(true, true, true, true);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        byte[] buffer = new byte[] {0, 0, 0, 0, 0, 0, 0, 200, 0, 0, 0, 0, 0, 0, 0, 100};
        extraField.parseFromLocalFileData(buffer, 0, 16);
        extraField.reparseCentralDirectoryData(true, true, false, false);

        assertEquals(new ZipEightByteInteger(200), extraField.getSize());
        assertEquals(new ZipEightByteInteger(100), extraField.getCompressedSize());
    }
}