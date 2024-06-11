import org.junit.Test;

import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        ZipShort expected = new ZipShort(0x0001);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(expected, field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expected = new ZipShort(16);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(expected, field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expected = new ZipShort(16);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertEquals(expected, field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = new byte[16];
        expected[0] = 0;
        expected[8] = 0;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(expected, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[16];
        expected[0] = 0;
        expected[8] = 0;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        assertArrayEquals(expected, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] buffer = new byte[16];
        buffer[0] = 0;
        buffer[8] = 0;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, 16);
        assertEquals(0, field.getSize().getValue());
        assertEquals(0, field.getCompressedSize().getValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        byte[] buffer = new byte[8];
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, 0, 8);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = new byte[16];
        buffer[0] = 0;
        buffer[8] = 0;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 16);
        assertEquals(0, field.getSize().getValue());
        assertEquals(0, field.getCompressedSize().getValue());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] buffer = new byte[16];
        buffer[0] = 0;
        buffer[8] = 0;
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = false;
        boolean hasDiskStart = false;
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, 0, 16);
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
        assertEquals(0, field.getSize().getValue());
        assertEquals(0, field.getCompressedSize().getValue());
    }

    @Test
    public void testGetSize() {
        ZipEightByteInteger expected = new ZipEightByteInteger(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(expected, null);
        assertEquals(expected, field.getSize());
    }

    @Test
    public void testSetSize() {
        ZipEightByteInteger expected = new ZipEightByteInteger(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(expected);
        assertEquals(expected, field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        ZipEightByteInteger expected = new ZipEightByteInteger(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, expected);
        assertEquals(expected, field.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        ZipEightByteInteger expected = new ZipEightByteInteger(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setCompressedSize(expected);
        assertEquals(expected, field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        ZipEightByteInteger expected = new ZipEightByteInteger(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null, expected, null);
        assertEquals(expected, field.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        ZipEightByteInteger expected = new ZipEightByteInteger(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setRelativeHeaderOffset(expected);
        assertEquals(expected, field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        ZipLong expected = new ZipLong(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(null, null, null, expected);
        assertEquals(expected, field.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        ZipLong expected = new ZipLong(10);
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setDiskStartNumber(expected);
        assertEquals(expected, field.getDiskStartNumber());
    }
}