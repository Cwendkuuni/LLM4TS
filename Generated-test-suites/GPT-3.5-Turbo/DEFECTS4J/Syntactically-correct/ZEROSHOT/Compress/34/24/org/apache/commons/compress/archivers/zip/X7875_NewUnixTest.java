import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;
import java.util.zip.ZipException;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        Assert.assertEquals(0x7875, unix.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        Assert.assertEquals(1000, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        Assert.assertEquals(1000, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        Assert.assertEquals(1000, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        Assert.assertEquals(1000, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals(6, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals(6, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        byte[] expectedData = new byte[]{1, 1, 0xE8, 1, 0xE8};
        Assert.assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        Assert.assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = new byte[]{1, 1, 0xE8, 1, 0xE8};
        unix.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, unix.getUID());
        Assert.assertEquals(1, unix.getGID());

    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = new byte[]{};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", unix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(1000);
        unix2.setUID(1000);
        unix2.setGID(1000);
        Assert.assertEquals(unix1, unix2);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals(208044, unix.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = new byte[]{0, 0, 0, 0};
        byte[] expectedArray = new byte[]{0};
        Assert.assertArrayEquals(expectedArray, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}