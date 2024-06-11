import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewUnixWithValidValues() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(123456789L);
        newUnix.setGID(987654321L);

        Assert.assertEquals(new ZipShort((short) 0x7875), newUnix.getHeaderId());
        Assert.assertEquals(Long.valueOf(1), newUnix.getVersion());
        Assert.assertEquals(Long.valueOf(123456789), newUnix.getUID());
        Assert.assertEquals(Long.valueOf(987654321), newUnix.getGID());
        Assert.assertEquals(new ZipShort(newShort((short) (3 + 9 + 9))), newUnix.getLocalFileDataLength());
        Assert.assertArrayEquals(newUnix.getLocalFileDataData(), getExpectedBytesForValidValues());
        newUnix.parseFromLocalFileData(getExpectedBytesForValidValues(), 0, getExpectedBytesForValidValues().length);
        Assert.assertEquals(newUnix.getVersion(), newUnix.getVersion());
        Assert.assertEquals(newUnix.getUID(), newUnix.getUID());
        Assert.assertEquals(newUnix.getGID(), newUnix.getGID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewUnixWithNegativeUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(-123456789L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewUnixWithNegativeGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(-987654321L);
    }

    @Test
    public void testNewUnixWithZeroUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(0L);

        Assert.assertEquals(new ZipShort((short) 0x7875), newUnix.getHeaderId());
        Assert.assertEquals(Long.valueOf(1), newUnix.getVersion());
        Assert.assertEquals(Long.valueOf(0), newUnix.getUID());
        Assert.assertEquals(Long.valueOf(0), newUnix.getGID());
        Assert.assertEquals(new ZipShort(newShort((short) (3))), newUnix.getLocalFileDataLength());
        Assert.assertArrayEquals(newUnix.getLocalFileDataData(), getExpectedBytesForZeroUID());
        newUnix.parseFromLocalFileData(getExpectedBytesForZeroUID(), 0, getExpectedBytesForZeroUID().length);
        Assert.assertEquals(newUnix.getVersion(), newUnix.getVersion());
        Assert.assertEquals(newUnix.getUID(), newUnix.getUID());
        Assert.assertEquals(newUnix.getGID(), newUnix.getGID());
    }

    @Test
    public void testNewUnixWithZeroGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(0L);

        Assert.assertEquals(new ZipShort((short) 0x7875), newUnix.getHeaderId());
        Assert.assertEquals(Long.valueOf(1), newUnix.getVersion());
        Assert.assertEquals(Long.valueOf(123456789), newUnix.getUID());
        Assert.assertEquals(Long.valueOf(0), newUnix.getGID());
        Assert.assertEquals(new ZipShort(newShort((short) (3 + 9))), newUnix.getLocalFileDataLength());
        Assert.assertArrayEquals(newUnix.getLocalFileDataData(), getExpectedBytesForZeroGID());
        newUnix.parseFromLocalFileData(getExpectedBytesForZeroGID(), 0, getExpectedBytesForZeroGID().length);
        Assert.assertEquals(newUnix.getVersion(), newUnix.getVersion());
        Assert.assertEquals(newUnix.getUID(), newUnix.getUID());
        Assert.assertEquals(newUnix.getGID(), newUnix.getGID());
    }

    private byte[] getExpectedBytesForValidValues() {
        byte[] expectedBytes = new byte[17];
        expectedBytes[0] = (byte) 0x55;
        expectedBytes[1] = (byte) 0x78;
        expectedBytes[2] = (byte) 0x75;
        expectedBytes[3] = (byte) 0x00;
        expectedBytes[4] = (byte) 0x03;
        expectedBytes[5] = (byte) 0x1A;
        expectedBytes[6] = (byte) 0x00;
        expectedBytes[7] = (byte) 0x09;
        expectedBytes[8] = (byte) 0x49;
        expectedBytes[9] = (byte) 0x2A;
        expectedBytes[10] = (byte) 0x00;
        expectedBytes[11] = (byte) 0x09;
        expectedBytes[12] = (byte) 0x2A;
        expectedBytes[13] = (byte) 0x00;
        expectedBytes[14] = (byte) 0x49;
        expectedBytes[15] = (byte) 0x43;
        expectedBytes[16] = (byte) 0x12;
        expectedBytes[17] = (byte) 0x34;
        return expectedBytes;
    }

    private byte[] getExpectedBytesForZeroUID() {
        byte[] expectedBytes = new byte[15];
        expectedBytes[0] = (byte) 0x55;
        expectedBytes[1] = (byte) 0x78;
        expectedBytes[2] = (byte) 0x75;
        expectedBytes[3] = (byte) 0x00;
        expectedBytes[4] = (byte) 0x03;
        expectedBytes[5] = (byte) 0x01;
        expectedBytes[6] = (byte) 0x00;
        expectedBytes[7] = (byte) 0x01;
        expectedBytes[8] = (byte) 0x00;
        expectedBytes[9] = (byte) 0x00;
        expectedBytes[10] = (byte) 0x00;
        expectedBytes[11] = (byte) 0x00;
        expectedBytes[12] = (byte) 0x00;
        expectedBytes[13] = (byte) 0x00;
        expectedBytes[14] = (byte) 0x43;
        return expectedBytes;
    }

    private byte[] getExpectedBytesForZeroGID() {
        byte[] expectedBytes = new byte[15];
        expectedBytes[0] = (byte) 0x55;
        expectedBytes[1] = (byte) 0x78;
        expectedBytes[2] = (byte) 0x75;
        expectedBytes[3] = (byte) 0x00;
        expectedBytes[4] = (byte) 0x03;
        expectedBytes[5] = (byte) 0x01;
        expectedBytes[6] = (byte) 0x01;
        expectedBytes[7] = (byte) 0x00;
        expectedBytes[8] = (byte) 0x01;
        expectedBytes[9] = (byte) 0x00;
        expectedBytes[10] = (byte) 0x00;
        expectedBytes[11] = (byte) 0x00;
        expectedBytes[12] = (byte) 0x00;
        expectedBytes[13] = (byte) 0x00;
        expectedBytes[14] = (byte) 0x43;
        return expectedBytes;
    }
}