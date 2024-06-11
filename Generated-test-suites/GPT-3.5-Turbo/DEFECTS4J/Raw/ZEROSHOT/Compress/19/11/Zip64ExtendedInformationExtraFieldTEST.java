import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort(0x0001), field.getHeaderId());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort(16), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(new ZipShort(24), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];
        Assert.assertArrayEquals(data, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[24];
        Assert.assertArrayEquals(data, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];
        field.parseFromLocalFileData(data, 0, 16);
        Assert.assertNull(field.getSize());
        Assert.assertNull(field.getCompressedSize());
        Assert.assertNull(field.getRelativeHeaderOffset());
        Assert.assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[24];
        field.parseFromCentralDirectoryData(data, 0, 24);
        Assert.assertNull(field.getSize());
        Assert.assertNull(field.getCompressedSize());
        Assert.assertNull(field.getRelativeHeaderOffset());
        Assert.assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[24];
        field.parseFromCentralDirectoryData(data, 0, 24);
        field.reparseCentralDirectoryData(true, true, true, true);
        Assert.assertNull(field.getSize());
        Assert.assertNull(field.getCompressedSize());
        Assert.assertNull(field.getRelativeHeaderOffset());
        Assert.assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testGetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertNull(field.getSize());
    }

    @Test
    public void testSetSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setSize(new ZipEightByteInteger(123));
        Assert.assertEquals(new ZipEightByteInteger(123), field.getSize());
    }

    @Test
    public void testGetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertNull(field.getCompressedSize());
    }

    @Test
    public void testSetCompressedSize() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setCompressedSize(new ZipEightByteInteger(123));
        Assert.assertEquals(new ZipEightByteInteger(123), field.getCompressedSize());
    }

    @Test
    public void testGetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertNull(field.getRelativeHeaderOffset());
    }

    @Test
    public void testSetRelativeHeaderOffset() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setRelativeHeaderOffset(new ZipEightByteInteger(456));
        Assert.assertEquals(new ZipEightByteInteger(456), field.getRelativeHeaderOffset());
    }

    @Test
    public void testGetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testSetDiskStartNumber() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.setDiskStartNumber(new ZipLong(789));
        Assert.assertEquals(new ZipLong(789), field.getDiskStartNumber());
    }

    @Test
    public void testAddSizes() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = new byte[16];
        Assert.assertEquals(0, field.addSizes(data));
    }

}