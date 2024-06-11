package org.apache.commons.compress.archivers.zip; // make sure the package is correct

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {
    // 

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(new ZipShort(0x7875), headerId);
    }

    @Test
    public void testGetUIDAndSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long uid = 1234567890L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());

        long newUid = 9876543210L;
        x7875.setUID(newUid);
        assertEquals(newUid, x7875.getUID());
    }

    @Test
    public void testGetGIDAndSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long gid = 23456789012L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());

        long newGid = 1357987654321L;
        x7875.setGID(newGid);
        assertEquals(newGid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");

        x7875.setUID(uid.longValue());
        x7875.setGID(gid.longValue());

        ZipShort expectedLength = new ZipShort((int) (3 + uid.toByteArray().length + gid.toByteArray().length));
        ZipShort actualLength = x7875.getLocalFileDataLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix expectedX7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        long gid = 9876543210L;
        expectedX7875.setUID(uid);
        expectedX7875.setGID(gid);

        byte[] data = expectedX7875.getLocalFileDataData();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        X7875_NewUnix actualX7875 = new X7875_NewUnix();
        actualX7875.parseFromLocalFileData(inputStream, 0, data.length);

        assertEquals(expectedX7875.getUID(), actualX7875.getUID());
        assertEquals(expectedX7875.getGID(), actualX7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[] { 0x78, 0x75, 0x01, 0x01, 0x01, 0x02 };
        x7875.parseFromLocalFileData(new ByteArrayInputStream(data), 0, data.length);
        fail("Expected ZipException but did not receive one.");
    }

    // 
}