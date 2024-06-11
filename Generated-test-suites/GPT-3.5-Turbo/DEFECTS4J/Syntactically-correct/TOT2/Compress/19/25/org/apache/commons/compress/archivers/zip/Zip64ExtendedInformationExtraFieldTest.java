import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructor() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertNotNull(extraField);
    }

    @Test
    public void testHeaderId() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(0x0001, extraField.getHeaderId().getValue());
    }

    @Test
    public void testLocalFileDataLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(16, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(8, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x64, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00};
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(100L, extraField.getSize().getLongValue());
        assertEquals(50L, extraField.getCompressedSize().getLongValue());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = {0x64, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(100L, extraField.getSize().getLongValue());
        assertEquals(50L, extraField.getCompressedSize().getLongValue());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = {0x64, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00};
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, false);
        assertEquals(100L, extraField.getSize().getLongValue());
        assertEquals(50L, extraField.getCompressedSize().getLongValue());
    }

    @Test
    public void testGettersAndSetters() {
        ZipEightByteInteger size = new ZipEightByteInteger(100L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(50L);
        ZipEightByteInteger offset = new ZipEightByteInteger(10L);
        ZipLong diskStart = new ZipLong(2);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, offset, diskStart);
        assertEquals(size, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(offset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());

        extraField.setSize(new ZipEightByteInteger(200L));
        assertEquals(200L, extraField.getSize().getLongValue());

        extraField.setCompressedSize(new ZipEightByteInteger(100L));
        assertEquals(100L, extraField.getCompressedSize().getLongValue());

        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(20L));
        assertEquals(20L, extraField.getRelativeHeaderOffset().getLongValue());

        extraField.setDiskStartNumber(new ZipLong(3));
        assertEquals(3, extraField.getDiskStartNumber().getValue());
    }
}