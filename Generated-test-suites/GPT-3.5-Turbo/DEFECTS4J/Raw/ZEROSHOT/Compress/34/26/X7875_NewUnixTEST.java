import org.junit.Test;
import org.junit.Assert;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        Assert.assertEquals(new ZipShort(0x7875), extraField.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456);
        Assert.assertEquals(123456, extraField.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(654321);
        Assert.assertEquals(654321, extraField.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456);
        Assert.assertEquals(123456, extraField.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(654321);
        Assert.assertEquals(654321, extraField.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456);
        extraField.setGID(654321);
        Assert.assertEquals(new ZipShort(8), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456);
        extraField.setGID(654321);
        Assert.assertEquals(new ZipShort(8), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456);
        extraField.setGID(654321);
        byte[] expectedData = new byte[]{0x01, 0x04, 0x40, 0x0F, 0x01, 0x0A, 0x04, (byte) 0xE0};
        Assert.assertArrayEquals(expectedData, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        Assert.assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, 0x04, 0x40, 0x0F, 0x01, 0x0A, 0x04, (byte) 0xE0};
        extraField.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, extraField.version);
        Assert.assertEquals(123456, extraField.getUID());
        Assert.assertEquals(654321, extraField.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, 0x04, 0x40, 0x0F, 0x01, 0x0A, 0x04, (byte) 0xE0};
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix extraField1 = new X7875_NewUnix();
        extraField1.setUID(123456);
        extraField1.setGID(654321);

        X7875_NewUnix extraField2 = new X7875_NewUnix();
        extraField2.setUID(123456);
        extraField2.setGID(654321);

        Assert.assertEquals(extraField1, extraField2);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix extraField1 = new X7875_NewUnix();
        extraField1.setUID(123456);
        extraField1.setGID(654321);

        X7875_NewUnix extraField2 = new X7875_NewUnix();
        extraField2.setUID(123456);
        extraField2.setGID(654321);

        Assert.assertEquals(extraField1.hashCode(), extraField2.hashCode());
    }
}