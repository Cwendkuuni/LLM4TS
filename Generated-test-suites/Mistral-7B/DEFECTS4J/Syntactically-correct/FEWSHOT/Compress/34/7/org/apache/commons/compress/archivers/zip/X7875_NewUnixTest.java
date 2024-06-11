package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;
import java.util.zip.ZipException;

public class X7875_NewUnixTest {

    @Test
    public void testX7875_NewUnix_Constructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(1, x7875NewUnix.getVersion());
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
    }

    @Test
    public void testX7875_NewUnix_SetUIDAndGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 12345;
        x7875NewUnix.setUID(uid);
        assertEquals(uid, x7875NewUnix.getUID());
    }

    @Test
    public void testX7875_NewUnix_SetGIDAndGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 67890;
        x7875NewUnix.setGID(gid);
        assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testX7875_NewUnix_GetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        BigInteger uid = BigInteger.valueOf(12345);
        BigInteger gid = BigInteger.valueOf(67890);
        x7875NewUnix.setUID(uid.longValue());
        x7875NewUnix.setGID(gid.longValue());

        int uidSize = uid.toByteArray().length;
        int gidSize = gid.toByteArray().length;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();

        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testX7875_NewUnix_GetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        BigInteger uid = BigInteger.valueOf(12345);
        BigInteger gid = BigInteger.valueOf(67890);
        x7875NewUnix.setUID(uid.longValue());
        x7875NewUnix.setGID(gid.longValue());

        int uidSize = uid.toByteArray().length;
        int gidSize = gid.toByteArray().length;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();

        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test(expected = ZipException.class)
    public void testX7875_NewUnix_ParseFromCentralDirectoryData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] buffer = new byte[0];
        x7875NewUnix.parseFromCentralDirectoryData(buffer, 0, buffer.length);
    }

    @Test
    public void testX7875_NewUnix_ParseFromLocalFileData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07};

        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(new BigInteger("12345"), x7875NewUnix.getUID());
        assertEquals(new BigInteger("67890"), x7875NewUnix.getGID());
    }

    @Test
    public void testX7875_NewUnix_ToString() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(BigInteger.valueOf(12345));
        x7875NewUnix.setGID(BigInteger.valueOf(67890));

        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=67890", x7875NewUnix.toString());
    }

    @Test
    public void testX7875_NewUnix_Clone() {
        X7875_NewUnix original = new X7875_NewUnix();
        original.setUID(BigInteger.valueOf(12345));
        original.setGID(BigInteger.valueOf(67890));

        try {
            X7875_NewUnix clone = (X7875_NewUnix) original.clone();
            assertEquals(original.getUID(), clone.getUID());
            assertEquals(original.getGID(), clone.getGID());
        } catch (CloneNotSupportedException e) {
            fail("Clone should not throw CloneNotSupportedException");
        }
    }

    @Test
    public void testX7875_NewUnix_Equals() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;

        x7875NewUnix1.setUID(BigInteger.valueOf(12345));
        x7875NewUnix1.setGID(BigInteger.valueOf(67890));
        x7875NewUnix2.setUID(BigInteger.valueOf(12345));
        x7875NewUnix2.setGID(BigInteger.valueOf(67890));

        assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
        assertTrue(x7875NewUnix1.equals(x7875NewUnix3));
        assertFalse(x7875NewUnix1.equals(null));
        assertFalse(x7875NewUnix1.equals(new Object()));
    }

    @Test
    public void testX7875_NewUnix_HashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;

        x7875NewUnix1.setUID(BigInteger.valueOf(12345));
        x7875NewUnix1.setGID(BigInteger.valueOf(67890));
        x7875NewUnix2.setUID(BigInteger.valueOf(12345));
        x7875NewUnix2.setGID(BigInteger.valueOf(67890));

        int expectedHashCode = 2149473151;
        int actualHashCode = x7875NewUnix1.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
        assertEquals(expectedHashCode, x7875NewUnix2.hashCode());
        assertEquals(expectedHashCode, x7875NewUnix3.hashCode());
    }
}