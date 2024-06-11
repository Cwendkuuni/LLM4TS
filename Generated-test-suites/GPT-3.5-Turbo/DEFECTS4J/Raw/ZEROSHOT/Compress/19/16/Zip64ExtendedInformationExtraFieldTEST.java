import static org.junit.Assert.*;
import org.junit.Test;
import java.util.zip.ZipException;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = Zip64ExtendedInformationExtraField.HEADER_ID;
        assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expected = new ZipShort(16);
        ZipShort actual = new Zip64ExtendedInformationExtraField().getLocalFileDataLength();
        assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expected = new ZipShort(28);
        ZipShort actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryLength();
        assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actual = new Zip64ExtendedInformationExtraField().getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actual = new Zip64ExtendedInformationExtraField().getCentralDirectoryData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
        // Verify parsing
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        // Verify parsing
        assertNotNull(extraField.getRawCentralDirectoryData());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = false;
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        // Verify re-parsing
        assertNotNull(extraField.getSize());
        assertNotNull(extraField.getCompressedSize());
        assertNull(extraField.getRelativeHeaderOffset());
        assertNull(extraField.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileData_Exception() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0, 0, 0, 0};
        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryData_Exception() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = false;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = false;
        extraField.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
    }
}