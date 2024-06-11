package org.apache.commons.compress.archivers.zip; // Make sure the test file is in the same package as the class under test.

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.ZipException;

import static org.apache.commons.compress.archivers.zip.ZipUtil.reverse;
import static org.apache.commons.compress.archivers.zip.ZipUtil.signedByteToUnsignedInt;
import static org.apache.commons.compress.archivers.zip.ZipUtil.unsignedIntToSignedByte;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        Assert.assertEquals(new ZipShort(0x7875), headerId);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = x7875.getUID();
        Assert.assertEquals(1000L, uid); // Default value

        x7875.setUID(2000L);
        uid = x7875.getUID();
        Assert.assertEquals(2000L, uid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = x7875.getGID();
        Assert.assertEquals(1000L, gid); // Default value

        x7875.setGID(3000L);
        gid = x7875.getGID();
        Assert.assertEquals(3000L, gid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        // Test default values
        ZipShort length = x7875.getLocalFileDataLength();
        Assert.assertEquals(new ZipShort(3), length);

        BigInteger uid = BigInteger.valueOf(2000);
        BigInteger gid = BigInteger.valueOf(3000);
        x7875.setUID(uid);
        x7875.setGID(gid);

        // Test non-default values
        length = x7875.getLocalFileDataLength();
        int uidSize = uid.toByteArray().length;
        int gidSize = gid.toByteArray().length;
        Assert.assertEquals(new ZipShort(3 + uidSize + gidSize), length);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, // version
                (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, // uid size
                (byte) 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, // uid
                (byte) 0x10, (byte) 0x11, (byte) 0x12, // gid size
                (byte) 0x13, 0x14, 0x15, 0x16, 0x17 // gid
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        BigInteger expectedUid = new BigInteger(reverse(new byte[]{(byte) 0x0f, (byte) 0x0e, (byte) 0x0d, (byte) 0x0c, (byte) 0x0b, (byte) 0x0a, (byte) 0x09, (byte) 0x08}));
        BigInteger expectedGid = new BigInteger(reverse(new byte[]{(byte) 0x17, (byte) 0x16, (byte) 0x15, (byte) 0x14, (byte) 0x13}));
        Assert.assertEquals(expectedUid, x7875.uid);
        Assert.assertEquals(expectedGid, x7875.gid);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidLength() throws ZipException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, // version
                (byte) 0x04, (byte) 0x05, (byte) 0x06, // uid size
                (byte) 0x08 // invalid length for gid
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidVersion() throws ZipException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, // version=2
                (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, // uid size
                (byte) 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f // uid
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        String result = x7875.toString();
        Assert.assertEquals("0x7875 Zip Extra Field: UID=1 GID=1", result);

        BigInteger uid = BigInteger.valueOf(2000);
        BigInteger gid = BigInteger.valueOf(3000);
        x7875.setUID(uid);
        x7875.setGID(gid);
        result = x7875.toString();
        String expected = "0x7875 Zip Extra Field: UID=" + uid + " GID=" + gid;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix original = new X7875_NewUnix();
        X7875_NewUnix clone = (X7875_NewUnix) original.clone();

        Assert.assertNotSame(original, clone);
        Assert.assertEquals(original.uid, clone.uid);
        Assert.assertEquals(original.gid, clone.gid);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        X7875_NewUnix x78752 = new X7875_NewUnix();
        X7875_NewUnix x78753 = x78751;

        Assert.assertEquals(x78751, x78751);
        Assert.assertEquals(x78751, x78753);
        Assert.assertNotEquals(x78751, x78752);

        BigInteger uid = BigInteger.valueOf(2000);
        BigInteger gid = BigInteger.valueOf(3000);
        x78751.setUID(uid);
        x78751.setGID(gid);
        x78752.setUID(uid);
        x78752.setGID(gid);

        Assert.assertEquals(x78751, x78752);
        Assert.assertNotEquals(x78751, new Object());
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        X7875_NewUnix x78752 = new X7875_NewUnix();
        X7875_NewUnix x78753 = x78751;

        int hashCode1 = x78751.hashCode();
        int hashCode2 = x78752.hashCode();
        int hashCode3 = x78753.hashCode();

        Assert.assertEquals(hashCode1, hashCode3);
        Assert.assertNotEquals(hashCode1, hashCode2);

        BigInteger uid = BigInteger.valueOf(2000);
        BigInteger gid = BigInteger.valueOf(3000);
        x78751.setUID(uid);
        x78751.setGID(gid);
        x78752.setUID(uid);
        x78752.setGID(gid);

        Assert.assertEquals(x78751.hashCode(), x78752.hashCode());
    }
}