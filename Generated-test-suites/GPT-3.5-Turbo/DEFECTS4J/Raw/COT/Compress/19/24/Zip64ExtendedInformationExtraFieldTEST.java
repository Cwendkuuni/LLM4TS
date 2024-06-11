import org.junit.Assert;
import org.junit.Test;

public class Zip64ExtendedInformationExtraFieldTest {

    @Test
    public void testGetHeaderId() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(0x0001, field.getHeaderId().getValue());
    }

    @Test
    public void testGetLocalFileDataLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(8, field.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        Assert.assertEquals(20, field.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = field.getLocalFileDataData();
        Assert.assertEquals(8, data.length);
    }

    @Test
    public void testGetCentralDirectoryData() {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] data = field.getCentralDirectoryData();
        Assert.assertEquals(20, data.length);
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        field.parseFromLocalFileData(buffer, 0, 8);
        Assert.assertEquals(new ZipEightByteInteger(0), field.getSize());
        Assert.assertEquals(new ZipEightByteInteger(0), field.getCompressedSize());
        Assert.assertNull(field.getRelativeHeaderOffset());
        Assert.assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        byte[] buffer = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        field.parseFromCentralDirectoryData(buffer, 0, 8);
        Assert.assertEquals(new ZipEightByteInteger(0), field.getSize());
        Assert.assertEquals(new ZipEightByteInteger(0), field.getCompressedSize());
        Assert.assertNull(field.getRelativeHeaderOffset());
        Assert.assertNull(field.getDiskStartNumber());
    }

    @Test
    public void testReparseCentralDirectoryData() throws Exception {
        Zip64ExtendedInformationExtraField field = new Zip64ExtendedInformationExtraField();
        field.reparseCentralDirectoryData(true, true, false, false);
        Assert.assertNull(field.getSize());
        Assert.assertNull(field.getCompressedSize());
        Assert.assertNull(field.getRelativeHeaderOffset());
        Assert.assertNull(field.getDiskStartNumber());
    }
}