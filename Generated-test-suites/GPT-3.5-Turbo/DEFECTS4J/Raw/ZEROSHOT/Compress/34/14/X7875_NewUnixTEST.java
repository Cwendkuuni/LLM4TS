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
        x7875_NewUnix.setUID(12345);
        Assert.assertEquals(12345, x7875_NewUnix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(54321);
        Assert.assertEquals(54321, x7875_NewUnix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(12345);
        Assert.assertEquals(12345, x7875_NewUnix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(54321);
        Assert.assertEquals(54321, x7875_NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(3);
        Assert.assertEquals(expected, x7875_NewUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(3);
        Assert.assertEquals(expected, x7875_NewUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] expected = {1, 1, 0, 0, 0};
        Assert.assertArrayEquals(expected, x7875_NewUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] expected = new byte[0];
        Assert.assertArrayEquals(expected, x7875_NewUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws java.util.zip.ZipException {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = {1, 1, 0, 0, 0};
        x7875_NewUnix.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, x7875_NewUnix.version);
        Assert.assertEquals(1, x7875_NewUnix.getUID());
        Assert.assertEquals(0, x7875_NewUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws java.util.zip.ZipException {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] buffer = new byte[10];
        x7875_NewUnix.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        // No-op method, nothing to test
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        String expected = "0x7875 Zip Extra Field: UID=1000 GID=1000";
        Assert.assertEquals(expected, x7875_NewUnix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875_NewUnix2 = new X7875_NewUnix();
        Assert.assertTrue(x7875_NewUnix1.equals(x7875_NewUnix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875_NewUnix2 = new X7875_NewUnix();
        Assert.assertEquals(x7875_NewUnix1.hashCode(), x7875_NewUnix2.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 1, 2, 3};
        byte[] expected = {1, 2, 3};
        Assert.assertArrayEquals(expected, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }

}