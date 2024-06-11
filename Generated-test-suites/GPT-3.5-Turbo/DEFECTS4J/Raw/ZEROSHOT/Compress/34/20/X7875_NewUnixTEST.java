import org.junit.Test;
import org.junit.Assert;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        Assert.assertEquals(new ZipShort(0x7875), x7875_NewUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(1000L);
        Assert.assertEquals(1000L, x7875_NewUnix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(1000L);
        Assert.assertEquals(1000L, x7875_NewUnix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(1000L);
        Assert.assertEquals(1000L, x7875_NewUnix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(1000L);
        Assert.assertEquals(1000L, x7875_NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(1000L);
        x7875_NewUnix.setGID(1000L);
        Assert.assertEquals(new ZipShort(21), x7875_NewUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(1000L);
        x7875_NewUnix.setGID(1000L);
        Assert.assertEquals(new ZipShort(21), x7875_NewUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(1000L);
        x7875_NewUnix.setGID(1000L);
        byte[] expectedData = {(byte) 1, (byte) 1, (byte) 232, (byte) 3, (byte) 232, (byte) 3};
        Assert.assertArrayEquals(expectedData, x7875_NewUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        Assert.assertArrayEquals(new byte[0], x7875_NewUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = {(byte) 1, (byte) 1, (byte) 232, (byte) 3, (byte) 232, (byte) 3};
        x7875_NewUnix.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, x7875_NewUnix.getUID());
        Assert.assertEquals(1, x7875_NewUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = {(byte) 1, (byte) 1, (byte) 232, (byte) 3, (byte) 232, (byte) 3};
        x7875_NewUnix.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(1000L);
        x7875_NewUnix.setGID(1000L);
        Assert.assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x7875_NewUnix.toString());
    }
}