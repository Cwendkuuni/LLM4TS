package org.apache.commons.compress.archivers.zip; // adjust the package name if necessary

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    // 

    @Test
    public void testConstructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertNotNull(x7875NewUnix);
        assertEquals(1, x7875NewUnix.getVersion());
        assertTrue(x7875NewUnix.getUID() == 0);
        assertTrue(x7875NewUnix.getGID() == 0);
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort headerId = x7875NewUnix.getHeaderId();
        assertEquals(0x7875, headerId.getValue().intValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        long uid = x7875NewUnix.getUID();
        assertEquals(1234567890L, uid);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        BigInteger expectedUid = BigInteger.valueOf(1234567890L);
        BigInteger actualUid = x7875NewUnix.getUid();
        assertEquals(expectedUid, actualUid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(2345678901L);
        long gid = x7875NewUnix.getGID();
        assertEquals(2345678901L, gid);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(2345678901L);
        BigInteger expectedGid = BigInteger.valueOf(2345678901L);
        BigInteger actualGid = x7875NewUnix.getGid();
        assertEquals(expectedGid, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int uidSize = 1;
        int gidSize = 1;
        x7875NewUnix.setUID(1234567890L);
        x7875NewUnix.setGID(2345678901L);
        ZipShort length = x7875NewUnix.getLocalFileDataLength();
        int expectedLength = 3 + uidSize + gidSize;
        assertEquals(expectedLength, length.getValue().intValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int uidSize = 1;
        int gidSize = 1;
        x7875NewUnix.setUID(1234567890L);
        x7875NewUnix.setGID(2345678901L);
        ZipShort length = x7875NewUnix.getCentralDirectoryLength();
        int expectedLength = 3 + uidSize + gidSize;
        assertEquals(expectedLength, length.getValue().intValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        x7875NewUnix.setGID(2345678901L);
        byte[] expectedData = getExpectedData();
        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = getExpectedData();
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(1234567890L, x7875NewUnix.getUID());
        assertEquals(2345678901L, x7875NewUnix.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = new byte[0];
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testEqualsAndHashcode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        x7875NewUnix1.setUID(1234567890L);
        x7875NewUnix1.setGID(2345678901L);
        x7875NewUnix2.setUID(1234567890L);
        x7875NewUnix2.setGID(2345678901L);
        x7875NewUnix3.setUID(1234567891L);
        x7875NewUnix3.setGID(2345678901L);
        assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
        assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
        X7875_NewUnix cloneX7875NewUnix1 = (X7875_NewUnix) x7875NewUnix1.clone();
        assertTrue(x7875NewUnix1.equals(cloneX7875NewUnix1));
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] data1 = new byte[0];
        byte[] data2 = new byte[] { 0x00 };
        byte[] data3 = new byte[] { 0x01, 0x00, 0x00 };
        byte[] data4 = new byte[] { 0x01, 0x01, 0x01, 0x01 };
        byte[] expectedData1 = new byte[0];
        byte[] expectedData2 = new byte[] { 0x01 };
        byte[] expectedData3 = new byte[] { 0x01, 0x00 };
        byte[] expectedData4 = new byte[] { 0x01, 0x01, 0x01, 0x01 };

        byte[] trimmedData1 = X7875_NewUnix.trimLeadingZeroesForceMinLength(data1);
        byte[] trimmedData2 = X7875_NewUnix.trimLeadingZeroesForceMinLength(data2);
        byte[] trimmedData3 = X7875_NewUnix.trimLeadingZeroesForceMinLength(data3);
        byte[] trimmedData4 = X7875_NewUnix.trimLeadingZeroesForceMinLength(data4);

        assertArrayEquals(expectedData1, trimmedData1);
        assertArrayEquals(expectedData2, trimmedData2);
        assertArrayEquals(expectedData3, trimmedData3);
        assertArrayEquals(expectedData4, trimmedData4);
    }

    // 

    private byte[] getExpectedData() {
        byte[] version = new byte[] { (byte) 0x01 };
        int uidSize = 1;
        byte[] uid = new byte[uidSize];
        byte[] expectedUidBytes = new byte[] { 0x3B, 0x25, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30 };
        System.arraycopy(expectedUidBytes, 0, uid, 0, uidSize);
        int gidSize = 1;
        byte[] gid = new byte[gidSize];
        byte[] expectedGidBytes = new byte[] { 0x3C, 0x35, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30 };
        System.arraycopy(expectedGidBytes, 0, gid, 0, gidSize);

        byte[] data = new byte[3 + uid.length + gid.length];
        data[0] = version[0];
        data[1] = (byte) (uidSize + 1);
        System.arraycopy(uid, 0, data, 2, uid.length);
        data[2 + uid.length] = (byte) gidSize;
        System.arraycopy(gid, 0, data, 2 + uid.length, gid.length);

        return data;
    }
}