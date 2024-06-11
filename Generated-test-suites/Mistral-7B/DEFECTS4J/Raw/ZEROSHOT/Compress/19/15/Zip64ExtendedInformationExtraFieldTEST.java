import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateInstanceWithValidParameters() {
        ZipEightByteInteger size = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(512 * 1024);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(4096);
        ZipLong diskStart = new ZipLong(0x12345678901234L);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, relativeHeaderOffset, diskStart);

        Assertions.assertNotNull(extraField);
        Assertions.assertEquals(size, extraField.getSize());
        Assertions.assertEquals(compressedSize, extraField.getCompressedSize());
        Assertions.assertEquals(relativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assertions.assertEquals(diskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testCreateInstanceWithNullParameters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(1024 * 1024));
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024 * 1024), null);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024 * 1024), new ZipEightByteInteger(1024 * 1024), null);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024 * 1024), new ZipEightByteInteger(1024 * 1024), new ZipEightByteInteger(1024 * 1024), null);
        });
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger localFileSize = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger localFileCompressedSize = new ZipEightByteInteger(512 * 1024);
        System.arraycopy(localFileSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(localFileCompressedSize.getBytes(), 0, localFileData, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        Assertions.assertEquals(localFileSize, extraField.getSize());
        Assertions.assertEquals(localFileCompressedSize, extraField.getCompressedSize());
        Assertions.assertNull(extraField.getRelativeHeaderOffset());
        Assertions.assertNull(extraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger centralDirectorySize = new ZipEightByteInteger(1024 * 1024);
        ZipEightByteInteger centralDirectoryCompressedSize = new ZipEightByteInteger(512 * 1024);
        System.arraycopy(centralDirectorySize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(centralDirectoryCompressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        ZipEightByteInteger centralDirectoryRelativeHeaderOffset = new ZipEightByteInteger(4096);
        System.arraycopy(centralDirectoryRelativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        ZipLong centralDirectoryDiskStart = new ZipLong(0x12345678901234L);
        byte[] centralDirectoryDiskStartBytes = centralDirectoryDiskStart.getBytes();
        System.arraycopy(centralDirectoryDiskStartBytes, 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        Assertions.assertEquals(centralDirectorySize, extraField.getSize());
        Assertions.assertEquals(centralDirectoryCompressedSize, extraField.getCompressedSize());
        Assertions.assertEquals(centralDirectoryRelativeHeaderOffset, extraField.getRelativeHeaderOffset());
        Assertions.assertEquals(centralDirectoryDiskStart, extraField.getDiskStartNumber());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(4096));
        extraField.setDiskStartNumber(new ZipLong(0x12345678901234L));

        ZipShort length = extraField.getLocalFileDataLength();
        Assertions.assertEquals(new ZipShort(2 * DWORD), length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(4096));
        extraField.setDiskStartNumber(new ZipLong(0x12345678901234L));

        ZipShort length = extraField.getCentralDirectoryLength();
        Assertions.assertEquals(new ZipShort((1 * DWORD) + (1 * DWORD) + (1 * DWORD) + (1 * WORD)), length);
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));

        byte[] localFileData = extraField.getLocalFileDataData();

        Assertions.assertNotNull(localFileData);
        Assertions.assertEquals(2 * DWORD, localFileData.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.setSize(new ZipEightByteInteger(1024 * 1024));
        extraField.setCompressedSize(new ZipEightByteInteger(512 * 1024));
        extraField.setRelativeHeaderOffset(new ZipEightByteInteger(4096));
        extraField.setDiskStartNumber(new ZipLong(0x12345678901234L));

        byte[] centralDirectoryData = extraField.getCentralDirectoryData();

        Assertions.assertNotNull(centralDirectoryData);
        Assertions.assertEquals(3 * DWORD + WORD, centralDirectoryData.length);
    }
}