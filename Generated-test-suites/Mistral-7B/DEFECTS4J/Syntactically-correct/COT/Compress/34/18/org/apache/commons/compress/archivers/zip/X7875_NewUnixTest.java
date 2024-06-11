package org.apache.commons.compress.archivers.zip; // Import the package for importing necessary classes

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.ZipShort;

import static org.apache.commons.compress.archivers.zip.ZipUtil.reverse;
import static org.apache.commons.compress.archivers.zip.ZipUtil.signedByteToUnsignedInt;
import static org.apache.commons.compress.archivers.zip.ZipUtil.unsignedIntToSignedByte;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    

    @Test
    public void testConstructor() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        Assert.assertEquals(X7875_NewUnix.HEADER_ID, testObj.getHeaderId());
        Assert.assertEquals(BigInteger.ONE, testObj.getUID());
        Assert.assertEquals(BigInteger.ONE, testObj.getGID());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        Assert.assertEquals(X7875_NewUnix.HEADER_ID, testObj.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        Assert.assertEquals(BigInteger.ONE, testObj.getUID());

        testObj.setUID(12345);
        Assert.assertEquals(Long.toUnsignedLong(12345), testObj.getUID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.setUID(12345);
        Assert.assertEquals(Long.toUnsignedLong(12345), testObj.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        Assert.assertEquals(BigInteger.ONE, testObj.getGID());

        testObj.setGID(54321);
        Assert.assertEquals(Long.toUnsignedLong(54321), testObj.getGID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.setGID(54321);
        Assert.assertEquals(Long.toUnsignedLong(54321), testObj.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix testObj = new X7875_NewUnix();

        testObj.setUID(12345);
        testObj.setGID(54321);

        byte[] uidBytes = testObj.uid.toByteArray();
        byte[] gidBytes = testObj.gid.toByteArray();

        int uidSize = uidBytes.length;
        int gidSize = gidBytes.length;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = testObj.getLocalFileDataLength();

        Assert.assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix testObj = new X7875_NewUnix();

        testObj.setUID(12345);
        testObj.setGID(54321);

        byte[] uidBytes = testObj.uid.toByteArray();
        byte[] gidBytes = testObj.gid.toByteArray();

        int uidSize = uidBytes.length;
        int gidSize = gidBytes.length;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = testObj.getCentralDirectoryLength();

        Assert.assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] testData = {0x78, (byte) 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00,
                           (byte) 0x78, (byte) 0x55, 0x01, 0x01, 0x30, 0x31, 0x32, 0x33,
                           (byte) 0x78, (byte) 0x55, 0x01, 0x01, 0x34, 0x35, 0x36, 0x37};

        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.parseFromLocalFileData(testData, 0, testData.length);

        Assert.assertEquals(1, testObj.version);
        Assert.assertEquals(Long.toUnsignedLong(313233345), testObj.getUID());
        Assert.assertEquals(Long.toUnsignedLong(343536375), testObj.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidDataSize() throws ZipException {
        byte[] testData = {0x78, (byte) 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00,
                           (byte) 0x78, (byte) 0x55, 0x01, 0x01};

        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.parseFromLocalFileData(testData, 0, testData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidUID() throws ZipException {
        byte[] testData = {0x78, (byte) 0x75, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00,
                           (byte) 0x78, (byte) 0x55, 0x01, 0x01, 0x30, 0xFF, 0xFF, 0xFF};

        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.parseFromLocalFileData(testData, 0, testData.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidGID() throws ZipException {
        byte[] testData = {0x78, (byte) 0x75, 0x01, 0x01, 0x00, 0x00, 0x01, 0x00,
                           (byte) 0x78, (byte) 0x55, 0x01, 0x01, 0x30, 0xFF, 0xFF, 0xFF,
                           (byte) 0x78, (byte) 0x55, 0x01, 0x01, 0x31, 0x80, 0x00, 0x00};

        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.parseFromLocalFileData(testData, 0, testData.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix testObj = new X7875_NewUnix();
        testObj.setUID(12345);
        testObj.setGID(54321);

        String expectedString = "0x7875 Zip Extra Field: UID=12345 GID=54321";
        String actualString = testObj.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix testObj1 = new X7875_NewUnix();
        testObj1.setUID(12345);
        testObj1.setGID(54321);

        X7875_NewUnix testObj2 = new X7875_NewUnix();
        testObj2.setUID(12345);
        testObj2.setGID(54321);

        X7875_NewUnix testObj3 = new X7875_NewUnix();
        testObj3.setUID(54321);
        testObj3.setGID(12345);

        Assert.assertTrue(testObj1.equals(testObj1));
        Assert.assertTrue(testObj1.equals(testObj2));
        Assert.assertFalse(testObj1.equals(testObj3));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix testObj1 = new X7875_NewUnix();
        testObj1.setUID(12345);
        testObj1.setGID(54321);

        X7875_NewUnix testObj2 = new X7875_NewUnix();
        testObj2.setUID(12345);
        testObj2.setGID(54321);

        X7875_NewUnix testObj3 = new X7875_NewUnix();
        testObj3.setUID(54321);
        testObj3.setGID(12345);

        Assert.assertEquals(testObj1.hashCode(), testObj2.hashCode());
        Assert.assertNotEquals(testObj1.hashCode(), testObj3.hashCode());
    }

    
}