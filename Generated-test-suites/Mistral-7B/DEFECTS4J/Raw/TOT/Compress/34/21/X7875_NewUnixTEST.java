package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test cases for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertEquals(BigInteger.ONE, x7875.getUid());
        Assert.assertEquals(BigInteger.ONE, x7875.getGid());
    }

    /**
     * Test setting and getting UID.
     */
    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long uid = 1234567890L;
        x7875.setUID(uid);
        Assert.assertEquals(uid, x7875.getUID());
    }

    /**
     * Test setting and getting GID.
     */
    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long gid = 9876543210L;
        x7875.setGID(gid);
        Assert.assertEquals(gid, x7875.getGID());
    }

    /**
     * Test getLocalFileDataLength method.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        // Test with default UID and GID
        int length = x7875.getLocalFileDataLength().getValue();
        Assert.assertEquals(10, length);

        // Test with custom UID and GID
        x7875.setUID(1234567890L);
        x7875.setGID(9876543210L);
        length = x7875.getLocalFileDataLength().getValue();
        Assert.assertEquals(16, length);
    }

    /**
     * Test getCentralDirectoryLength method.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int length = x7875.getCentralDirectoryLength().getValue();
        Assert.assertEquals(0, length);
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, // Header-ID
                0x03, 0x04, // Length
                0x01, 0x01, // Version
                0x03, 0x04, // UID size
                0x00, 0x01, 0x02, 0x03, // UID (123)
                0x03, 0x04, // GID size
                0x00, 0x01, 0x02, 0x03, // GID (456)
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertEquals(new BigInteger("123"), x7875.getUid());
        Assert.assertEquals(new BigInteger("456"), x7875.getGid());
    }

    /**
     * Test parsing from central directory data (should not do anything).
     */
    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[0];
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromCentralDirectoryData(data, 0, data.length);
    }
}