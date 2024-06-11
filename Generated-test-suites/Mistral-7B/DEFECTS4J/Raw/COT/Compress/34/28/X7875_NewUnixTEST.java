package org.apache.commons.compress.archivers.zip; // Import the package for the class under test

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {
    

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort actual = x7875.getHeaderId();
        ZipShort expected = new ZipShort(0x7875);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long expected = 1000;
        x7875.setUID(expected);
        long actual = x7875.getUID();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long expected = 1000;
        x7875.setGID(expected);
        long actual = x7875.getGID();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890l;
        x7875.setUID(uid);
        long actual = x7875.getUID();
        Assert.assertEquals(uid, actual);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210l;
        x7875.setGID(gid);
        long actual = x7875.getGID();
        Assert.assertEquals(gid, actual);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int expectedUidSize = 4;
        int expectedGidSize = 4;
        x7875.uid = new BigInteger("12345678901234567890"); // Set UID size
        x7875.gid = new BigInteger("98765432109876543210"); // Set GID size

        int actual = x7875.getLocalFileDataLength().getValue();
        int expected = 3 + expectedUidSize + expectedGidSize;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.uid = new BigInteger("12345678901234567890"); // Set UID
        x7875.gid = new BigInteger("98765432109876543210"); // Set GID

        byte[] expectedUidBytes = new BigInteger("12345678901234567890").toByteArray();
        byte[] expectedGidBytes = new BigInteger("98765432109876543210").toByteArray();

        byte[] actualData = x7875.getLocalFileDataData();
        int uidSize = x7875.getLocalFileDataLength().getValue() - 3;
        int gidSize = x7875.getLocalFileDataLength().getValue() - 3 - uidSize;

        byte[] actualUidBytes = new byte[uidSize];
        byte[] actualGidBytes = new byte[gidSize];

        System.arraycopy(actualData, 0, actualUidBytes, 0, uidSize);
        System.arraycopy(actualData, uidSize, actualGidBytes, 0, gidSize);

        Assert.assertArrayEquals(expectedUidBytes, actualUidBytes);
        Assert.assertArrayEquals(expectedGidBytes, actualGidBytes);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] invalidData = {0x78, 0x75, 0x01, 0x02, 0x03, 0x04};
        x7875.parseFromLocalFileData(invalidData, 0, invalidData.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] validData = {0x78, 0x75, (byte) 0x01, (byte) 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        x7875.parseFromLocalFileData(validData, 0, validData.length);
        int expectedVersion = 1;
        int uidSize = 1;
        byte[] uidBytes = new byte[]{0x01};
        byte[] gidBytes = new byte[]{0x01};
        BigInteger expectedUid = new BigInteger(1);
        BigInteger expectedGid = new BigInteger(1);

        int actualVersion = x7875.version;
        int actualUidSize = (int) x7875.uid.toByteArray().length;
        byte[] actualUidBytes = (byte[]) x7875.uid.toByteArray();
        byte[] actualGidBytes = (byte[]) x7875.gid.toByteArray();
        int actualGidSize = (int) x7875.gid.toByteArray().length;

        Assert.assertEquals(expectedVersion, actualVersion);
        Assert.assertEquals(uidSize, actualUidSize);
        Assert.assertArrayEquals(uidBytes, actualUidBytes);
        Assert.assertEquals(expectedUid, x7875.uid);
        Assert.assertEquals(uidSize, actualGidSize);
        Assert.assertArrayEquals(gidBytes, actualGidBytes);
        Assert.assertEquals(expectedGid, x7875.gid);
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] emptyData = {};

        x7875.parseFromCentralDirectoryData(emptyData, 0, emptyData.length);
        // No assertions here, as this method does nothing
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.uid = new BigInteger("12345678901234567890"); // Set UID
        x7875.gid = new BigInteger("98765432109876543210"); // Set GID
        x7875.reset();

        int uidSize = x7875.getLocalFileDataLength().getValue() - 3;
        int gidSize = x7875.getLocalFileDataLength().getValue() - 3 - uidSize;

        byte[] actualUidBytes = new byte[uidSize];
        byte[] actualGidBytes = new byte[gidSize];

        System.arraycopy(x7875.getLocalFileDataData(), 0, actualUidBytes, 0, uidSize);
        System.arraycopy(x7875.getLocalFileDataData(), uidSize, actualGidBytes, 0, gidSize);

        Assert.assertArrayEquals(new byte[uidSize], actualUidBytes);
        Assert.assertArrayEquals(new byte[gidSize], actualGidBytes);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.uid = new BigInteger("12345678901234567890"); // Set UID
        x7875.gid = new BigInteger("98765432109876543210"); // Set GID

        String expected = "0x7875 Zip Extra Field: UID=12345678901234567890 GID=98765432109876543210";
        String actual = x7875.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        X7875_NewUnix original = new X7875_NewUnix();
        original.uid = new BigInteger("12345678901234567890"); // Set UID
        original.gid = new BigInteger("98765432109876543210"); // Set GID

        X7875_NewUnix cloned = (X7875_NewUnix) original.clone();

        Assert.assertEquals(original.uid, cloned.uid);
        Assert.assertEquals(original.gid, cloned.gid);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();

        x7875_1.uid = new BigInteger("12345678901234567890"); // Set UID_1
        x7875_1.gid = new BigInteger("98765432109876543210"); // Set GID_1

        x7875_2.uid = new BigInteger("12345678901234567890"); // Set UID_2
        x7875_2.gid = new BigInteger("98765432109876543210"); // Set GID_2

        X7875_NewUnix x7875_3 = new X7875_NewUnix();
        x7875_3.uid = new BigInteger("12345678901234567891"); // Set UID_3 (different)
        x7875_3.gid = new BigInteger("98765432109876543210"); // Set GID_3 (same)

        Assert.assertTrue(x7875_1.equals(x7875_2));
        Assert.assertFalse(x7875_1.equals(x7875_3));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();

        x7875_1.uid = new BigInteger("12345678901234567890"); // Set UID_1
        x7875_1.gid = new BigInteger("98765432109876543210"); // Set GID_1

        x7875_2.uid = new BigInteger("12345678901234567890"); // Set UID_2
        x7875_2.gid = new BigInteger("98765432109876543210"); // Set GID_2

        X7875_NewUnix x7875_3 = new X7875_NewUnix();
        x7875_3.uid = new BigInteger("12345678901234567891"); // Set UID_3 (different)
        x7875_3.gid = new BigInteger("98765432109876543210"); // Set GID_3 (same)

        int x7875_1_hashCode = x7875_1.hashCode();
        int x7875_2_hashCode = x7875_2.hashCode();
        int x7875_3_hashCode = x7875_3.hashCode();

        Assert.assertEquals(x7875_1_hashCode, x7875_2_hashCode);
        Assert.assertNotSame(x7875_1_hashCode, x7875_3_hashCode);
    }

    
}