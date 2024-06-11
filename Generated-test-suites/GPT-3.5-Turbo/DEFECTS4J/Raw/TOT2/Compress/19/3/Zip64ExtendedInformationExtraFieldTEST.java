import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import java.util.zip.ZipException;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(100L));
        assertEquals(new ZipEightByteInteger(100L), extraField.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setCompressedSize(new ZipEightByteInteger(50L));
        assertEquals(new ZipEightByteInteger(50L), extraField.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(200L));
        assertEquals(new ZipEightByteInteger(200L), extraField.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setDiskStartNumber(new ZipLong(1));
        assertEquals(new ZipLong(1), extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidZip64ExtendedInformationExtraField() {
        // Trying to create extra field with null size should throw IllegalArgumentException
        new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(50L));
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x05, 0x00, 0x00, 0x00, 0x30, 0x75, 0x08, 0x00};
        extraField.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(8L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(5L), extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x01, 0x00, 0x08, 0x00, 0x05, 0x00, 0x00, 0x00, 0x30, 0x75, 0x08, 0x00};
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(8L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(5L), extraField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] buffer = {0x08, 0x00, 0x05, 0x00, 0x30, 0x75, 0x08, 0x00};
        extraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(new ZipEightByteInteger(8L), extraField.getSize());
        assertEquals(new ZipEightByteInteger(5L), extraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(200L), extraField.getRelativeHeaderOffset());
    }
}