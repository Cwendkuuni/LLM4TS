package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Test cases for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test creating a X7875_NewUnix instance with default values.
     */
    @Test
    public void testCreateDefaultInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertEquals(1000L, x7875.getUID());
        assertEquals(1000L, x7875.getGID());
    }

    /**
     * Test setting and getting UID and GID values.
     */
    @Test
    public void testSetGetUidAndGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890123L;
        long gid = 9876543210987L;

        x7875.setUID(uid);
        x7875.setGID(gid);

        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    /**
     * Test parsing X7875_NewUnix instance from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws IOException {
        final long UID = 1234567890123L;
        final long GID = 9876543210987L;
        byte[] data = new byte[] {
                0x78, (byte) 0x75, // tag
                0x04, (byte) 0x1C, // total data size
                0x01, // version
                0x03, // UID size
                (byte) 0x1A, 0x00, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23, // UID
                0x03, // GID size
                (byte) 0x1A, 0x00, 0x54, 0x32, 0x10, 0x98, 0x07, 0x54 // GID
        };

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(UID, x7875.getUID());
        assertEquals(GID, x7875.getGID());
    }

    /**
     * Test throwing ZipException when parsing invalid data from local file data.
     */
    @Test(expected = ZipException.class)
    public void testParseInvalidLocalFileData() throws IOException {
        byte[] data = new byte[] {
                0x78, (byte) 0x75, // tag
                0x04, (byte) 0x1C, // total data size
                0x01, // version
                0x03, // UID size
                (byte) 0x1A, 0x00, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23, // UID
                0x03, // GID size (expected 1, but this is 3)
                (byte) 0x1A, 0x00, 0x54, 0x32, 0x10, 0x98, 0x07, 0x54, 0x42 // GID
        };

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test getting local file data length.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890123L);
        x7875.setGID(9876543210987L);

        ZipShort localFileDataLength = x7875.getLocalFileDataLength();
        assertEquals(11, localFileDataLength.getValue());
    }

    /**
     * Test getting central directory length.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890123L);
        x7875.setGID(9876543210987L);

        ZipShort centralDirectoryLength = x7875.getCentralDirectoryLength();
        assertEquals(0, centralDirectoryLength.getValue());
    }

    /**
     * Test getting local file data data.
     */
    @Test
    public void testGetLocalFileDataData() throws IOException {
        final long UID = 1234567890123L;
        final long GID = 9876543210987L;

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(UID);
        x7875.setGID(GID);

        byte[] expectedData = new byte[] {
                0x78, (byte) 0x75, // tag
                0x06, // total data size
                0x01, // version
                0x03, // UID size
                (byte) 0x1A, 0x00, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23, // UID
                0x03, // GID size
                (byte) 0x1A, 0x00, 0x54, 0x32, 0x10, 0x98, 0x07, 0x54 // GID
        };

        byte[] data = x7875.getLocalFileDataData();
        assertEquals(expectedData.length, data.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], data[i]);
        }
    }

    /**
     * Test getting central directory data.
     */
    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] expectedData = new byte[0];
        byte[] data = x7875.getCentralDirectoryData();
        assertEquals(expectedData.length, data.length);
    }
}