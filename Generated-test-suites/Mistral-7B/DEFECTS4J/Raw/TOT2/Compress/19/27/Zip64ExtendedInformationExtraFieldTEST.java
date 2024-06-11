import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Test;
import static org.junit.Assert.*;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testValidZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024),
                new ZipEightByteInteger(512),
                new ZipEightByteInteger(10),
                new ZipLong(20));
        assertEquals("Expected header id to be 0x0001", ZipShort.ZERO, zip64.getHeaderId());
        assertEquals("Expected size to be 1024", new ZipEightByteInteger(1024), zip64.getSize());
        assertEquals("Expected compressed size to be 512", new ZipEightByteInteger(512), zip64.getCompressedSize());
        assertEquals("Expected relative header offset to be 10", new ZipEightByteInteger(10), zip64.getRelativeHeaderOffset());
        assertEquals("Expected disk start number to be 20", new ZipLong(20), zip64.getDiskStartNumber());
        assertEquals("Expected local file data length to be 16", new ZipShort(16), zip64.getLocalFileDataLength());
        assertEquals("Expected central directory length to be 24", new ZipShort(24), zip64.getCentralDirectoryLength());
    }

    @Test
    public void testEmptyZip64ExtendedInformationExtraField() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        assertEquals("Expected header id to be 0x0001", ZipShort.ZERO, zip64.getHeaderId());
        assertNull("Expected no size value", zip64.getSize());
        assertNull("Expected no compressed size value", zip64.getCompressedSize());
        assertNull("Expected no relative header offset value", zip64.getRelativeHeaderOffset());
        assertNull("Expected no disk start number value", zip64.getDiskStartNumber());
        assertEquals("Expected local file data length to be 0", new ZipShort(0), zip64.getLocalFileDataLength());
        assertEquals("Expected central directory length to be 0", new ZipShort(0), zip64.getCentralDirectoryLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidZip64ExtendedInformationExtraFieldSizeMismatch() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField(
                new ZipEightByteInteger(1024),
                null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidZip64ExtendedInformationExtraFieldNullSizes() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField(
                null,
                new ZipEightByteInteger(512));
    }

    @Test
    public void testParsingFromLocalFileData() {
        byte[] localData = new byte[] {
                0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00};
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.parseFromLocalFileData(localData, 0, localData.length);
        assertEquals("Expected header id to be 0x0001", ZipShort.ZERO, zip64.getHeaderId());
        assertEquals("Expected size to be 1024", new ZipEightByteInteger(1024), zip64.getSize());
        assertEquals("Expected compressed size to be 1024", new ZipEightByteInteger(1024), zip64.getCompressedSize());
        assertNull("Expected no relative header offset value", zip64.getRelativeHeaderOffset());
        assertNull("Expected no disk start number value", zip64.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testInvalidLocalFileDataLength() {
        byte[] localData = new byte[] {
                0x00, 0x01, 0x00, 0x00};
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.parseFromLocalFileData(localData, 0, localData.length);
    }

    @Test
    public void testParsingFromCentralDirectoryData() {
        byte[] centralData = new byte[] {
                0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00};
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.parseFromCentralDirectoryData(centralData, 0, centralData.length);
        assertEquals("Expected header id to be 0x0001", ZipShort.ZERO, zip64.getHeaderId());
        assertEquals("Expected size to be 1024", new ZipEightByteInteger(1024), zip64.getSize());
        assertEquals("Expected compressed size to be 1024", new ZipEightByteInteger(1024), zip64.getCompressedSize());
        assertEquals("Expected relative header offset to be 10", new ZipEightByteInteger(10), zip64.getRelativeHeaderOffset());
        assertEquals("Expected disk start number to be 3", new ZipLong(3), zip64.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testInvalidCentralDirectoryDataLength() {
        byte[] centralData = new byte[] {
                0x00, 0x01, 0x01, 0x00};
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.parseFromCentralDirectoryData(centralData, 0, centralData.length);
    }

    @Test
    public void testReparsingCentralDirectoryData() {
        byte[] centralData = new byte[] {
                0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00};
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.parseFromCentralDirectoryData(centralData, 0, centralData.length);
        zip64.reparseCentralDirectoryData(true, true, true, true);
        assertEquals("Expected size to be 1024", new ZipEightByteInteger(1024), zip64.getSize());
        assertEquals("Expected compressed size to be 1024", new ZipEightByteInteger(1024), zip64.getCompressedSize());
        assertEquals("Expected relative header offset to be 10", new ZipEightByteInteger(10), zip64.getRelativeHeaderOffset());
        assertEquals("Expected disk start number to be 3", new ZipLong(3), zip64.getDiskStartNumber());
    }

    @Test(expected = ZipException.class)
    public void testInvalidReparsingCentralDirectoryData() {
        byte[] centralData = new byte[] {
                0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x01};
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.parseFromCentralDirectoryData(centralData, 0, centralData.length);
        zip64.reparseCentralDirectoryData(true, true, true, true);
    }
}