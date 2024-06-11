import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        ZipShort expectedLength = new ZipShort(2 * DWORD);
        ZipShort actualLength = extraField.getLocalFileDataLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L), new ZipEightByteInteger(100L), new ZipLong(1234L));
        ZipShort expectedLength = new ZipShort(2 * DWORD + DWORD + DWORD + WORD);
        ZipShort actualLength = extraField.getCentralDirectoryLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataDataWithMissingSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] data = extraField.getLocalFileDataData();
        assertNotNull(data);
        assertEquals(0, data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataDataWithInvalidSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), null);
        extraField.getLocalFileDataData();
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x03,
                (byte) 0x04, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x05,
                (byte) 0x00};
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, offset, buffer.length);
        ZipEightByteInteger size = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        assertEquals(new ZipEightByteInteger(1024L), size);
        assertEquals(new ZipEightByteInteger(512L), compressedSize);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizes() {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00};
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(buffer, offset, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x03, (byte) 0x00};
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x03,
                (byte) 0x04, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x05,
                (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x07, (byte) 0x00,
                (byte) 0x07, (byte) 0x00};
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        ZipEightByteInteger size = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(buffer, offset);
        offset += DWORD;
        ZipLong diskStart = new ZipLong(buffer, offset);
        offset += WORD;
        assertEquals(new ZipEightByteInteger(1024L), size);
        assertEquals(new ZipEightByteInteger(512L), compressedSize);
        assertEquals(new ZipEightByteInteger(100L), relativeHeaderOffset);
        assertEquals(new ZipLong(1234L), diskStart);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x03,
                (byte) 0x04, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x05,
                (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x07, (byte) 0x00,
                (byte) 0x07, (byte) 0x00};
        int offset = 0;
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        ZipEightByteInteger size = extraField.getSize();
        assertEquals(new ZipEightByteInteger(1024L), size);
        ZipEightByteInteger compressedSize = extraField.getCompressedSize();
        assertEquals(new ZipEightByteInteger(512L), compressedSize);
        ZipEightByteInteger relativeHeaderOffset = extraField.getRelativeHeaderOffset();
        assertEquals(new ZipEightByteInteger(100L), relativeHeaderOffset);
        ZipLong diskStart = extraField.getDiskStartNumber();
        assertEquals(new ZipLong(1234L), diskStart);
    }
}