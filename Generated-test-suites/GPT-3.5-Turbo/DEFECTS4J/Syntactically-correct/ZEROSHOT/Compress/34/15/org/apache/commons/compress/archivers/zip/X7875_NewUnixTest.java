package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(123);
        assertEquals(123, x7875_NewUnix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(456);
        assertEquals(456, x7875_NewUnix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(123);
        assertEquals(123, x7875_NewUnix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(456);
        assertEquals(456, x7875_NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(123);
        x7875_NewUnix.setGID(456);
        byte[] data = x7875_NewUnix.getLocalFileDataData();
        ZipShort length = x7875_NewUnix.getLocalFileDataLength();
        assertEquals(length.getValue(), data.length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        ZipShort length = x7875_NewUnix.getCentralDirectoryLength();
        assertEquals(3, length.getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(123);
        x7875_NewUnix.setGID(456);
        byte[] data = x7875_NewUnix.getLocalFileDataData();
        assertEquals(123, ZipUtil.byteToUnsignedInt(data[2]));
        assertEquals(456, ZipUtil.byteToUnsignedInt(data[4]));
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = x7875_NewUnix.getCentralDirectoryData();
        assertEquals(data.length, 0);
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(123);
        x7875_NewUnix.setGID(456);
        byte[] data = x7875_NewUnix.getLocalFileDataData();
        X7875_NewUnix newData = new X7875_NewUnix();
        newData.parseFromLocalFileData(data, 0, data.length);
        assertEquals(x7875_NewUnix, newData);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = x7875_NewUnix.getCentralDirectoryData();
        X7875_NewUnix newData = new X7875_NewUnix();
        newData.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(x7875_NewUnix, newData);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875_NewUnix2 = new X7875_NewUnix();
        assertTrue(x7875_NewUnix1.equals(x7875_NewUnix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875_NewUnix2 = new X7875_NewUnix();
        assertEquals(x7875_NewUnix1.hashCode(), x7875_NewUnix2.hashCode());
    }

}