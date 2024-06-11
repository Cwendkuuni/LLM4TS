import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    private Zip64ExtendedInformationExtraField zipField;

    @Before
    public void setup() {
        zipField = new Zip64ExtendedInformationExtraField(new ZipEightByteInteger(10),
                new ZipEightByteInteger(5));
    }

    @Test
    public void testGetHeaderId() {
        ZipShort expected = new ZipShort(0x0001);
        ZipShort actual = zipField.getHeaderId();
        Assert.assertEquals("Unexpected header id.", expected, actual);
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expected = new ZipShort(2 * ZipConstants.DWORD);
        ZipShort actual = zipField.getLocalFileDataLength();
        Assert.assertEquals("Unexpected local file data length.", expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expected = new ZipShort(2 * ZipConstants.DWORD +
                ZipConstants.DWORD + ZipConstants.WORD);
        ZipShort actual = zipField.getCentralDirectoryLength();
        Assert.assertEquals("Unexpected central directory length.", expected, actual);
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expected = new byte[2 * ZipConstants.DWORD];
        addSizes(expected);

        byte[] actual = zipField.getLocalFileDataData();
        Assert.assertArrayEquals("Unexpected local file data.", expected, actual);
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[zipField.getCentralDirectoryLength().getValue()];
        int off = addSizes(expected);
        System.arraycopy(zipField.relativeHeaderOffset.getBytes(), 0, expected, off, ZipConstants.DWORD);
        off += ZipConstants.DWORD;
        System.arraycopy(zipField.diskStart.getBytes(), 0, expected, off, ZipConstants.WORD);

        byte[] actual = zipField.getCentralDirectoryData();
        Assert.assertArrayEquals("Unexpected central directory data.", expected, actual);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] buffer = new byte[6 * ZipConstants.DWORD];
        int offset = 0;
        zipField.parseFromLocalFileData(buffer, offset, 6 * ZipConstants.DWORD);

        Assert.assertEquals("Unexpected size value.", zipField.size,
                new ZipEightByteInteger(buffer, offset));
        offset += ZipConstants.DWORD;
        Assert.assertEquals("Unexpected compressed size value.", zipField.compressedSize,
                new ZipEightByteInteger(buffer, offset));

        if (zipField.relativeHeaderOffset != null) {
            Assert.assertEquals("Unexpected relative header offset value.", zipField.relativeHeaderOffset,
                    new ZipEightByteInteger(buffer, offset));
            offset += ZipConstants.DWORD;
        }

        if (zipField.diskStart != null) {
            Assert.assertEquals("Unexpected disk start value.", zipField.diskStart,
                    new ZipLong(buffer, offset));
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithMissingSizeValues() throws ZipException {
        byte[] buffer = new byte[2 * ZipConstants.DWORD];
        int offset = 0;
        zipField.parseFromLocalFileData(buffer, offset, 2 * ZipConstants.DWORD);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[3 * ZipConstants.DWORD + ZipConstants.WORD];
        int offset = 0;
        zipField.parseFromCentralDirectoryData(buffer, offset,
                buffer.length);

        if (zipField.size != null) {
            Assert.assertEquals("Unexpected size value.", zipField.size,
                    new ZipEightByteInteger(buffer, offset));
            offset += ZipConstants.DWORD;
        }

        if (zipField.compressedSize != null) {
            Assert.assertEquals("Unexpected compressed size value.", zipField.compressedSize,
                    new ZipEightByteInteger(buffer, offset));
            offset += ZipConstants.DWORD;
        }

        if (zipField.relativeHeaderOffset != null) {
            Assert.assertEquals("Unexpected relative header offset value.", zipField.relativeHeaderOffset,
                    new ZipEightByteInteger(buffer, offset));
            offset += ZipConstants.DWORD;
        }

        if (zipField.diskStart != null) {
            Assert.assertEquals("Unexpected disk start value.", zipField.diskStart,
                    new ZipLong(buffer, offset));
        }
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithMissingField() throws ZipException {
        byte[] buffer = new byte[2 * ZipConstants.DWORD];
        int offset = 0;
        zipField.parseFromCentralDirectoryData(buffer, offset, buffer.length);
    }

    @Test
    public void testReparseCentralDirectoryData() throws ZipException {
        byte[] buffer = new byte[16];
        zipField.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        zipField.reparseCentralDirectoryData(true, true, true, true);

        Assert.assertEquals("Unexpected size value.", zipField.size,
                new ZipEightByteInteger(buffer, 0));
        Assert.assertEquals("Unexpected compressed size value.", zipField.compressedSize,
                new ZipEightByteInteger(buffer, ZipConstants.DWORD));

        if (zipField.relativeHeaderOffset != null) {
            Assert.assertEquals("Unexpected relative header offset value.", zipField.relativeHeaderOffset,
                    new ZipEightByteInteger(buffer, 2 * ZipConstants.DWORD));
        }

        if (zipField.diskStart != null) {
            Assert.assertEquals("Unexpected disk start value.", zipField.diskStart,
                    new ZipLong(buffer, 3 * ZipConstants.DWORD));
        }
    }

    @Test(expected = ZipException.class)
    public void testReparseCentralDirectoryDataWithMissingData() throws ZipException {
        byte[] buffer = new byte[ZipConstants.DWORD];
        zipField.reparseCentralDirectoryData(true, true, true, true);
    }

    private void addSizes(byte[] data) {
        int off = 0;
        if (zipField.size != null) {
            System.arraycopy(zipField.size.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (zipField.compressedSize != null) {
            System.arraycopy(zipField.compressedSize.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (zipField.relativeHeaderOffset != null) {
            System.arraycopy(zipField.relativeHeaderOffset.getBytes(), 0, data, off, ZipConstants.DWORD);
            off += ZipConstants.DWORD;
        }
        if (zipField.diskStart != null) {
            System.arraycopy(zipField.diskStart.getBytes(), 0, data, off, ZipConstants.WORD);
        }
    }
}