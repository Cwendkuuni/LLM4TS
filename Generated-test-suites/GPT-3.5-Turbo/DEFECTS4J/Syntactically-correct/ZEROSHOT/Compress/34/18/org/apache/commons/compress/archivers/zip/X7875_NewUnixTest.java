import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    private X7875_NewUnix unix;

    @Before
    public void setUp() {
        unix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(0x7875, unix.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        unix.setUID(1000);
        assertEquals(1000, unix.getUID());
    }

    @Test
    public void testGetGID() {
        unix.setGID(1000);
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testSetUID() {
        unix.setUID(2000);
        assertEquals(2000, unix.getUID());
    }

    @Test
    public void testSetGID() {
        unix.setGID(2000);
        assertEquals(2000, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        unix.setUID(3000);
        unix.setGID(4000);
        byte[] data = unix.getLocalFileDataData();
        byte[] uidBytes = X7875_NewUnix.trimLeadingZeroesForceMinLength(ZipUtil.longToBig(3000).toByteArray());
        byte[] gidBytes = X7875_NewUnix.trimLeadingZeroesForceMinLength(ZipUtil.longToBig(4000).toByteArray());
        assertEquals(3 + uidBytes.length + gidBytes.length, data.length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        unix.setUID(1000);
        unix.setGID(2000);
        byte[] data = unix.getCentralDirectoryData();
        assertEquals(0, data.length);
    }

    @Test
    public void testGetLocalFileDataData() {
        unix.setUID(5000);
        unix.setGID(6000);
        byte[] data = unix.getLocalFileDataData();
        byte[] uidBytes = X7875_NewUnix.trimLeadingZeroesForceMinLength(ZipUtil.longToBig(5000).toByteArray());
        byte[] gidBytes = X7875_NewUnix.trimLeadingZeroesForceMinLength(ZipUtil.longToBig(6000).toByteArray());

        assertEquals(uidBytes.length, data[1]);
        assertEquals(uidBytes.length, data[4 + uidBytes.length]);
        for (int i = 0; i < uidBytes.length; i++) {
            assertEquals(uidBytes[i], data[3 + i]);
        }
        for (int i = 0; i < gidBytes.length; i++) {
            assertEquals(gidBytes[i], data[5 + uidBytes.length + i]);
        }
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] data = unix.getCentralDirectoryData();
        assertEquals(0, data.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{0x78, 0x75, 0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x02};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getVersion());
        assertEquals(1, unix.getUID());
        assertEquals(2, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] data = new byte[]{};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(1, unix.getVersion());
        assertEquals(1000, unix.getUID());
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(1234);
        other.setGID(5678);
        assertTrue(unix.equals(other));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(1234);
        other.setGID(5678);
        assertEquals(unix.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix clone = (X7875_NewUnix) unix.clone();
        assertEquals(unix, clone);
    }
}