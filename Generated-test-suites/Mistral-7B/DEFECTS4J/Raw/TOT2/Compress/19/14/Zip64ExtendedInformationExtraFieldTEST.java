import org.apache.commons.compress.archivers.zip.*;
import org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithBothSizes() {
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 50);
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);

        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testCreateWithoutSizesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Zip64ExtendedInformationExtraField(null, null);
        });
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizes() {
        byte[] localFileData = new byte[2 * DWORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 50);
        System.arraycopy(originalSize.getBytes(), 0, localFileData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, localFileData, DWORD, DWORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);

        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
    }

    @Test
    public void testParseFromLocalFileDataWithOnlyOneSizeThrowsException() {
        byte[] localFileData = new byte[DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        assertThrows(ZipException.class, () -> {
            extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        });
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10);
        ZipLong diskStart = new ZipLong(1);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithoutAllFields() {
        byte[] centralDirectoryData = new byte[2 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        // All fields are optional so no specific check is required
    }

    @Test
    public void testParseFromCentralDirectoryDataWithOnlyDiskStartField() {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        ZipLong diskStart = new ZipLong(1);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);

        assertEquals(0, extraField.getSize().getValue());
        assertEquals(0, extraField.getCompressedSize().getValue());
        assertEquals(0, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithAllFields() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        ZipEightByteInteger originalSize = new ZipEightByteInteger((short) 100);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 50);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(10);
        ZipLong diskStart = new ZipLong(1);

        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, 0, DWORD);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, DWORD, DWORD);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, 2 * DWORD, DWORD);
        System.arraycopy(diskStart.getBytes(), 0, centralDirectoryData, 3 * DWORD, WORD);

        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);

        assertEquals(originalSize.getValue(), extraField.getSize().getValue());
        assertEquals(compressedSize.getValue(), extraField.getCompressedSize().getValue());
        assertEquals(relativeHeaderOffset.getValue(), extraField.getRelativeHeaderOffset().getValue());
        assertEquals(diskStart.getValue(), extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutAllFields() throws ZipException {
        byte[] centralDirectoryData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, true, false);

        // All fields are optional so no specific check is required
    }
}