package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testCreateX7875_NewUnixWithValidValues() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789L);
        x7875.setGID(234567890L);

        Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        Assert.assertEquals(Long.toUnsignedLong(123456789L), x7875.getUID());
        Assert.assertEquals(Long.toUnsignedLong(234567890L), x7875.getGID());
        Assert.assertEquals(new ZipShort(3 + 8 + 8), x7875.getLocalFileDataLength());
        Assert.assertArrayEquals(new byte[]{0x78, 0x75, 0x00, 0x01, 0x08, 0x00, 0x56, 0x34, 0x21, 0x12, 0x00, 0x08, 0x00, 0x45, 0x23, 0x49, 0x00, 0x56, 0x27, 0x89, 0x09, 0x00}, x7875.getLocalFileDataData());
    }

    @Test
    public void testCreateX7875_NewUnixWithMinimumLengthValues() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(0L);
        x7875.setGID(0L);

        Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        Assert.assertEquals(Long.toUnsignedLong(0L), x7875.getUID());
        Assert.assertEquals(Long.toUnsignedLong(0L), x7875.getGID());
        Assert.assertEquals(new ZipShort(1), x7875.getLocalFileDataLength());
        Assert.assertArrayEquals(new byte[]{0x78, 0x75}, x7875.getLocalFileDataData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateX7875_NewUnixWithNegativeUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-123456789L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateX7875_NewUnixWithNegativeGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(-234567890L);
    }

    @Test
    public void testParseX7875_NewUnixFromValidLocalFileData() {
        byte[] localFileData = new byte[]{0x78, 0x75, 0x00, 0x01, 0x08, 0x00, 0x56, 0x34, 0x21, 0x12, 0x00, 0x08, 0x00, 0x45, 0x23, 0x49, 0x00, 0x56, 0x27, 0x89, 0x09, 0x00};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(localFileData, 0, localFileData.length);

        Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        Assert.assertEquals(Long.toUnsignedLong(123456789L), x7875.getUID());
        Assert.assertEquals(Long.toUnsignedLong(234567890L), x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseX7875_NewUnixFromInvalidLocalFileData() {
        byte[] localFileData = new byte[]{0x78, 0x75, 0x00, 0x01, 0x08, 0x00, 0x56, 0x34, 0x21, 0x12, 0x00};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(localFileData, 0, localFileData.length);
    }

    @Test
    public void testResetX7875_NewUnix() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789L);
        x7875.setGID(234567890L);
        x7875.reset();

        Assert.assertEquals(Long.toUnsignedLong(1000L), x7875.getUID());
        Assert.assertEquals(Long.toUnsignedLong(1000L), x7875.getGID());
    }
}