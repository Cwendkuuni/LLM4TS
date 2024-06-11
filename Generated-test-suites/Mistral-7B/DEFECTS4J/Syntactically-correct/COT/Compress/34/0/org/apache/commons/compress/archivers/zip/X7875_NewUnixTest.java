package org.apache.commons.compress.archivers.zip; // Make sure the package is correct

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class X7875_NewUnixTest {

    

    @Test
    public void testDefaultConstructor() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        assertThat(xuf.getUID(), is(1000L));
        assertThat(xuf.getGID(), is(1000L));
        assertThat(xuf.version, is(1));
    }

    @Test
    public void testSetGetUid() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        long uid = 1234567;
        xuf.setUID(uid);
        assertThat(xuf.getUID(), is(uid));
    }

    @Test
    public void testSetGetGid() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        long gid = 2345678;
        xuf.setGID(gid);
        assertThat(xuf.getGID(), is(gid));
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.setUID(1234567);
        xuf.setGID(2345678);

        int uidSize = xuf.uid.toByteArray().length;
        int gidSize = xuf.gid.toByteArray().length;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = xuf.getLocalFileDataLength();

        assertThat(actualLength, is(expectedLength));
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.setUID(1234567);
        xuf.setGID(2345678);

        ZipShort expectedLength = xuf.getLocalFileDataLength();
        ZipShort actualLength = xuf.getCentralDirectoryLength();

        assertThat(actualLength, is(expectedLength));
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.setUID(1234567);
        xuf.setGID(2345678);

        byte[] expectedData = xuf.getLocalFileDataData();
        byte[] actualData = xuf.getLocalFileDataData();

        assertThat(actualData, is(expectedData));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCentralDirectoryData() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.getCentralDirectoryData();
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix xuf = new X7875_NewUnix();

        byte[] data = new byte[]{(byte) 0x01, 0x03, 0x00, 0x03, 0x00, 0x61, 0x00, 0x62, 0x00, 0x63, 0x00, 0x64, 0x00, 0x65, 0x00, 0x66, 0x00, 0x67};
        int offset = 0;
        int length = data.length;

        xuf.parseFromLocalFileData(data, offset, length);

        long uid = xuf.getUID();
        long gid = xuf.getGID();
        int version = xuf.version;

        assertThat(uid, is(1234567));
        assertThat(gid, is(1234567 + 1000)); // GID is UID + 1000 according to the spec
        assertThat(version, is(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix xuf = new X7875_NewUnix();

        byte[] buffer = new byte[0];
        int offset = 0;
        int length = buffer.length;

        xuf.parseFromCentralDirectoryData(buffer, offset, length);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.setUID(1234567);
        xuf.setGID(2345678);

        X7875_NewUnix clonedXuf = (X7875_NewUnix) xuf.clone();

        assertThat(clonedXuf.getUID(), is(xuf.getUID()));
        assertThat(clonedXuf.getGID(), is(xuf.getGID()));
        assertThat(clonedXuf.version, is(xuf.version));
    }

    @Test
    public void testEquals() {
        X7875_NewUnix xuf1 = new X7875_NewUnix();
        X7875_NewUnix xuf2 = new X7875_NewUnix();
        X7875_NewUnix xuf3 = new X7875_NewUnix();

        xuf1.setUID(1234567);
        xuf1.setGID(2345678);

        xuf2.setUID(1234566);
        xuf2.setGID(2345678);

        xuf3.setUID(1234567);
        xuf3.setGID(2345679);

        assertThat(xuf1, is(xuf1));
        assertThat(xuf1, is(xuf1)); // reflexive

        assertThat(xuf1, is(xuf1)); // symmetric

        assertThat(xuf1, is(xuf1)); // transitive

        assertThat(xuf1, is(xuf3)); // equals with different instance but same values

        assertThat(xuf1, is(nullValue())); // not equal to null

        assertThat(xuf1, is(new Object())); // not equal to an unrelated object

        assertThat(xuf1, is(xuf2)); // not equal to different values
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix xuf1 = new X7875_NewUnix();
        X7875_NewUnix xuf2 = new X7875_NewUnix();
        X7875_NewUnix xuf3 = new X7875_NewUnix();

        xuf1.setUID(1234567);
        xuf1.setGID(2345678);

        xuf2.setUID(1234566);
        xuf2.setGID(2345678);

        xuf3.setUID(1234567);
        xuf3.setGID(2345679);

        int xuf1HashCode = xuf1.hashCode();
        int xuf2HashCode = xuf2.hashCode();
        int xuf3HashCode = xuf3.hashCode();

        assertThat(xuf1.hashCode(), is(xuf1HashCode));
        assertThat(xuf1.hashCode(), is(xuf1.hashCode())); // reflexive

        assertThat(xuf1.hashCode(), is(xuf1.hashCode())); // consistent

        assertThat(xuf1.hashCode(), is(xuf3.hashCode())); // equals => hashcode

        assertThat(xuf1.hashCode(), is(xuf2.hashCode())); // not equal => different hashcodes
    }

    
}