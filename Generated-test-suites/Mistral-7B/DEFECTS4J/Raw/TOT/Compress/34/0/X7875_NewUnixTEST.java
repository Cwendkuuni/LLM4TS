package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link X7875_NewUnix} class.
 */
public class X7875_NewUnixTest {

    /**
     * Test constructor method for {@link X7875_NewUnix}.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertNull(x7875.getUID());
        Assert.assertNull(x7875.getGID());
    }

    /**
     * Test getters and setters methods for {@link X7875_NewUnix}.
     */
    @Test
    public void testGettersAndSetters() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        // Test setters
        x7875.setUID(123456789L);
        x7875.setGID(234567890L);

        // Test getters
        Assert.assertEquals(123456789L, x7875.getUID());
        Assert.assertEquals(234567890L, x7875.getGID());
    }

    /**
     * Test parseFromLocalFileData method for {@link X7875_NewUnix}.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75,
            (byte) 0x03, (byte) 0x02, (byte) 0x02, (byte) 0x01,
            (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x7A, (byte) 0x2B,
            (byte) 0x1B, (byte) 0xE1, (byte) 0x02, (byte) 0x00, (byte) 0x04,
            (byte) 0x00, (byte) 0x5F, (byte) 0x4E, (byte) 0x53, (byte) 0x49,
            (byte) 0x4F, (byte) 0x53, (byte) 0x54, (byte) 0x45, (byte) 0x50};
        X7875_NewUnix x7875 = new X7875_NewUnix();

        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertEquals(new BigInteger("7468657374657354534545427A2BE102"), x7875.getUID());
        Assert.assertEquals(new BigInteger("6667666666676665454E494F535445505F4E53494F53544542"), x7875.getGID());
    }

    /**
     * Test parseFromLocalFileData with incorrect data length method for {@link X7875_NewUnix}.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataIncorrectDataLength() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75,
            (byte) 0x03, (byte) 0x02, (byte) 0x02, (byte) 0x01};
        X7875_NewUnix x7875 = new X7875_NewUnix();

        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            Assert.assertEquals("Invalid length for this extrafield.", e.getMessage());
        }
    }
}