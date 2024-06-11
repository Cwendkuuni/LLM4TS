import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 10);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 5);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        ZipShort expected = new ZipShort(2 * ZipConstants.DWORD);
        ZipShort actual = testObject.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 10);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 5);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        testObject.setRelativeHeaderOffset(new ZipEightByteInteger(0));
        testObject.setDiskStart(new ZipLong(0));
        ZipShort expected = new ZipShort((size.getValue() + compressedSize.getValue())
                + (testObject.getRelativeHeaderOffset().getValue() + testObject.getDiskStart().getValue())
                + ZipConstants.WORD);
        ZipShort actual = testObject.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger size = new ZipEightByteInteger((short) 10);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger((short) 5);
        testObject.setSize(size);
        testObject.setCompressedSize(compressedSize);
        byte[] expected = new byte[]{0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x06,
                0x00, 0x00, 0x00, 0x06};
        byte[] actual = testObject.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseFromLocalFileDataWithBothSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00, 0x0A,
                0x00, 0x01, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01, (byte) 0xFF,
                (byte) 0xFF, 0x00, 0x00, 0x00, 0x06};
        int offset = 0;
        testObject.parseFromLocalFileData(buffer, offset, buffer.length);
        ZipEightByteInteger expectedSize = new ZipEightByteInteger((short) 10);
        ZipEightByteInteger expectedCompressedSize = new ZipEightByteInteger((short) 5);
        assertEquals(expectedSize.getValue(), testObject.getSize().getValue());
        assertEquals(expectedCompressedSize.getValue(), testObject.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithoutBothSizes() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x01, (byte) 0xFF, (byte) 0xFF};
        int offset = 0;
        testObject.parseFromLocalFileData(buffer, offset, buffer.length);
        fail("Expected ZipException: " + Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithAllFields() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x03, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x06};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertEquals(new ZipEightByteInteger((short) 10), testObject.getSize());
        assertEquals(new ZipEightByteInteger((short) 5), testObject.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0), testObject.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0), testObject.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryDataWithSizeOnly() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertEquals(new ZipEightByteInteger((short) 10), testObject.getSize());
        assertNull(testObject.getCompressedSize());
        assertNull(testObject.getRelativeHeaderOffset());
        assertNull(testObject.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithoutSize() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        fail("Expected ZipException: " + Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithRelativeHeaderOffsetOnly() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x02,
                0x00, 0x00, 0x00, 0x01};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertNull(testObject.getSize());
        assertNull(testObject.getCompressedSize());
        assertEquals(new ZipEightByteInteger((short) 2), testObject.getRelativeHeaderOffset());
        assertNull(testObject.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithoutRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        fail("Expected ZipException: " + Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }

    @Test
    public void testParseFromCentralDirectoryDataWithDiskStartNumberOnly() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        assertNull(testObject.getSize());
        assertNull(testObject.getCompressedSize());
        assertNull(testObject.getRelativeHeaderOffset());
        assertEquals(new ZipLong(1), testObject.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithoutDiskStartNumber() {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00};
        int offset = 0;
        testObject.parseFromCentralDirectoryData(buffer, offset, buffer.length);
        fail("Expected ZipException: " + Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x03, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x06};
        testObject.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        testObject.reparseCentralDirectoryData(true, true, true, true);
        assertEquals(new ZipEightByteInteger((short) 10), testObject.getSize());
        assertEquals(new ZipEightByteInteger((short) 5), testObject.getCompressedSize());
        assertEquals(new ZipEightByteInteger(0), testObject.getRelativeHeaderOffset());
        assertEquals(new ZipLong(0), testObject.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithOnlySizes() throws ZipException {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A};
        testObject.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        testObject.reparseCentralDirectoryData(true, true, false, false);
        assertNull(testObject.getRelativeHeaderOffset());
        assertNull(testObject.getDiskStartNumber());
        assertEquals(new ZipEightByteInteger((short) 10), testObject.getSize());
        assertEquals(new ZipEightByteInteger((short) 0), testObject.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryDataWithOnlyRelativeHeaderOffset() throws ZipException {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x02,
                0x00, 0x00, 0x00, 0x01};
        testObject.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        testObject.reparseCentralDirectoryData(false, false, true, false);
        assertNull(testObject.getSize());
        assertNull(testObject.getCompressedSize());
        assertEquals(new ZipEightByteInteger((short) 2), testObject.getRelativeHeaderOffset());
        assertNull(testObject.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryDataWithOnlyDiskStartNumber() throws ZipException {
        Zip64ExtendedInformationExtraField testObject = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01};
        testObject.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        testObject.reparseCentralDirectoryData(false, false, false, true);
        assertNull(testObject.getSize());
        assertNull(testObject.getCompressedSize());
        assertNull(testObject.getRelativeHeaderOffset());
        assertEquals(new ZipLong(1), testObject.getDiskStartNumber());
    }
}