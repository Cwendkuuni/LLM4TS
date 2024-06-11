package org.apache.commons.compress.archivers.zip; // make sure the package name is correct

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    // 

    /**
     * Test for constructor.
     */
    @Test
    public void testX7875_NewUnix_Constructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(x7875NewUnix.getHeaderId(), ZipShort.valueOf("0x7875"));
        assertEquals(x7875NewUnix.getVersion(), 1);
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
    }

    /**
     * Test for getHeaderId() method.
     */
    @Test
    public void testX7875_NewUnix_getHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expected = ZipShort.valueOf("0x7875");
        assertEquals(x7875NewUnix.getHeaderId(), expected);
    }

    /**
     * Test for getUID() method.
     */
    @Test
    public void testX7875_NewUnix_getUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        long expected = 12345;
        long actual = x7875NewUnix.getUID();
        assertEquals(expected, actual);
    }

    /**
     * Test for getGID() method.
     */
    @Test
    public void testX7875_NewUnix_getGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(54321);
        long expected = 54321;
        long actual = x7875NewUnix.getGID();
        assertEquals(expected, actual);
    }

    /**
     * Test for setUID() method.
     */
    @Test
    public void testX7875_NewUnix_setUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        BigInteger expected = BigInteger.valueOf(12345);
        BigInteger actual = x7875NewUnix.getUID();
        assertEquals(expected, actual);
    }

    /**
     * Test for setGID() method.
     */
    @Test
    public void testX7875_NewUnix_setGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(54321);
        BigInteger expected = BigInteger.valueOf(54321);
        BigInteger actual = x7875NewUnix.getGID();
        assertEquals(expected, actual);
    }

    /**
     * Test for getLocalFileDataLength() method.
     */
    @Test
    public void testX7875_NewUnix_getLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1);
        x7875NewUnix.setGID(1);

        int uidSize = 1;
        int gidSize = 1;
        ZipShort expected = new ZipShort(uidSize + gidSize + 1); // version 1

        ZipShort actual = x7875NewUnix.getLocalFileDataLength();
        assertEquals(expected.getValue(), actual.getValue());
    }

    /**
     * Test for getCentralDirectoryLength() method.
     */
    @Test
    public void testX7875_NewUnix_getCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1);
        x7875NewUnix.setGID(1);

        int uidSize = 1;
        int gidSize = 1;
        ZipShort expected = new ZipShort(uidSize + gidSize + 1); // version 1

        ZipShort actual = x7875NewUnix.getCentralDirectoryLength();
        assertEquals(expected.getValue(), actual.getValue());
    }

    /**
     * Test for getLocalFileDataData() method.
     */
    @Test
    public void testX7875_NewUnix_getLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(54321);

        byte[] expected = {0x01, 0x02, 0x01, 0x00, 0x01, 0x00, 0x05, 0x00, 0x32, 0x00, 0x35, 0x00, 0x01, 0x00, 0x34, 0x00, 0x35};
        byte[] actual = x7875NewUnix.getLocalFileDataData();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test for parseFromLocalFileData() method.
     */
    @Test
    public void testX7875_NewUnix_parseFromLocalFileData() {
        byte[] data = {0x01, 0x02, 0x01, 0x00, 0x01, 0x00, 0x05, 0x00, 0x32, 0x00, 0x35, 0x00, 0x01, 0x00, 0x34, 0x00, 0x35};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        int offset = 0;
        int length = data.length;
        try {
            x7875NewUnix.parseFromLocalFileData(data, offset, length);
            BigInteger expectedUid = BigInteger.valueOf(12345);
            BigInteger expectedGid = BigInteger.valueOf(54321);
            BigInteger actualUid = x7875NewUnix.getUID();
            BigInteger actualGid = x7875NewUnix.getGID();

            assertEquals(expectedUid, actualUid);
            assertEquals(expectedGid, actualGid);
        } catch (ZipException e) {
            fail("parseFromLocalFileData should not throw an exception.");
        }
    }

    /**
     * Test for equals() method.
     */
    @Test
    public void testX7875_NewUnix_equals() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();

        x7875NewUnix1.setUID(12345);
        x7875NewUnix1.setGID(54321);

        x7875NewUnix2.setUID(12345);
        x7875NewUnix2.setGID(54321);

        assertTrue(x7875NewUnix1.equals(x7875NewUnix2));

        x7875NewUnix3.setUID(54321);
        x7875NewUnix3.setGID(12345);

        assertFalse(x7875NewUnix1.equals(x7875NewUnix3));

        x7875NewUnix3.setUID(12345);
        x7875NewUnix3.setGID(54321);

        assertTrue(x7875NewUnix1.equals(x7875NewUnix3));

        assertFalse(x7875NewUnix1.equals(null));
        assertFalse(x7875NewUnix1.equals(""));
        assertFalse(x7875NewUnix1.equals(new Object()));
    }

    /**
     * Test for hashCode() method.
     */
    @Test
    public void testX7875_NewUnix_hashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();

        x7875NewUnix1.setUID(12345);
        x7875NewUnix1.setGID(54321);

        x7875NewUnix2.setUID(12345);
        x7875NewUnix2.setGID(54321);

        int expected = -1094484445;
        int actual1 = x7875NewUnix1.hashCode();
        int actual2 = x7875NewUnix2.hashCode();

        assertEquals(expected, actual1);
        assertEquals(expected, actual2);

        x7875NewUnix3.setUID(54321);
        x7875NewUnix3.setGID(12345);

        int actual3 = x7875NewUnix3.hashCode();

        assertNotEquals(expected, actual3);
    }

    /**
     * Test for clone() method.
     */
    @Test
    public void testX7875_NewUnix_clone() {
        X7875_NewUnix original = new X7875_NewUnix();
        X7875_NewUnix clone = null;

        try {
            clone = (X7875_NewUnix) original.clone();
        } catch (CloneNotSupportedException e) {
            fail("clone() method should not throw CloneNotSupportedException.");
        }

        original.setUID(12345);
        original.setGID(54321);

        assertNull(clone.getUID());
        assertNull(clone.getGID());

        clone.setUID(67890);
        clone.setGID(98765);

        assertNotSame(original, clone);
        assertEquals(original.getUID(), clone.getUID());
        assertEquals(original.getGID(), clone.getGID());
    }

    /**
     * Test for toString() method.
     */
    @Test
    public void testX7875_NewUnix_toString() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(54321);

        String expected = "0x7875 Zip Extra Field: UID=12345 GID=54321";
        String actual = x7875NewUnix.toString();

        assertEquals(expected, actual);
    }

    // 
}