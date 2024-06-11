package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.utils.ZipShort;

import java.math.BigInteger;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345678l);
        assertEquals(12345678L, x7875.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(23456789l);
        assertEquals(23456789L, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(BigInteger.valueOf(1234567890123L));
        x7875.setGID(BigInteger.valueOf(2345678902345L));

        int uidSize = x7875.getUID().toByteArray().length;
        int gidSize = x7875.getGID().toByteArray().length;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getLocalFileDataLength();

        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        byte[] data = new byte[] {
            0x78, 0x75, // Header ID
            0x03, (byte) 0x7f, // Length of data in this extra field
            0x01, (byte) 0x01, // Version
            0x01, (byte) 0x0a, // Size of UID
            (byte) 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // UID
            0x01, (byte) 0x0b, // Size of GID
            (byte) 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 // GID
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("10901234567890123"), x7875.getUID());
        assertEquals(new BigInteger("108023456789012345"), x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws Exception {
        byte[] data = new byte[] {
                0x78, 0x75, // Header ID
                0x03, (byte) 0x7f, // Length of data in this extra field
                0x01, (byte) 0x01, // Version
                0x01, (byte) 0x0a // Size of UID
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidVersion() throws Exception {
        byte[] data = new byte[] {
                0x78, 0x75, // Header ID
                0x03, (byte) 0x7f, // Length of data in this extra field
                0x02, (byte) 0x01, // Version
                0x01, (byte) 0x0a, // Size of UID
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(BigInteger.valueOf(1234567890123L));
        x7875.setGID(BigInteger.valueOf(2345678902345L));

        String expectedString = "0x7875 Zip Extra Field: UID=1234567890123 GID=2345678902345";
        String actualString = x7875.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        X7875_NewUnix x7875_3 = new X7875_NewUnix();

        x7875_1.setUID(BigInteger.valueOf(1234567890123L));
        x7875_1.setGID(BigInteger.valueOf(2345678902345L));

        x7875_2.setUID(BigInteger.valueOf(1234567890123L));
        x7875_2.setGID(BigInteger.valueOf(2345678902345L));

        x7875_3.setUID(BigInteger.valueOf(1234567890124L));
        x7875_3.setGID(BigInteger.valueOf(2345678902345L));

        assertTrue(x7875_1.equals(x7875_2));
        assertFalse(x7875_1.equals(x7875_3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = (X7875_NewUnix) x7875_1.clone();

        x7875_1.setUID(BigInteger.valueOf(1234567890123L));
        x7875_1.setGID(BigInteger.valueOf(2345678902345L));

        x7875_2.setUID(BigInteger.valueOf(1234567890124L));
        x7875_2.setGID(BigInteger.valueOf(2345678902346L));

        assertTrue(x7875_1.equals(x7875_2));
        assertFalse(x7875_1.equals(x7875_1.clone()));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        X7875_NewUnix x7875_3 = new X7875_NewUnix();

        x7875_1.setUID(BigInteger.valueOf(1234567890123L));
        x7875_1.setGID(BigInteger.valueOf(2345678902345L));

        x7875_2.setUID(BigInteger.valueOf(1234567890123L));
        x7875_2.setGID(BigInteger.valueOf(2345678902345L));

        x7875_3.setUID(BigInteger.valueOf(1234567890123L));
        x7875_3.setGID(BigInteger.valueOf(2345678902346L));

        int x7875_1_hashCode = x7875_1.hashCode();
        int x7875_2_hashCode = x7875_2.hashCode();
        int x7875_3_hashCode = x7875_3.hashCode();

        assertEquals(x7875_1_hashCode, x7875_2.hashCode());
        assertNotEquals(x7875_1_hashCode, x7875_3.hashCode());
    }

}