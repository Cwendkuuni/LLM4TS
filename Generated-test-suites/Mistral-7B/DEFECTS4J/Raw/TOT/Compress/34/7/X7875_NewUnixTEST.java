import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testValidX7875_NewUnix() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);

        byte[] expectedData = new byte[10];
        expectedData[0] = (byte) 0x78;
        expectedData[1] = (byte) 0x75;
        expectedData[2] = (byte) 1; // version
        expectedData[3] = (byte) 1; // uid size
        expectedData[4] = 8; // uid byte array length
        System.arraycopy(x7875.uid.toByteArray(), 0, expectedData, 5, 8);
        expectedData[12] = (byte) 1; // gid size
        expectedData[13] = (byte) 9; // gid byte array length
        System.arraycopy(x7875.gid.toByteArray(), 0, expectedData, 14, 9);

        Assert.assertArrayEquals(expectedData, x7875.getLocalFileDataData());
        Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        Assert.assertEquals(Long.valueOf(123456789), x7875.getUID());
        Assert.assertEquals(Long.valueOf(987654321), x7875.getGID());
        Assert.assertEquals(13, x7875.getLocalFileDataLength().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNegativeUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-123456789);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNegativeGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(-987654321);
    }

    @Test
    public void testResetState() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);
        x7875.reset();
        Assert.assertEquals(Long.valueOf(1000), x7875.getUID());
        Assert.assertEquals(Long.valueOf(1000), x7875.getGID());
    }
}