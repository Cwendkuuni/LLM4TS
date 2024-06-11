package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.ZipException;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNotNull(x7875);
        assertEquals(1, x7875.getVersion());
        assertEquals(BigInteger.ZERO, x7875.getUid());
        assertEquals(BigInteger.ZERO, x7875.getGid());
        assertEquals(getLengthOfEmptyX7875NewUnix(), x7875.getLocalFileDataLength());
    }

    /**
     * Test getHeaderId method.
     */
    @org.junit.Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(new ZipShort(0x7875), headerId);
    }

    /**
     * Test getUID method.
     */
    @org.junit.Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    /**
     * Test setUID method.
     */
    @org.junit.Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    /**
     * Test getGID method.
     */
    @org.junit.Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 123456;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    /**
     * Test setGID method.
     */
    @org.junit.Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 123456;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    /**
     * Test getLocalFileDataLength method.
     */
    @org.junit.Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 2;
        int gidSize = 3;
        x7875.setUid(new BigInteger(1, new byte[]{0x01, 0x02}));
        x7875.setGid(new BigInteger(1, new byte[]{0x03, 0x04, 0x05}));
        assertEquals(new ZipShort((short) (3 + uidSize + gidSize)), x7875.getLocalFileDataLength());
    }

    /**
     * Test parseFromLocalFileData method.
     */
    @org.junit.Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = getTestData();
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger(1, new byte[]{0x01, 0x02}), x7875.getUid());
        assertEquals(new BigInteger(1, new byte[]{0x03, 0x04, 0x05}), x7875.getGid());
    }

    /**
     * Test parseFromCentralDirectoryData method.
     */
    @org.junit.Test
    public void testParseFromCentralDirectoryData() {
        // Empty test case since parseFromCentralDirectoryData does nothing.
    }

    /**
     * Test equals method.
     */
    @org.junit.Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        X7875_NewUnix x7875_3 = new X7875_NewUnix();
        x7875_1.setUID(123456);
        x7875_1.setGID(123456);
        x7875_2.setUID(123456);
        x7875_2.setGID(123456);
        x7875_3.setUID(123457);
        x7875_3.setGID(123457);
        assertTrue(x7875_1.equals(x7875_2));
        assertFalse(x7875_1.equals(x7875_3));
    }

    /**
     * Test hashCode method.
     */
    @org.junit.Test
    public void testHashCode() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        X7875_NewUnix x7875_3 = new X7875_NewUnix();
        x7875_1.setUID(123456);
        x7875_1.setGID(123456);
        x7875_2.setUID(123456);
        x7875_2.setGID(123456);
        x7875_3.setUID(123457);
        x7875_3.setGID(123457);
        int expectedHashCode_1 = -306721329 + 123456 * 16 + 123456;
        int expectedHashCode_2 = expectedHashCode_1;
        int expectedHashCode_3 = -306721329 + 123457 * 16 + 123457;
        assertEquals(expectedHashCode_1, x7875_1.hashCode());
        assertEquals(expectedHashCode_2, x7875_2.hashCode());
        assertEquals(expectedHashCode_3, x7875_3.hashCode());
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        X7875_NewUnix clonedX7875 = (X7875_NewUnix) x7875.clone();
        assertEquals(x7875, clonedX7875);
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=123456 GID=123456", x7875.toString());
    }

    /**
     * Test trimLeadingZeroesForceMinLength method.
     */
    @org.junit.Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = new byte[]{0, 0, 0, 1};
        byte[] trimmedArray = X7875_NewUnix.trimLeadingZeroesForceMinLength(array);
        assertEquals(new byte[]{1}, trimmedArray);
    }

    /**
     * Test getLengthOfEmptyX7875NewUnix method.
     */
    private int getLengthOfEmptyX7875NewUnix() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        return x7875.getLocalFileDataLength().getValue();
    }

    /**
     * Test data for parseFromLocalFileData method.
     */
    private byte[] getTestData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] uidBytes = new byte[]{0x01, 0x02};
        byte[] gidBytes = new byte[]{0x03, 0x04, 0x05};
        byte[] data = new byte[3 + uidBytes.length + gidBytes.length];

        data[0] = unsignedIntToSignedByte(x7875.getVersion());
        data[1] = unsignedIntToSignedByte(uidBytes.length);
        System.arraycopy(uidBytes, 0, data, 2, uidBytes.length);
        data[2 + uidBytes.length] = unsignedIntToSignedByte(gidBytes.length);
        System.arraycopy(gidBytes, 0, data, 3 + uidBytes.length, gidBytes.length);

        return data;
    }
}