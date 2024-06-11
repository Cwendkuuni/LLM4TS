package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.utils.ZipShort;

import java.math.BigInteger;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
    }

    @Test
    public void testSetUid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[] { 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 2, 3, 4, 5 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        BigInteger expectedUid = new BigInteger("1234567890");
        BigInteger actualUid = x7875.getUID();
        assertArrayEquals(expectedUid.toByteArray(), actualUid.toByteArray());
        BigInteger expectedGid = new BigInteger("3456789012");
        BigInteger actualGid = x7875.getGID();
        assertArrayEquals(expectedGid.toByteArray(), actualGid.toByteArray());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = new byte[] { 1, 4, 0, 0, 0 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(new BigInteger("1234567890"));
        x7875.setGID(new BigInteger("3456789012"));
        assertEquals("0x7875 Zip Extra Field: UID=1234567890 GID=3456789012", x7875.toString());
    }
}