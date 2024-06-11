import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testCreateWithSizeAndCompressedSize() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L));
        assertNotNull(extraField);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(0L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0L, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testCreateWithAllValues() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024L), new ZipEightByteInteger(512L),
                new ZipEightByteInteger(5L), new ZipLong(10L));
        assertNotNull(extraField);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(10L, extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutBothSizes() {
        new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(1024L), null);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] localFileData = new byte[2 * DWORD];
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        addSizes(localFileData, extraField);
        extraField.parseFromLocalFileData(localFileData, 0, localFileData.length);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(0L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0L, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllValues() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[3 * DWORD + WORD];
        addSizes(centralDirectoryData, extraField);
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(10L, extraField.getDiskStartNumber().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] centralDirectoryData = new byte[2 * DWORD - WORD];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
    }

    private void addSizes(byte[] data, Zip64ExtendedInformationExtraField extraField) {
        int off = 0;
        if (extraField.getSize() != null) {
            System.arraycopy(extraField.getSize().getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (extraField.getCompressedSize() != null) {
            System.arraycopy(extraField.getCompressedSize().getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (extraField.getRelativeHeaderOffset() != null) {
            System.arraycopy(extraField.getRelativeHeaderOffset().getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (extraField.getDiskStartNumber() != null) {
            System.arraycopy(extraField.getDiskStartNumber().getBytes(), 0, data, off, WORD);
        }
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = new byte[3 * DWORD + WORD];
        addSizes(rawCentralDirectoryData, extraField);
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(10L, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutUncompressedSize() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = new byte[2 * DWORD + WORD];
        addSizes(rawCentralDirectoryData, extraField);
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.reparseCentralDirectoryData(false, true, true, true);
        assertEquals(0L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(10L, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutCompressedSize() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = new byte[3 * DWORD];
        addSizes(rawCentralDirectoryData, extraField);
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, false, true, true);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(0L, extraField.getCompressedSize().getValue());
        assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(10L, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutRelativeHeaderOffset() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = new byte[3 * DWORD + DWORD];
        addSizes(rawCentralDirectoryData, extraField);
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, false, true);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(0L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(10L, extraField.getDiskStartNumber().getValue());
    }

    @Test
    public void testReparseCentralDirectoryDataWithoutDiskStartNumber() throws ZipException {
        Zip64ExtendedInformationExtraField extraField = new Zip64ExtendedInformationExtraField();
        byte[] rawCentralDirectoryData = new byte[3 * DWORD];
        addSizes(rawCentralDirectoryData, extraField);
        extraField.parseFromCentralDirectoryData(rawCentralDirectoryData, 0, rawCentralDirectoryData.length);
        extraField.reparseCentralDirectoryData(true, true, true, false);
        assertEquals(1024L, extraField.getSize().getValue());
        assertEquals(512L, extraField.getCompressedSize().getValue());
        assertEquals(5L, extraField.getRelativeHeaderOffset().getValue());
        assertEquals(0L, extraField.getDiskStartNumber().getValue());
    }
}