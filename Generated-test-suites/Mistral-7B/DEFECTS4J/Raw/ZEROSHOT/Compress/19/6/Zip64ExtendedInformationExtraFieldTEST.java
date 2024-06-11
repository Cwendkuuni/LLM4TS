import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipExtendedData;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateExtraFieldWithSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertTrue(extraField.getLocalFileDataLength().getValue() == 16);
        assertTrue(extraField.getCentralDirectoryLength().getValue() == 24);
    }

    @Test
    public void testCreateExtraFieldWithAllValues() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((long) 1234);
        ZipLong diskStart = new ZipLong((long) 5678);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize,
                relativeHeaderOffset, diskStart);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
        assertTrue(extraField.getLocalFileDataLength().getValue() == 28);
        assertTrue(extraField.getCentralDirectoryLength().getValue() == 28 + 8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateExtraFieldWithoutBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, null);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[16];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        int offset = 0;
        System.arraycopy(originalSize.getBytes(), 0, localFileData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, offset, DWORD);

        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithWrongSize() {
        byte[] localFileData = new byte[8];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[32];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertTrue(extraField.getLocalFileDataLength().getValue() == 0);
        assertTrue(extraField.getCentralDirectoryLength().getValue() == 0);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] centralDirectoryData = new byte[32];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((long) 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((long) 512);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger((long) 1234);
        ZipLong diskStart = new ZipLong((long) 5678);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, 4, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 8, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 12, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(originalSize, extraField.getSize());
        assertEquals(compressedSize, extraField.getCompressedSize());
        assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        assertEquals(diskStart, extraField.getDiskStartNumber());
        assertTrue(extraField.getLocalFileDataLength().getValue() == 28);
        assertTrue(extraField.getCentralDirectoryLength().getValue() == 28 + 8);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStart() {
        byte[] centralDirectoryData = new byte[32];
        ZipLong diskStart = new ZipLong((long) 5678);

        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 24, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertTrue(extraField.getLocalFileDataLength().getValue() == 0);
        assertTrue(extraField.getCentralDirectoryLength().getValue() == 0);
        assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[32];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertTrue(extraField.getLocalFileDataLength().getValue() == 16);
        assertTrue(extraField.getCentralDirectoryLength().getValue() == 24);
        assertEquals(extraField.getSize(), new ZipEightByteInteger((long) 1024));
        assertEquals(extraField.getCompressedSize(), new ZipEightByteInteger((long) 512));
    }
}