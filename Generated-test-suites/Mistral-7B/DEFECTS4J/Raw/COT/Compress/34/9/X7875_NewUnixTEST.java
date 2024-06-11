package org.apache.commons.compress.archivers.zip; // Import the package of the class under test

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.ZipException;

import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    /**
     * Test getHeaderId method.
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort expected = new ZipShort(0x7875);
        ZipShort actual = x7875.getHeaderId();
        assertEquals(expected, actual);
    }

    /**
     * Test getUID method.
     */
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long expected = 0;
        long actual = x7875.getUID();
        assertEquals(expected, actual);

        x7875.setUID(12345);
        expected = 12345;
        actual = x7875.getUID();
        assertEquals(expected, actual);
    }

    /**
     * Test setUID method with valid UID.
     */
    @Test
    public void testSetUIDValid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 12345;

        x7875.setUID(uid);
        BigInteger actualUid = x7875.getUID();
        BigInteger expectedUid = new BigInteger(Long.toUnsignedLong(uid));
        assertEquals(expectedUid, actualUid);
    }

    /**
     * Test setUID method with negative UID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetUIDNegative() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = -12345;
        x7875.setUID(uid);
    }

    /**
     * Test getGID method.
     */
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long expected = 0;
        long actual = x7875.getGID();
        assertEquals(expected, actual);

        x7875.setGID(67890);
        expected = 67890;
        actual = x7875.getGID();
        assertEquals(expected, actual);
    }

    /**
     * Test setGID method with valid GID.
     */
    @Test
    public void testSetGIDValid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 67890;

        x7875.setGID(gid);
        BigInteger actualGid = x7875.getGID();
        BigInteger expectedGid = new BigInteger(Long.toUnsignedLong(gid));
        assertEquals(expectedGid, actualGid);
    }

    /**
     * Test setGID method with negative GID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGIDNegative() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = -67890;
        x7875.setGID(gid);
    }

    /**
     * Test parseFromLocalFileData method.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[] { 1, (byte) 0x03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 67, 0, 0, 0, 3, 0, 0, 0, 20, 0x75, 0x78, 0 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("480000000000000000001000"), x7875.getUID());
        assertEquals(new BigInteger("6700000000000000002"), x7875.getGID());
    }

    /**
     * Test parseFromLocalFileData method with invalid data length.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidDataLength() throws ZipException {
        byte[] data = new byte[] { 1, (byte) 0x03, 0, 0 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test parseFromLocalFileData method with invalid version.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidVersion() throws ZipException {
        byte[] data = new byte[] { 0, (byte) 0x03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 67, 0, 0, 0, 3, 0, 0, 0, 20, 0x75, 0x78, 0 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test parseFromLocalFileData method with invalid UID.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidUID() throws ZipException {
        byte[] data = new byte[] { 1, (byte) 0x03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 2, 0, 0, 1, 0, 0, 0, 67, 0, 0, 0, 3, 0, 0, 0, 20, 0x75, 0x78, 0 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test parseFromLocalFileData method with invalid GID.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidGID() throws ZipException {
        byte[] data = new byte[] { 1, (byte) 0x03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 67, 0, 0, 0, 3, 0, 0, 0, 20, 0x75, 0x78, 0xff };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        X7875_NewUnix x7875_3 = new X7875_NewUnix();

        x7875_1.setUID(12345);
        x7875_1.setGID(67890);

        x7875_2.setUID(12345);
        x7875_2.setGID(67890);

        x7875_3.setUID(56789);
        x7875_3.setGID(67890);

        assertTrue(x7875_1.equals(x7875_2));
        assertFalse(x7875_1.equals(x7875_3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        X7875_NewUnix x7875_3 = new X7875_NewUnix();

        x7875_1.setUID(12345);
        x7875_1.setGID(67890);

        x7875_2.setUID(12345);
        x7875_2.setGID(67890);

        x7875_3.setUID(56789);
        x7875_3.setGID(67890);

        int x7875_1_hashCode = x7875_1.hashCode();
        int x7875_2_hashCode = x7875_2.hashCode();
        int x7875_3_hashCode = x7875_3.hashCode();

        // Different UID's should result in different hash codes.
        assertFalse(x7875_1_hashCode == x7875_3_hashCode);

        // Different GID's should also result in different hash codes.
        assertFalse(x7875_1_hashCode == x7875_3_hashCode);

        // Same UID and GID should result in the same hash code.
        assertTrue(x7875_1_hashCode == x7875_2_hashCode);
    }
}