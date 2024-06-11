package org.apache.commons.compress.archivers.zip; // make sure the package is correct

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    

    @Test
    public void testConstructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertNull(x7875NewUnix.getUID());
        Assert.assertNull(x7875NewUnix.getGID());
    }

    @Test
    public void testSetUIDAndGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123456;
        x7875NewUnix.setUID(uid);
        Assert.assertEquals(uid, x7875NewUnix.getUID());
    }

    @Test
    public void testSetGIDAndGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 7891011;
        x7875NewUnix.setGID(gid);
        Assert.assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123456);
        x7875NewUnix.setGID(7891011);

        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;
        int expectedLength = 3 + uidSize + gidSize;
        XipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        Assert.assertEquals(expectedLength, actualLength.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{0x78, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x50, 0x01, 0x00, 0x32, 0x34, 0x35, 0x36, 0x01, 0x00,
                0x78, 0x91, 0x01, 0x11};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertEquals(new BigInteger("123456"), x7875NewUnix.getUID());
        Assert.assertEquals(new BigInteger("7891011"), x7875NewUnix.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = new byte[]{0x78, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x50, 0x01, 0x00, 0x32, 0x34, 0x35, 0x36, 0x01, 0x00};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        // This line should throw ZipException as the data is missing gid value
    }

    
}