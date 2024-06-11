package org.apache.commons.compress.archivers.zip; // Make sure the package name matches the given class

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort headerId = x7875NewUnix.getHeaderId();
        assertEquals(HEADER_ID, headerId);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123456789;
        x7875NewUnix.setUID(uid);
        long actualUid = x7875NewUnix.getUID();
        assertEquals(uid, actualUid);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123456789;
        x7875NewUnix.setUID(uid);
        BigInteger expectedUid = ZipUtil.longToBig(uid);
        BigInteger actualUid = x7875NewUnix.uid;
        assertEquals(expectedUid, actualUid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 987654321;
        x7875NewUnix.setGID(gid);
        long actualGid = x7875NewUnix.getGID();
        assertEquals(gid, actualGid);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 987654321;
        x7875NewUnix.setGID(gid);
        BigInteger expectedGid = ZipUtil.longToBig(gid);
        BigInteger actualGid = x7875NewUnix.gid;
        assertEquals(expectedGid, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        BigInteger uid = new BigInteger("123456789");
        BigInteger gid = new BigInteger("987654321");
        x7875NewUnix.uid = uid;
        x7875NewUnix.gid = gid;

        int expectedLength = 3 + uid.toByteArray().length + gid.toByteArray().length;
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        assertEquals(expectedLength, actualLength.getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{
                0x55, 0x78, 0x01, 0x01, 0x01, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38
        };

        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        BigInteger expectedUid = new BigInteger("123456789");
        BigInteger expectedGid = new BigInteger("3132333435363738");
        assertEquals(expectedUid, x7875NewUnix.uid);
        assertEquals(expectedGid, x7875NewUnix.gid);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] data = new byte[]{
                0x55, 0x78, 0x01, 0x01, 0x01, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x01, 0x01, 0x0A // Invalid length for UID
        };

        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }
}