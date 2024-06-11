package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.X7875_NewUnix.HEADER_ID;
import static org.apache.commons.compress.archivers.zip.ZipUtil.reverse;
import static org.apache.commons.compress.archivers.zip.ZipUtil.unsignedIntToSignedByte;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.*;
import org.junit.Test;

/**
 * Test cases for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnix() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(HEADER_ID, x7875.getHeaderId());
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 12345;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 67890;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        BigInteger uid = new BigInteger("111111111111111111111111111111111");
        BigInteger gid = new BigInteger("222222222222222222222222222222222");

        x7875.setUID(uid.longValue());
        x7875.setGID(gid.longValue());

        int uidSize = uid.toByteArray().length;
        int gidSize = gid.toByteArray().length;

        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(new ZipShort((short) (3 + uidSize + gidSize)), length);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix expected = new X7875_NewUnix();
        long uid = 12345;
        long gid = 67890;
        byte[] data = new byte[10];
        data[0] = unsignedIntToSignedByte(X7875_NewUnix.VERSION);
        data[1] = unsignedIntToSignedByte((short) (uid.toByteArray().length + 1));
        System.arraycopy(uid.toByteArray(), 0, data, 2, uid.toByteArray().length);
        data[2 + uid.toByteArray().length] = unsignedIntToSignedByte((short) (gid.toByteArray().length + 1));
        System.arraycopy(gid.toByteArray(), 0, data, 3 + uid.toByteArray().length, gid.toByteArray().length);

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 0x75, 0xFF};
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array1 = new byte[0];
        byte[] array2 = new byte[0];
        byte[] array3 = new byte[]{"0".getBytes()};
        byte[] array4 = new byte[]{1, 0};
        byte[] array5 = new byte[]{"123".getBytes()};
        byte[] array6 = new byte[]{"012345".getBytes()};
        byte[] array7 = new byte[]{"0012345".getBytes()};

        byte[] result1 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array1);
        byte[] result2 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array2);
        byte[] result3 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array3);
        byte[] result4 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array4);
        byte[] result5 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array5);
        byte[] result6 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array6);
        byte[] result7 = X7875_NewUnix.trimLeadingZeroesForceMinLength(array7);

        assertEquals(new byte[0], result1);
        assertEquals(new byte[0], result2);
        assertEquals(new byte[0], result3);
        assertEquals(new byte[]{1}, result4);
        assertEquals(new byte[]{1, 2, 3}, result5);
        assertEquals(new byte[]{1, 2, 3, 4, 5}, result6);
        assertEquals(new byte[]{1, 2, 3, 4, 5}, result7);
    }
}