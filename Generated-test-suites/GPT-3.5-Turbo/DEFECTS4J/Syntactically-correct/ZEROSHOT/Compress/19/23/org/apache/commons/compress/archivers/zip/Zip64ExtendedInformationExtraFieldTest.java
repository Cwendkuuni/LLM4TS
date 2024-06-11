import org.junit.Before;
import org.junit.Test;

import java.util.zip.ZipException;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField;

    @Before
    public void setUp() {
        zip64ExtendedInformationExtraField = new Zip64ExtendedInformationExtraField();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x0001), zip64ExtendedInformationExtraField.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        assertEquals(new ZipShort(2 * ZipConstants.DWORD), zip64ExtendedInformationExtraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort((zip64ExtendedInformationExtraField.getSize() != null ? ZipConstants.DWORD : 0)
                            + (zip64ExtendedInformationExtraField.getCompressedSize() != null ? ZipConstants.DWORD : 0)
                            + (zip64ExtendedInformationExtraField.getRelativeHeaderOffset() != null ? ZipConstants.DWORD : 0)
                            + (zip64ExtendedInformationExtraField.getDiskStartNumber() != null ? ZipConstants.WORD : 0)), zip64ExtendedInformationExtraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        if (zip64ExtendedInformationExtraField.getSize() != null || zip64ExtendedInformationExtraField.getCompressedSize() != null) {
            if (zip64ExtendedInformationExtraField.getSize() == null || zip64ExtendedInformationExtraField.getCompressedSize() == null) {
                try {
                    zip64ExtendedInformationExtraField.getLocalFileDataData();
                    fail("IllegalArgumentException was not thrown.");
                } catch (IllegalArgumentException e) {
                    assertEquals("Zip64 extended information must contain both size values in the local file header.", e.getMessage());
                }
            } else {
                byte[] data = new byte[2 * ZipConstants.DWORD];
                zip64ExtendedInformationExtraField.addSizes(data);
                assertArrayEquals(data, zip64ExtendedInformationExtraField.getLocalFileDataData());
            }
        } else {
            assertArrayEquals(new byte[0], zip64ExtendedInformationExtraField.getLocalFileDataData());
        }
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] data = new byte[zip64ExtendedInformationExtraField.getCentralDirectoryLength().getValue()];
        int off = zip64ExtendedInformationExtraField.addSizes(data);
        if (zip64ExtendedInformationExtraField.getRelativeHeaderOffset() != null) {
            System.arraycopy(zip64ExtendedInformationExtraField.getRelativeHeaderOffset().getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (zip64ExtendedInformationExtraField.getDiskStartNumber() != null) {
            System.arraycopy(zip64ExtendedInformationExtraField.getDiskStartNumber().getBytes(), 0, data, off, ZipConstants.WORD);
            off += ZipConstants.WORD;
        }
        assertArrayEquals(data, zip64ExtendedInformationExtraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = {0x00, 0x01, 0x00, 0x10, 0x00, 0x10, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        zip64ExtendedInformationExtraField.parseFromLocalFileData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(buffer, 0), zip64ExtendedInformationExtraField.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), zip64ExtendedInformationExtraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(buffer, 0), zip64ExtendedInformationExtraField.getSize());
        assertNull(zip64ExtendedInformationExtraField.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = {0x00, 0x01, 0x00, 0x10, 0x00, 0x10, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        zip64ExtendedInformationExtraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertEquals(new ZipEightByteInteger(buffer, 0), zip64ExtendedInformationExtraField.getSize());
        assertEquals(new ZipEightByteInteger(buffer, 8), zip64ExtendedInformationExtraField.getCompressedSize());
        assertEquals(new ZipEightByteInteger(buffer, 0), zip64ExtendedInformationExtraField.getSize());
        assertNull(zip64ExtendedInformationExtraField.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = {0x00, 0x01, 0x00, 0x10, 0x00, 0x10, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        zip64ExtendedInformationExtraField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        assertTrue(zip64ExtendedInformationExtraField.reparseCentralDirectoryData(true, true, true, true));
    }
}