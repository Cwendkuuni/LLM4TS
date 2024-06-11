package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Test for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix uef = new X7875_NewUnix();
        assertEquals(1, uef.getVersion());
        assertEquals(BigInteger.ZERO, uef.getUid());
        assertEquals(BigInteger.ZERO, uef.getGid());
    }

    /**
     * Tests get/set UID and GID.
     */
    @Test
    public void testUidGid() {
        X7875_NewUnix uef = new X7875_NewUnix();
        uef.setUID(123456789);
        uef.setGID(234567890);
        assertEquals("UID", 123456789, uef.getUID());
        assertEquals("GID", 234567890, uef.getGID());
    }

    /**
     * Tests parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws IOException {
        byte[] data = new byte[] {
            0x78, 0x75, (byte) 0x01, (byte) 0x04,
            0x00, 0x00, 0x00, 0x2a, (byte) 0x7a, (byte) 0x69, (byte) 0x62, (byte) 0x73, (byte) 0x6f, (byte) 0x6d, 0x00,
            0x01, 0x04, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00, 0x01, 0x04, 0x32, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00};

        X7875_NewUnix uef = new X7875_NewUnix();
        uef.parseFromLocalFileData(data, 0, data.length);
        assertEquals("UID", 313233343536373839L, uef.getUID());
        assertEquals("GID", 32343536373839L, uef.getGID());
    }

    /**
     * Tests parsing from local file data with incorrect data length.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidDataLength() throws IOException {
        byte[] data = new byte[] {
            0x78, 0x75, (byte) 0x01, (byte) 0x04,
            0x00, 0x00, 0x00, 0x2a, (byte) 0x7a, (byte) 0x69, (byte) 0x62, (byte) 0x73, (byte) 0x6f, (byte) 0x6d, 0x00,
            0x01, 0x04, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00, 0x01, 0x03}; // incorrect last byte

        X7875_NewUnix uef = new X7875_NewUnix();
        uef.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Tests parsing from local file data with incorrect version number.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidVersion() throws IOException {
        byte[] data = new byte[] {
            0x78, 0x75, (byte) 0x02, // incorrect version
            0x00, 0x00, 0x00, 0x2a, (byte) 0x7a, (byte) 0x69, (byte) 0x62, (byte) 0x73, (byte) 0x6f, (byte) 0x6d, 0x00,
            0x01, 0x04, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00, 0x01, 0x04, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};

        X7875_NewUnix uef = new X7875_NewUnix();
        uef.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Tests getLocalFileDataLength.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix uef = new X7875_NewUnix();
        uef.setUID(123456789);
        uef.setGID(234567890);
        ZipShort length = uef.getLocalFileDataLength();
        assertEquals("length", (short) 11, length.getValue());
    }

    /**
     * Tests getCentralDirectoryLength.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix uef = new X7875_NewUnix();
        assertEquals("central directory length", (short) 0, uef.getCentralDirectoryLength().getValue());
    }

    /**
     * Tests getLocalFileDataData.
     */
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix uef = new X7875_NewUnix();
        uef.setUID(123456789);
        uef.setGID(234567890);
        byte[] data = uef.getLocalFileDataData();

        // check size
        assertEquals("size", 11, data.length);

        // check uid
        byte[] uidBytes = new byte[8];
        System.arraycopy(data, 2, uidBytes, 0, 8);
        byte[] uidBytesExpected = new byte[] {
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38};
        assertArrayEquals("uid", uidBytesExpected, uidBytes);

        // check gid
        byte[] gidBytes = new byte[8];
        System.arraycopy(data, 10, gidBytes, 0, 8);
        byte[] gidBytesExpected = new byte[] {
            0x32, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30};
        assertArrayEquals("gid", gidBytesExpected, gidBytes);
    }

    /**
     * Tests clone.
     */
    @Test
    public void testClone() {
        X7875_NewUnix uef = new X7875_NewUnix();
        uef.setUID(123456789);
        uef.setGID(234567890);
        X7875_NewUnix clonedUef = (X7875_NewUnix) uef.clone();
        assertEquals("UID", 123456789, clonedUef.getUID());
        assertEquals("GID", 234567890, clonedUef.getGID());
    }

    /**
     * Tests equals.
     */
    @Test
    public void testEquals() {
        X7875_NewUnix uef1 = new X7875_NewUnix();
        uef1.setUID(123456789);
        uef1.setGID(234567890);
        X7875_NewUnix uef2 = new X7875_NewUnix();
        uef2.setUID(123456789);
        uef2.setGID(234567890);
        Object obj = new Object();
        X7875_NewUnix uef3 = new X7875_NewUnix();
        uef3.setUID(234567890);
        uef3.setGID(123456789);

        assertEquals("same object", uef1, uef1);
        assertEquals("same uid and gid", uef1, uef2);
        assertNotEquals("different class", uef1, obj);
        assertNotEquals("different uid and gid", uef1, uef3);
    }

    /**
     * Tests hashCode.
     */
    @Test
    public void testHashCode() {
        X7875_NewUnix uef1 = new X7875_NewUnix();
        uef1.setUID(123456789);
        uef1.setGID(234567890);
        X7875_NewUnix uef2 = new X7875_NewUnix();
        uef2.setUID(123456789);
        uef2.setGID(234567890);
        Object obj = new Object();
        X7875_NewUnix uef3 = new X7875_NewUnix();
        uef3.setUID(234567890);
        uef3.setGID(123456789);

        int hashCode1 = uef1.hashCode();
        int hashCode2 = uef2.hashCode();
        int hashCode3 = uef3.hashCode();
        int hashCodeObj = obj.hashCode();

        assertEquals("same object", hashCode1, hashCode1);
        assertEquals("same uid and gid", hashCode1, hashCode2);
        assertNotEquals("different uid and gid", hashCode1, hashCode3);
        assertNotEquals("different class", hashCode1, hashCodeObj);
    }
}