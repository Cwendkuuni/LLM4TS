package org.apache.commons.compress.archivers.zip; // Import necessary packages

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {
    // 

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedHeaderId = new ZipShort(0x7875);
        ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
        Assert.assertEquals("Incorrect header id", expectedHeaderId, actualHeaderId);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedUID = 1001;
        x7875NewUnix.setUID(expectedUID);
        long actualUID = x7875NewUnix.getUID();
        Assert.assertEquals("Incorrect UID", expectedUID, actualUID);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long newUID = 1001;
        x7875NewUnix.setUID(newUID);
        long actualUID = x7875NewUnix.getUID();
        Assert.assertEquals("Incorrect UID after setting", newUID, actualUID);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedGID = 1001;
        x7875NewUnix.setGID(expectedGID);
        long actualGID = x7875NewUnix.getGID();
        Assert.assertEquals("Incorrect GID", expectedGID, actualGID);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long newGID = 1001;
        x7875NewUnix.setGID(newGID);
        long actualGID = x7875NewUnix.getGID();
        Assert.assertEquals("Incorrect GID after setting", newGID, actualGID);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int expectedLength = 7; // Based on the class description
        int actualLength = x7875NewUnix.getLocalFileDataLength().getValue();
        Assert.assertEquals("Incorrect length for local file data", expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int expectedLength = 7; // Based on the class description
        int actualLength = x7875NewUnix.getCentralDirectoryLength().getValue();
        Assert.assertEquals("Incorrect length for central directory data", expectedLength, actualLength);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] inputData = new byte[]{0x55, 0x78, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01};
        int offset = 0;
        int length = inputData.length;
        x7875NewUnix.parseFromLocalFileData(inputData, offset, length);

        long expectedUID = 1;
        long expectedGID = 1;
        int expectedVersion = 1;

        long actualUID = x7875NewUnix.getUID();
        long actualGID = x7875NewUnix.getGID();
        int actualVersion = x7875NewUnix.getVersion();

        Assert.assertEquals("Incorrect UID after parsing", expectedUID, actualUID);
        Assert.assertEquals("Incorrect GID after parsing", expectedGID, actualGID);
        Assert.assertEquals("Incorrect version after parsing", expectedVersion, actualVersion);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] inputData = new byte[]{0x55, 0x78, (byte)0x80, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01};
        int offset = 0;
        int length = inputData.length;
        x7875NewUnix.parseFromLocalFileData(inputData, offset, length);
    }

    // 
}