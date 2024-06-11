import org.junit.Test;
import org.junit.Assert;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        Assert.assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1001);
        Assert.assertEquals(1001, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1002);
        Assert.assertEquals(1002, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1003);
        Assert.assertEquals(1003, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1004);
        Assert.assertEquals(1004, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1005);
        unix.setGID(1006);
        Assert.assertEquals(new ZipShort(7), unix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1007);
        unix.setGID(1008);
        Assert.assertEquals(new ZipShort(7), unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1009);
        unix.setGID(1010);
        byte[] expected = {1, 1, -23, -1, -2, 1, 1, -22, -1, -2};
        Assert.assertArrayEquals(expected, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        Assert.assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws java.util.zip.ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 1, -23, -1, -2, 1, 1, -22, -1, -2};
        unix.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, unix.getUID());
        Assert.assertEquals(1, unix.getGID());
    }

    @Test
    public void testReset() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1011);
        unix.setGID(1012);
        unix.reset();
        Assert.assertEquals(1000, unix.getUID());
        Assert.assertEquals(1000, unix.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix1.setUID(1013);
        unix1.setGID(1014);
        unix2.setUID(1013);
        unix2.setGID(1014);
        Assert.assertEquals(true, unix1.equals(unix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1015);
        unix.setGID(1016);
        Assert.assertEquals(-2042028847, unix.hashCode());
    }

}