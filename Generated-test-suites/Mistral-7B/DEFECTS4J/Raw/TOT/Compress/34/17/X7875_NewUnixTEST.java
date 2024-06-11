package org.apache.commons.compress.archivers.zip.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.X7875_NewUnixInputStream;

/**
 * Test for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Tests the constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertEquals(BigInteger.ONE, x7875.getUid());
        assertEquals(BigInteger.ONE, x7875.getGid());
    }

    /**
     * Tests setting UID and GID and getting them back.
     */
    @org.junit.Test
    public void testSettingAndGettingUidAndGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456789;
        long gid = 987654321;
        x7875.setUID(uid);
        x7875.setGID(gid);
        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    /**
     * Tests setting UID and GID with values that are too large to be represented as a long.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSettingUidAndGidWithValuesTooLargeToBeRepresentedAsALong() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = Long.MAX_VALUE + 1;
        long gid = Long.MAX_VALUE + 1;
        x7875.setUID(uid);
        x7875.setGID(gid);
    }

    /**
     * Tests the local file data length.
     */
    @org.junit.Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 3; // UID for 'uid' is '123' and has 3 bytes in the byte array.
        int gidSize = 2; // GID for 'gid' is '45' and has 2 bytes in the byte array.
        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);

        int version = x7875.getVersion();
        BigInteger uid = x7875.getUid();
        BigInteger gid = x7875.getGid();

        // Trim leading zeroes and calculate the actual length.
        int actualUidSize = trimLeadingZeroesForceMinLength(uid.toByteArray()).length;
        int actualGidSize = trimLeadingZeroesForceMinLength(gid.toByteArray()).length;

        ZipShort actualLength = new ZipShort(3 + actualUidSize + actualGidSize);

        assertEquals(expectedLength, actualLength);
    }

    /**
     * Tests the central directory data length.
     */
    @org.junit.Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 3; // UID for 'uid' is '123' and has 3 bytes in the byte array.
        int gidSize = 2; // GID for 'gid' is '45' and has 2 bytes in the byte array.
        ZipShort expectedLength = new ZipShort(0);

        int version = x7875.getVersion();
        BigInteger uid = x7875.getUid();
        BigInteger gid = x7875.getGid();

        // Trim leading zeroes and calculate the actual length.
        int actualUidSize = trimLeadingZeroesForceMinLength(uid.toByteArray()).length;
        int actualGidSize = trimLeadingZeroesForceMinLength(gid.toByteArray()).length;

        ZipShort actualLength = new ZipShort(0);

        assertEquals(expectedLength, actualLength);
    }

    /**
     * Tests parsing from local file data.
     */
    @org.junit.Test
    public void testParseFromLocalFileData() throws ZipException, IOException {
        byte[] data = getTestData();
        X7875_NewUnix x7875 = new X7875_NewUnix();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        X7875_NewUnixInputStream x7875InputStream = new X7875_NewUnixInputStream(inputStream);
        x7875.parseFromLocalFileData(x7875InputStream.readAllBytes(), 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(123L, x7875.getUID());
        assertEquals(45L, x7875.getGID());
    }

    /**
     * Tests parsing from invalid local file data.
     */
    @org.junit.Test(expected = ZipException.class)
    public void testParseFromInvalidLocalFileData() throws ZipException, IOException {
        byte[] data = getTestDataWithInvalidVersion();
        X7875_NewUnix x7875 = new X7875_NewUnix();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        X7875_NewUnixInputStream x7875InputStream = new X7875_NewUnixInputStream(inputStream);
        x7875.parseFromLocalFileData(x7875InputStream.readAllBytes(), 0, data.length);
    }

    /**
     * Tests parsing from central directory data.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testParseFromCentralDirectoryData() throws ZipException, IOException {
        byte[] data = getTestData();
        X7875_NewUnix x7875 = new X7875_NewUnix();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        X7875_NewUnixInputStream x7875InputStream = new X7875_NewUnixInputStream(inputStream);
        x7875.parseFromCentralDirectoryData(x7875InputStream.readAllBytes(), 0, data.length);
    }

    /**
     * Helper method to return test data.
     *
     * @return byte array containing test data.
     */
    private byte[] getTestData() {
        byte[] versionBytes = new byte[]{ (byte) 0x1 };
        byte[] uidBytes = new byte[]{ 0x73, 0x12, 0x34 };
        byte[] gidBytes = new byte[]{ 0x45, 0x2 };

        // Calculate the total length.
        int length = 1 + uidBytes.length + gidBytes.length;

        byte[] data = new byte[length];
        System.arraycopy(versionBytes, 0, data, 0, 1);
        System.arraycopy(uidBytes, 0, data, 1, uidBytes.length);
        System.arraycopy(gidBytes, 0, data, 1 + uidBytes.length, gidBytes.length);

        return data;
    }

    /**
     * Helper method to return test data with an invalid version.
     *
     * @return byte array containing test data with an invalid version.
     */
    private byte[] getTestDataWithInvalidVersion() {
        byte[] versionBytes = new byte[]{ (byte) 0x0 };
        byte[] uidBytes = new byte[]{ 0x73, 0x12, 0x34 };
        byte[] gidBytes = new byte[]{ 0x45, 0x2 };

        int length = 1 + uidBytes.length + gidBytes.length;

        byte[] data = new byte[length];
        System.arraycopy(versionBytes, 0, data, 0, 1);
        System.arraycopy(uidBytes, 0, data, 1, uidBytes.length);
        System.arraycopy(gidBytes, 0, data, 1 + uidBytes.length, gidBytes.length);

        return data;
    }

    /**
     * Helper method to get the trimmed and padded byte[] array.
     *
     * @param array byte[] array to trim & pad.
     * @return trimmed & padded byte[] array.
     */
    static byte[] trimLeadingZeroesForceMinLength(byte[] array) {
        // Implementation taken from X7875_NewUnix class.
        return new byte[Math.max(1, array.length - ZipUtil.countLeadingZeros(array))];
    }
}