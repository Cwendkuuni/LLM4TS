import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.*;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = field.getHeaderId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLengthWithSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(WORDS_IN_DWORD * 2), new ZipEightByteInteger(WORDS_IN_DWORD * 2));
        ZipShort expected = new ZipShort(WORDS_IN_DWORD * 2 * DWORD);
        ZipShort actual = field.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataLengthWithSizesNull() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(null);
        field.setCompressedSize(null);
        ZipShort length = field.getLocalFileDataLength();
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(WORDS_IN_DWORD), new ZipEightByteInteger(WORDS_IN_DWORD), null, null);
        ZipShort expected = new ZipShort((WORDS_IN_DWORD * DWORD) + (WORDS_IN_DWORD * DWORD) + (WORDS_IN_DWORD * DWORD) + (WORDS_IN_DWORD * WORD));
        ZipShort actual = field.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataDataWithSizesPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(WORDS_IN_DWORD * 2), new ZipEightByteInteger(WORDS_IN_DWORD * 2));
        byte[] expected = new byte[WORDS_IN_DWORD * 2 * DWORD];
        addSizes(expected);
        byte[] actual = field.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalFileDataDataWithSizesNull() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(null);
        field.setCompressedSize(null);
        byte[] data = field.getLocalFileDataData();
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[WORDS_IN_DWORD * DWORD - 1];
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[WORDS_IN_DWORD * 2 * DWORD];
        field.parseFromLocalFileData(buffer, 0, buffer.length);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFieldsPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[WORDS_IN_DWORD * 3 * DWORD + WORDS_IN_DWORD * WORD];
        field.parseFromCentralDirectoryData(data, 0, data.length);
        assertTrue(field.hasSize());
        assertTrue(field.hasCompressedSize());
        assertTrue(field.hasRelativeHeaderOffset());
        assertTrue(field.hasDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithInvalidLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[WORDS_IN_DWORD * 3 * DWORD];
        field.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testReparseCentralDirectoryDataWithAllFieldsPresent() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[WORDS_IN_DWORD * 4 * DWORD];
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(true, true, true, true);
        assertTrue(field.hasSize());
        assertTrue(field.hasCompressedSize());
        assertTrue(field.hasRelativeHeaderOffset());
        assertTrue(field.hasDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[WORDS_IN_DWORD * 3 * DWORD];
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(false, false, false, true);
        assertFalse(field.hasSize());
        assertFalse(field.hasCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[WORDS_IN_DWORD * 4 * DWORD];
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(true, true, false, true);
        assertFalse(field.hasRelativeHeaderOffset());
    }

    @Test
    public void testReparseCentralDirectoryDataWithNoDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[WORDS_IN_DWORD * 4 * DWORD];
        field.parseFromCentralDirectoryData(data, 0, data.length);
        field.reparseCentralDirectoryData(true, true, true, false);
        assertFalse(field.hasDiskStartNumber());
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (size != null) {
            System.arraycopy(size.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
        if (compressedSize != null) {
            System.arraycopy(compressedSize.getBytes(), 0, data, off, DWORD);
            off += DWORD;
        }
    }

    private static final int WORDS_IN_DWORD = 4;
}