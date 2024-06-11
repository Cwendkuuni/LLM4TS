package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals(X7875_NewUnix.HEADER_ID, x7875.getHeaderId());
        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertEquals(BigInteger.ZERO, x7875.getUID());
        Assert.assertEquals(BigInteger.ZERO, x7875.getGID());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        Assert.assertEquals(1234567890L, x7875.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(2345678901L);
        Assert.assertEquals(2345678901L, x7875.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(2345678901L);

        int length = x7875.getLocalFileDataLength().getValue();
        Assert.assertEquals(1 + 1 + 8 + 8, length); // version=1, uidsize=1, gidsize=1
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataNullData() {
        byte[] data = null;
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, 0);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidDataLength() {
        byte[] data = new byte[2];
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, 2);
    }

    @Test
    public void testParseFromLocalFileDataValidData() {
        byte[] data = new byte[]{
                0x55, 0x78, 0x01, 0x01, (byte) 0x78, 0x75, // version=1, uidsize=1, tag=ux
                0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, // uid=12
                0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x34, // gid=34
                0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x56, // version=1, uidsize=1, tag=Ux
                0x00, 0x00 // length=0
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertEquals(BigInteger.valueOf(12), x7875.getUID());
        Assert.assertEquals(BigInteger.valueOf(34), x7875.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(2345678901L);
        String expected = "0x7875 Zip Extra Field: UID=1234567890 GID=2345678901";
        Assert.assertEquals(expected, x7875.toString());
    }

    @Test
    public void testClone() {
        X7875_NewUnix original = new X7875_NewUnix();
        original.setUID(1234567890L);
        original.setGID(2345678901L);
        X7875_NewUnix clone = (X7875_NewUnix) original.clone();
        Assert.assertNotEquals(original, clone);
        Assert.assertEquals(original.getVersion(), clone.getVersion());
        Assert.assertEquals(original.getUID(), clone.getUID());
        Assert.assertEquals(original.getGID(), clone.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        X7875_NewUnix x78752 = new X7875_NewUnix();
        X7875_NewUnix x78753 = new X7875_NewUnix();

        x78751.setUID(1234567890L);
        x78751.setGID(2345678901L);

        x78752.setUID(1234567890L);
        x78752.setGID(2345678901L);

        x78753.setUID(1234567891L);
        x78753.setGID(2345678901L);

        Assert.assertTrue(x78751.equals(x78752));
        Assert.assertFalse(x78751.equals(x78753));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        X7875_NewUnix x78752 = new X7875_NewUnix();
        X7875_NewUnix x78753 = new X7875_NewUnix();

        x78751.setUID(1234567890L);
        x78751.setGID(2345678901L);

        x78752.setUID(1234567890L);
        x78752.setGID(2345678901L);

        x78753.setUID(1234567891L);
        x78753.setGID(2345678901L);

        Assert.assertEquals(x78751.hashCode(), x78752.hashCode());
        Assert.assertNotEquals(x78751.hashCode(), x78753.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] input = new byte[]{0, 0, 0, 1, 0, 0, 0, 0, 0};
        byte[] output = X7875_NewUnix.trimLeadingZeroesForceMinLength(input);
        byte[] expectedOutput = new byte[]{1, 0, 0, 0, 0, 0, 0, 0};
        Assert.assertArrayEquals(expectedOutput, output);
    }
}