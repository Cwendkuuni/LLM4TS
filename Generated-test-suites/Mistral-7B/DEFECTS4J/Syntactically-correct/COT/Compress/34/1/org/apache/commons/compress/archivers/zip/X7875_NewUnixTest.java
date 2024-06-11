package org.apache.commons.compress.archivers.zip; // import necessary packages here

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    // Test case for constructor and getHeaderId() method
    @Test
    public void testX7875_NewUnixConstructorAndGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedHeaderId = new ZipShort(0x7875);
        ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
        assertEquals(expectedHeaderId, actualHeaderId);
    }

    // Test case for getUID() and setUID() methods
    @Test
    public void testGetUIDAndSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long testUID = 1001;
        x7875NewUnix.setUID(testUID);
        long actualUID = x7875NewUnix.getUID();
        assertEquals(testUID, actualUID);
    }

    // Test case for getGID() and setGID() methods
    @Test
    public void testGetGIDAndSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long testGID = 1002;
        x7875NewUnix.setGID(testGID);
        long actualGID = x7875NewUnix.getGID();
        assertEquals(testGID, actualGID);
    }

    // Test case for getLocalFileDataLength() method
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        BigInteger uid = new BigInteger("1001010101010101");
        BigInteger gid = new BigInteger("1002020202020202");
        x7875NewUnix.uid = uid;
        x7875NewUnix.gid = gid;
        int expectedLength = uid.byteLength() + gid.byteLength() + 3;
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        assertEquals(new ZipShort((short) expectedLength), actualLength);
    }

    // Test case for getCentralDirectoryLength() method
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
        assertEquals(new ZipShort((short) 0), actualLength);
    }

    // Test case for getLocalFileDataData() method
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        BigInteger uid = new BigInteger("1001010101010101");
        BigInteger gid = new BigInteger("1002020202020202");
        x7875NewUnix.uid = uid;
        x7875NewUnix.gid = gid;
        byte[] expectedData = new byte[uid.byteLength() + gid.byteLength() + 3];
        byte[] actualData = x7875NewUnix.getLocalFileDataData();

        // Set up expected data
        int pos = 0;
        expectedData[pos++] = (byte) 1; // version
        expectedData[pos++] = (byte) (uid.byteLength() + 1); // uid size
        System.arraycopy(uid.toByteArray(), 0, expectedData, pos, uid.byteLength());
        pos += uid.byteLength();
        expectedData[pos++] = (byte) (gid.byteLength() + 1); // gid size
        System.arraycopy(gid.toByteArray(), 0, expectedData, pos, gid.byteLength());

        // Compare expected and actual data
        assertArrayEquals(expectedData, actualData);
    }

    // Test case for parseFromLocalFileData() method
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] testData = new byte[]{(byte) 1, (byte) 4, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0};
        int offset = 0;
        int length = testData.length;
        x7875NewUnix.parseFromLocalFileData(testData, offset, length);
        BigInteger expectedUid = new BigInteger("1001");
        BigInteger expectedGid = new BigInteger("1002");
        assertEquals(expectedUid, x7875NewUnix.uid);
        assertEquals(expectedGid, x7875NewUnix.gid);
    }

    // Test case for equals() and hashCode() methods
    @Test
    public void testEqualsAndHashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        x7875NewUnix1.setUID(1001);
        x7875NewUnix1.setGID(1002);
        x7875NewUnix2.setUID(1001);
        x7875NewUnix2.setGID(1002);
        x7875NewUnix3.setUID(1003);
        x7875NewUnix3.setGID(1002);
        assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
        assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
        assertEquals(x7875NewUnix1.hashCode(), x78785NewUnix2.hashCode());
        assertNotEquals(x7875NewUnix1.hashCode(), x78785NewUnix3.hashCode());
    }
}