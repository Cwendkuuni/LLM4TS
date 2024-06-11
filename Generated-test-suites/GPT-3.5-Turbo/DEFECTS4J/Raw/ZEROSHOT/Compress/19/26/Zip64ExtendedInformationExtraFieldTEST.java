import org.apache.commons.compress.archivers.zip.ZipException;
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
        assertEquals(new ZipShort(16), zip64ExtendedInformationExtraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(20), zip64ExtendedInformationExtraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        assertArrayEquals(new byte[0], zip64ExtendedInformationExtraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertArrayEquals(new byte[20], zip64ExtendedInformationExtraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = new byte[16];
        zip64ExtendedInformationExtraField.parseFromLocalFileData(buffer, 0, 16);
        // TODO: Add assertions
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[20];
        zip64ExtendedInformationExtraField.parseFromCentralDirectoryData(buffer, 0, 20);
        // TODO: Add assertions
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        zip64ExtendedInformationExtraField.reparseCentralDirectoryData(true, true, true, true);
        // TODO: Add assertions
    }

    @Test
    public void testGetSize() {
        assertNull(zip64ExtendedInformationExtraField.getSize());
    }

    @Test
    public void testSetSize() {
        zip64ExtendedInformationExtraField.setSize(new ZipEightByteInteger(1000));
        // TODO: Add assertions
    }

    @Test
    public void testGetCompressedSize() {
        assertNull(zip64ExtendedInformationExtraField.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        zip64ExtendedInformationExtraField.setCompressedSize(new ZipEightByteInteger(500));
        // TODO: Add assertions
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        assertNull(zip64ExtendedInformationExtraField.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        zip64ExtendedInformationExtraField.setRelativeHeaderOffset(new ZipEightByteInteger(100));
        // TODO: Add assertions
    }

    @Test
    public void testGetDiskStartNumber() {
        assertNull(zip64ExtendedInformationExtraField.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        zip64ExtendedInformationExtraField.setDiskStartNumber(new ZipLong(5));
        // TODO: Add assertions
    }
}