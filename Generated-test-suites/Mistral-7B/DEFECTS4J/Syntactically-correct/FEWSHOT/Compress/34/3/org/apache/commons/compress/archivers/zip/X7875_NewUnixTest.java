package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

    @Test
    public void testNewUnix() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertEquals(1, newUnix.getVersion());
        assertNull(newUnix.getUID());
        assertNull(newUnix.getGID());
    }

    @Test
    public void testSetUIDAndGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        long uid = 123456789L;
        long gid = 987654321L;
        newUnix.setUID(uid);
        newUnix.setGID(gid);
        assertEquals(uid, newUnix.getUID());
        assertEquals(gid, newUnix.getGID());
    }

    @Test
    public void testGetUIDAndGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        long uid = 123456789L;
        long gid = 987654321L;
        newUnix.setUID(uid);
        newUnix.setGID(gid);
        assertEquals(uid, newUnix.getUID());
        assertEquals(gid, newUnix.getGID());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x04, (byte) 0x63, 0x62, 0x65, 0x63, 0x74, 0x32, 0x36,
                0x35, 0x37, 0x38, 0x39, (byte) 0x01, 0x04, 0x63, 0x62, 0x65, 0x63,
                0x74, 0x32, 0x30, 0x36, 0x35, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
                0x01, 0x04, 0x63, 0x62, 0x65, 0x63, 0x74, 0x32, 0x39, 0x38, 0x37,
                0x36, 0x35, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39
        };

        newUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, newUnix.getVersion());
        assertEquals(123456789L, newUnix.getUID());
        assertEquals(987654321L, newUnix.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x04, (byte) 0x63, 0x62, 0x65, 0x63, 0x74, 0x32, 0x36,
                0x35, 0x37, 0x38, 0x39, 0x01, 0x05 // invalid version value
        };

        newUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array1 = new byte[0];
        byte[] array2 = new byte[]{(byte) 0x00};
        byte[] array3 = new byte[]{(byte) 0x00, (byte) 0x00};
        byte[] array4 = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00};
        byte[] array5 = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00};

        assertEquals(new byte[0], X7875_NewUnix.trimLeadingZeroesForceMinLength(array1));
        assertEquals(new byte[]{0x00}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array2));
        assertEquals(new byte[]{0x00}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array3));
        assertEquals(new byte[]{0x01}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array4));
        assertEquals(new byte[]{0x01, 0x00}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array5));
    }

    @Test
    public void testEquals() {
        X7875_NewUnix newUnix1 = new X7875_NewUnix();
        X7875_NewUnix newUnix2 = new X7875_NewUnix();
        long uid = 123456789L;
        long gid = 987654321L;
        newUnix1.setUID(uid);
        newUnix1.setGID(gid);
        newUnix2.setUID(uid);
        newUnix2.setGID(gid);
        assertTrue(newUnix1.equals(newUnix2));
        X7875_NewUnix newUnix3 = new X7875_NewUnix();
        assertFalse(newUnix1.equals(newUnix3));
        X7875_NewUnix newUnix4 = new X7875_NewUnix();
        newUnix4.setUID(uid + 1);
        assertFalse(newUnix1.equals(newUnix4));
        newUnix4.setGID(gid);
        assertFalse(newUnix1.equals(newUnix4));
        newUnix4.setUID(uid);
        assertFalse(newUnix1.equals(newUnix4));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix newUnix1 = new X7875_NewUnix();
        long uid = 123456789L;
        long gid = 987654321L;
        newUnix1.setUID(uid);
        newUnix1.setGID(gid);

        X7875_NewUnix newUnix2 = new X7875_NewUnix();
        newUnix2.setUID(uid);
        newUnix2.setGID(gid);

        X7875_NewUnix newUnix3 = new X7875_NewUnix();
        newUnix3.setUID(uid + 1);
        newUnix3.setGID(gid);

        X7875_NewUnix newUnix4 = new X7875_NewUnix();
        newUnix4.setUID(uid);
        newUnix4.setGID(gid + 1);

        int hashCode1 = newUnix1.hashCode();
        int hashCode2 = newUnix2.hashCode();
        int hashCode3 = newUnix3.hashCode();
        int hashCode4 = newUnix4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}