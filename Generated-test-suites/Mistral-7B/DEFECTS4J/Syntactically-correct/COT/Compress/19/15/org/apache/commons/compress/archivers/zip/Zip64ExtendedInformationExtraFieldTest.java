import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, extraField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, extraField.getLocalFileDataLength());

        ZipEightByteInteger size = new ZipEightByteInteger(10L);
        Zip64ExtendedInformationExtraField extraFieldWithSize = new Zip64ExtendedInformationExtraField(size, null);
        assertEquals(new ZipShort(8), extraFieldWithSize.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        assertEquals(ZipShort.ZERO, extraField.getCentralDirectoryLength());

        ZipEightByteInteger size = new ZipEightByteInteger(10L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5L);
        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize);
        assertEquals(new ZipShort(16), extraField.getCentralDirectoryLength());

        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, null, null);
        assertEquals(new ZipShort(20), extraField.getCentralDirectoryLength());

        extraField = new Zip64ExtendedInformationExtraField(size, compressedSize, null, new ZipLong(3L));
        assertEquals(new ZipShort(22), extraField.getCentralDirectoryLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataDataWithMissingSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = extraField.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(0, localFileData.length);

        ZipEightByteInteger size = new ZipEightByteInteger(10L);
        Zip64ExtendedInformationExtraField extraFieldWithSize = new Zip64ExtendedInformationExtraField(size, null);
        byte[] localFileDataWithSize = extraFieldWithSize.getLocalFileDataData();
        assertNotNull(localFileDataWithSize);
        assertEquals(2 * DWORD, localFileDataWithSize.length);

        byte[] data = new byte[2 * DWORD];
        extraField.parseFromLocalFileData(data, 0, 2 * DWORD);

        localFileData = extraField.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(2 * DWORD, localFileData.length);

        Zip64ExtendedInformationExtraField extraFieldWithMissingSizes = new Zip64ExtendedInformationExtraField();
        localFileData = extraFieldWithMissingSizes.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(0, localFileData.length);

        byte[] dataWithMissingSizes = new byte[2 * DWORD];
        extraFieldWithMissingSizes.parseFromLocalFileData(dataWithMissingSizes, 0, 2 * DWORD);
        localFileData = extraFieldWithMissingSizes.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(0, localFileData.length);

        Zip64ExtendedInformationExtraField extraFieldWithOnlyCompressedSize = new Zip64ExtendedInformationExtraField(null, new ZipEightByteInteger(5L));
        localFileData = extraFieldWithOnlyCompressedSize.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(DWORD, localFileData.length);
    }

    @Test
    public void testGetLocalFileDataDataWithPresentSizes() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] localFileData = extraField.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(0, localFileData.length);

        ZipEightByteInteger originalSize = new ZipEightByteInteger(10L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5L);
        extraField = new Zip64ExtendedInformationExtraField(originalSize, compressedSize);
        localFileData = extraField.getLocalFileDataData();
        assertNotNull(localFileData);
        assertEquals(2 * DWORD, localFileData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidSizeData() {
        byte[] data = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        extraField.parseFromLocalFileData(data, 0, 2 * DWORD + 1);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] centralDirectoryData = new byte[0];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
            fail("Expected IllegalArgumentException for empty data");
        } catch (IllegalArgumentException e) {
            // expected
        }

        centralDirectoryData = new byte[3 * DWORD];
        int offset = 0;
        extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, offset, centralDirectoryData.length);
            fail("Expected ZipException for missing sizes");
        } catch (ZipException e) {
            // expected
        }

        offset += DWORD;
        ZipEightByteInteger originalSize = new ZipEightByteInteger(10L);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, offset, centralDirectoryData.length);
            fail("Expected ZipException for missing compressed size");
        } catch (ZipException e) {
            // expected
        }

        offset += DWORD;
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5L);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, offset, centralDirectoryData.length);
            fail("Expected ZipException for missing relative header offset");
        } catch (ZipException e) {
            // expected
        }

        offset += DWORD;
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(3L);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;

        extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, offset, centralDirectoryData.length);
            fail("Expected ZipException for missing disk start number");
        } catch (ZipException e) {
            // expected
        }

        offset += WORD;
        ZipLong diskStartNumber = new ZipLong(1L);
        System.arraycopy(diskStartNumber.getBytes(), 0, centralDirectoryData, offset, WORD);

        extraField = new Zip64ExtendedInformationExtraField();
        try {
            extraField.parseFromCentralDirectoryData(centralDirectoryData, offset, centralDirectoryData.length);
            Zip64ExtendedInformationExtraField parsedExtraField = extraField;
            assertEquals(originalSize, parsedExtraField.size);
            assertEquals(compressedSize, parsedExtraField.compressedSize);
            assertEquals(relativeHeaderOffset, parsedExtraField.relativeHeaderOffset);
            assertEquals(diskStartNumber, parsedExtraField.diskStart);
        } catch (ZipException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();

        // test no data
        extraField.reparseCentralDirectoryData(false, false, false, false);

        // test only original size
        int offset = 0;
        ZipEightByteInteger originalSize = new ZipEightByteInteger(10L);
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        extraField.reparseCentralDirectoryData(true, false, false, false);
        assertEquals(originalSize, extraField.size);

        // test only compressed size
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(5L);
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        extraField.reparseCentralDirectoryData(false, true, false, false);
        assertEquals(compressedSize, extraField.compressedSize);

        // test only relative header offset
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(3L);
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        extraField.reparseCentralDirectoryData(false, false, true, false);
        assertEquals(relativeHeaderOffset, extraField.relativeHeaderOffset);

        // test only disk start number
        ZipLong diskStartNumber = new ZipLong(1L);
        System.arraycopy(diskStartNumber.getBytes(), 0, centralDirectoryData, offset, WORD);
        extraField.reparseCentralDirectoryData(false, false, false, true);
        assertEquals(diskStartNumber, extraField.diskStart);

        // test all data
        System.arraycopy(originalSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(compressedSize.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(relativeHeaderOffset.getBytes(), 0, centralDirectoryData, offset, DWORD);
        offset += DWORD;
        System.arraycopy(diskStartNumber.getBytes(), 0, centralDirectoryData, offset, WORD);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(originalSize, extraField.size);
        assertEquals(compressedSize, extraField.compressedSize);
        assertEquals(relativeHeaderOffset, extraField.relativeHeaderOffset);
        assertEquals(diskStartNumber, extraField.diskStart);
    }
}