import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testCreateWithAllValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStartNumber = new ZipLong(0x5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize, relativeHeaderOffset, diskStartNumber);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345L);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, null);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger(12345L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(6789L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(0x1234);
        ZipLong diskStartNumber = new ZipLong(0x5678);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStartNumber, extraField.getDiskStartNumber());
    }
}