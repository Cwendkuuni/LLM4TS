import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Zip64ExtendedInformationExtraFieldTest {

    private static final byte[] VALID_DATA = new byte[] {
            0x00, 0x01, // LOCAL_FILE_HEADER_SIGNATURE
            0x02, 0x01, // LOCAL_FILE_HEADER_VERSION_MADE_BY
            0x03, 0x01, // LOCAL_FILE_HEADER_VERSION_TO_EXTRACT
            0x04, 0x00, // GENERIC_FLAG
            0x05, 0x06, // COMPRESSED_SIZE
            0x07, 0x08, // UNCOMPRESSED_SIZE_LOW
            0x09, 0x0A, // UNCOMPRESSED_SIZE_HIGH
            0x0B, 0x0C, // RELATIVE_HEADER_OFFSET_LOW
            0x0D, 0x0E, // RELATIVE_HEADER_OFFSET_HIGH
            0x0F, 0x10, // DISK_START
            0x11, 0x12, // DISK_START_LOW
            0x13, 0x14, // DISK_START_HIGH
            0x15, 0x01, // EXTRA_FIELD_LENGTH
            0x00, 0x00, 0x01, 0x00, // ZIP64_EXTENDED_INFO_SIGNATURE
            0x16, 0x17, 0x18, 0x19, // ZIP64_EXTENDED_INFO_SIZE_LOW
            0x1A, 0x1B, 0x1C, 0x1D, // ZIP64_EXTENDED_INFO_SIZE_HIGH
            0x1E, 0x1F // ZIP64_EXTENDED_INFO_TAG
    };

    private Zip64ExtendedInformationExtraField testee;

    @BeforeAll
    public void setUp() {
        testee = new Zip64ExtendedInformationExtraField(
            new ZipEightByteInteger(VALID_DATA, 10),
            new ZipEightByteInteger(VALID_DATA, 5),
            new ZipEightByteInteger(VALID_DATA, 14),
            new ZipLong(VALID_DATA, 18)
        );
    }

    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(new ZipShort(new byte[] {0x00, (byte) 0x0e}), testee.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(new byte[] {0x00, (byte) 0x1b}), testee.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] testData = testee.getLocalFileDataData();
        assertArrayEquals(VALID_DATA, testData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] testData = new byte[VALID_DATA.length];
        System.arraycopy(VALID_DATA, 0, testData, 0, VALID_DATA.length);
        testee.parseFromLocalFileData(testData, 0, VALID_DATA.length);
        assertEquals(new ZipEightByteInteger(VALID_DATA, 5), testee.getSize());
        assertEquals(new ZipEightByteInteger(VALID_DATA, 7), testee.getCompressedSize());
        assertEquals(new ZipEightByteInteger(VALID_DATA, 10), testee.getRelativeHeaderOffset());
        assertEquals(new ZipLong(VALID_DATA, 13), testee.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        testee.parseFromCentralDirectoryData(VALID_DATA, 0, VALID_DATA.length);
        assertEquals(new ZipEightByteInteger(VALID_DATA, 5), testee.getSize());
        assertEquals(new ZipEightByteInteger(VALID_DATA, 7), testee.getCompressedSize());
        assertEquals(new ZipEightByteInteger(VALID_DATA, 10), testee.getRelativeHeaderOffset());
        assertEquals(new ZipLong(VALID_DATA, 13), testee.getDiskStartNumber());
    }

    @Test
    public void testGetSize() {
        assertEquals(new ZipEightByteInteger(VALID_DATA, 5), testee.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        assertEquals(new ZipEightByteInteger(VALID_DATA, 7), testee.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        assertEquals(new ZipEightByteInteger(VALID_DATA, 10), testee.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        assertEquals(new ZipLong(VALID_DATA, 13), testee.getDiskStartNumber());
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), testee.getHeaderId());
    }

    @Test
    public void testEmptyConstructor() {
        Zip64ExtendedInformationExtraField emptyTestee = new Zip64ExtendedInformationExtraField();
        assertEquals(new ZipShort(0), emptyTestee.getLocalFileDataLength());
        assertEquals(new ZipShort(0), emptyTestee.getCentralDirectoryLength());
        assertEquals(new byte[0], emptyTestee.getLocalFileDataData());
        assertEquals(new byte[0], emptyTestee.getCentralDirectoryData());
        assertNull(emptyTestee.getSize());
        assertNull(emptyTestee.getCompressedSize());
        assertNull(emptyTestee.getRelativeHeaderOffset());
        assertNull(emptyTestee.getDiskStartNumber());
    }
}