package org.apache.commons.compress.archivers.zip; // adjust the package name if necessary

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

/**
 * JUnit 4 test cases for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnix() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
        assertEquals(1, x7875NewUnix.getVersion());
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
        assertEquals(new ZipShort(0), x7875NewUnix.getLocalFileDataLength());
        assertEquals(new ZipShort(0), x7875NewUnix.getCentralDirectoryLength());
        assertArrayEquals(new byte[0], x7875NewUnix.getLocalFileDataData());
        assertArrayEquals(new byte[0], x7875NewUnix.getCentralDirectoryData());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        assertEquals(12345L, x7875NewUnix.getUID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUID_negativeValue() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(-1);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(54321);
        assertEquals(54321L, x7875NewUnix.getGID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGID_negativeValue() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(-1);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123);
        x7875NewUnix.setGID(456);
        byte[] uidBytes = x7875NewUnix.uid.toByteArray();
        byte[] gidBytes = x7875NewUnix.gid.toByteArray();
        int uidSize = uidBytes.length;
        int gidSize = gidBytes.length;
        assertEquals(new ZipShort(3 + uidSize + gidSize), x7875NewUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123);
        x7875NewUnix.setGID(456);
        assertEquals(new ZipShort(0), x7875NewUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123);
        x7875NewUnix.setGID(456);
        byte[] uidBytes = x7875NewUnix.uid.toByteArray();
        byte[] gidBytes = x7875NewUnix.gid.toByteArray();
        int uidSize = uidBytes.length;
        int gidSize = gidBytes.length;
        byte[] expectedData = new byte[3 + uidSize + gidSize];
        expectedData[0] = (byte) 0x78;
        expectedData[1] = (byte) 0x75;
        expectedData[2] = (byte) 0x01; // version
        expectedData[3] = (byte) uidSize;
        System.arraycopy(uidBytes, 0, expectedData, 4, uidSize);
        expectedData[4 + uidSize] = (byte) gidSize;
        System.arraycopy(gidBytes, 0, expectedData, 5 + uidSize, gidSize);
        assertArrayEquals(expectedData, x7875NewUnix.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = { (byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x45, (byte) 0x67, (byte) 0x01 };
        X7875_NewUnix expectedX7875NewUnix = new X7875_NewUnix();
        expectedX7875NewUnix.setVersion(1);
        expectedX7875NewUnix.setUID(new BigInteger("12345"));
        expectedX7875NewUnix.setGID(new BigInteger("4567"));
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(expectedX7875NewUnix.getVersion(), x7875NewUnix.getVersion());
        assertEquals(expectedX7875NewUnix.getUID(), x7875NewUnix.getUID());
        assertEquals(expectedX7875NewUnix.getGID(), x7875NewUnix.getGID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileData_invalidDataLength() throws ZipException {
        byte[] data = new byte[5];
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileData_invalidVersion() throws ZipException {
        byte[] data = { (byte) 0x78, (byte) 0x75, (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x45, (byte) 0x67, (byte) 0x01 };
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(4567);
        x7875NewUnix.reset();
        assertEquals(1, x7875NewUnix.getVersion());
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(new BigInteger("12345"));
        x7875NewUnix.setGID(new BigInteger("4567"));
        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=4567", x7875NewUnix.toString());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix originalX7875NewUnix = new X7875_NewUnix();
        originalX7875NewUnix.setUID(new BigInteger("12345"));
        originalX7875NewUnix.setGID(new BigInteger("4567"));
        X7875_NewUnix clonedX7875NewUnix = (X7875_NewUnix) originalX7875NewUnix.clone();
        assertEquals(originalX7875NewUnix.getVersion(), clonedX7875NewUnix.getVersion());
        assertEquals(originalX7875NewUnix.getUID(), clonedX7875NewUnix.getUID());
        assertEquals(originalX7875NewUnix.getGID(), clonedX7875NewUnix.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        x7875NewUnix1.setUID(new BigInteger("12345"));
        x7875NewUnix1.setGID(new BigInteger("4567"));
        x7875NewUnix2.setUID(new BigInteger("12345"));
        x7875NewUnix2.setGID(new BigInteger("4567"));
        x7875NewUnix3.setUID(new BigInteger("56789"));
        x7875NewUnix3.setGID(new BigInteger("4567"));
        assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
        assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
        assertFalse(x7875NewUnix1.equals(null));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        x7875NewUnix1.setUID(new BigInteger("12345"));
        x7875NewUnix1.setGID(new BigInteger("4567"));
        x7875NewUnix2.setUID(new BigInteger("12345"));
        x7875NewUnix2.setGID(new BigInteger("4567"));
        x7875NewUnix3.setUID(new BigInteger("56789"));
        x7875NewUnix3.setGID(new BigInteger("4567"));
        int expectedHashcode1 = -1234567 * 1 + 12345 * 16 + 4567 * 16 * 16 + 4567 * 16 * 16 * 16;
        int expectedHashcode2 = expectedHashcode1;
        int expectedHashcode3 = -1234567 * 1 + 56789 * 16 + 4567 * 16 * 16 * 16;
        assertEquals(expectedHashcode1, x7875NewUnix1.hashCode());
        assertEquals(expectedHashcode2, x7875NewUnix2.hashCode());
        assertEquals(expectedHashcode3, x7875NewUnix3.hashCode());
    }
}