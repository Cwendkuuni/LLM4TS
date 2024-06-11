package org.apache.commons.compress.archivers.zip; // adjust the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertEquals(1, newUnix.getVersion());
        assertNull(newUnix.getUID());
        assertNull(newUnix.getGID());
        assertEquals(new ZipShort((short) 0), newUnix.getHeaderId());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), newUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(123456L);
        assertEquals(123456L, newUnix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(234567L);
        assertEquals(234567L, newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(123456L);
        newUnix.setGID(234567L);

        int expectedLength = 3 + 1 + 1 + (1 + 4) + (1 + 4); // 1 for version, 1 for uidSize, 1 for gidSize, uid length, gid length
        assertEquals(new ZipShort((short) expectedLength), newUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(123456L);
        newUnix.setGID(234567L);

        int expectedLength = newUnix.getLocalFileDataLength().getValue();
        assertEquals(new ZipShort((short) expectedLength), newUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(123456L);
        newUnix.setGID(234567L);

        byte[] expectedData = new byte[newUnix.getLocalFileDataLength().getValue()];
        newUnix.parseFromLocalFileData(expectedData, 0, expectedData.length);

        byte[] newData = newUnix.getLocalFileDataData();
        assertArrayEquals(expectedData, newData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] invalidData = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x03, (byte) 0x02, (byte) 0x01}; // invalid version number
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.parseFromLocalFileData(invalidData, 0, invalidData.length);
    }

    @Test
    public void testEqualsAndHashcode() {
        X7875_NewUnix newUnix1 = new X7875_NewUnix();
        X7875_NewUnix newUnix2 = new X7875_NewUnix();
        X7875_NewUnix newUnix3 = new X7875_NewUnix();

        newUnix1.setUID(123456L);
        newUnix1.setGID(234567L);
        newUnix2.setUID(123456L);
        newUnix2.setGID(234567L);
        newUnix3.setUID(567890L);
        newUnix3.setGID(876543L);

        assertEquals(newUnix1, newUnix2);
        assertFalse(newUnix1.equals(newUnix3));

        int hashcode1 = newUnix1.hashCode();
        int hashcode2 = newUnix2.hashCode();
        int hashcode3 = newUnix3.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertFalse(hashcode1 == hashcode3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        X7875_NewUnix clonedNewUnix = (X7875_NewUnix) newUnix.clone();

        newUnix.setUID(123456L);
        newUnix.setGID(234567L);

        assertEquals(newUnix.getUID(), clonedNewUnix.getUID());
        assertEquals(newUnix.getGID(), clonedNewUnix.getGID());
    }
}