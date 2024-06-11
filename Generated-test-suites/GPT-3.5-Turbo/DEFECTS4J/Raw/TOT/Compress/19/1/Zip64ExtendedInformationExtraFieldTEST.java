import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipLong;
import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testConstructorAndGetters() {
        ZipEightByteInteger size = new ZipEightByteInteger(1000000000L);
        ZipEightByteInteger compressedSize = new ZipEightByteInteger(500000000L);
        ZipEightByteInteger relativeHeaderOffset = new ZipEightByteInteger(123456789L);
        ZipLong diskStart = new ZipLong(2L);

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField(size, compressedSize,
                relativeHeaderOffset, diskStart);

        Assert.assertEquals("Size getter returns incorrect value", size, field.getSize());
        Assert.assertEquals("Compressed size getter returns incorrect value", compressedSize, field.getCompressedSize());
        Assert.assertEquals("Relative header offset getter returns incorrect value", relativeHeaderOffset,
                field.getRelativeHeaderOffset());
        Assert.assertEquals("Disk start number getter returns incorrect value", diskStart, field.getDiskStartNumber());
    }

    @Test
    public void testGettersWithoutValues() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();

        Assert.assertNull("Size getter returns non-null value", field.getSize());
        Assert.assertNull("Compressed size getter returns non-null value", field.getCompressedSize());
        Assert.assertNull("Relative header offset getter returns non-null value", field.getRelativeHeaderOffset());
        Assert.assertNull("Disk start number getter returns non-null value", field.getDiskStartNumber());
    }

    @Test
    public void testParsingFromLocalFileData() {
        byte[] buffer = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                         0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        int offset = 0;
        int length = buffer.length;

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromLocalFileData(buffer, offset, length);

        Assert.assertEquals("Parsed size is incorrect", new ZipEightByteInteger(0x0000000000000000L), field.getSize());
        Assert.assertEquals("Parsed compressed size is incorrect", new ZipEightByteInteger(0x0000000000000000L),
                field.getCompressedSize());
    }

    @Test
    public void testParsingFromCentralDirectoryData() {
        byte[] buffer = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                         0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                         0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        int offset = 0;
        int length = buffer.length;

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.parseFromCentralDirectoryData(buffer, offset, length);

        Assert.assertEquals("Parsed size is incorrect", new ZipEightByteInteger(0x0000000000000000L), field.getSize());
        Assert.assertEquals("Parsed compressed size is incorrect", new ZipEightByteInteger(0x0000000000000000L),
                field.getCompressedSize());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        byte[] buffer = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        int offset = 0;
        int length = buffer.length;

        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        boolean hasUncompressedSize = true;
        boolean hasCompressedSize = true;
        boolean hasRelativeHeaderOffset = true;
        boolean hasDiskStart = true;
        field.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);

        Assert.assertNull("Size getter returns non-null value", field.getSize());
        Assert.assertNull("Compressed size getter returns non-null value", field.getCompressedSize());
        Assert.assertNull("Relative header offset getter returns non-null value", field.getRelativeHeaderOffset());
        Assert.assertNull("Disk start number getter returns non-null value", field.getDiskStartNumber());
    }

}