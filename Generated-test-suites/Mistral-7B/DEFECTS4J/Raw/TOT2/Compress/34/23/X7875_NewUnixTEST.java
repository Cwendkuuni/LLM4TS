import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(0x7875, headerId.getValue());
    }

    @Test
    public void testGetUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(9876543210L);

        long uid = x7875.getUID();
        long gid = x7875.getGID();

        assertEquals(1234567890L, uid);
        assertEquals(9876543210L, gid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUIDAndNegativeNumber() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGIDAndNegativeNumber() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(-1L);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 5;
        int gidSize = 5;
        x7875.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(3 + uidSize + gidSize, length.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        ZipShort length = x7875.getCentralDirectoryLength();
        assertEquals(x7875.getLocalFileDataLength().getValue(), length.getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        byte[] data = x7875.getLocalFileDataData();

        assertNotNull(data);
        assertEquals(3 + 5 + 5, data.length);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] testData = new byte[]{(byte) 0x01, (byte) 0x05, 0x00, 0x00, 0x05, 0x00, 0x00, 0x05, 0x00, 0x00, 0x05, 0x00, 0x00, 0x05};

        x7875.parseFromLocalFileData(testData, 0, testData.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}), x7875.getUID());
        assertEquals(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}), x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] testData = new byte[]{(byte) 0x01, (byte) 0x03, 0x00, 0x00, 0x05, 0x00, 0x00, 0x05};

        x7875.parseFromLocalFileData(testData, 0, testData.length);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(new byte[]{0x01, 0x05, 0x00, 0x00, 0x05, 0x00, 0x00, 0x05}, 0, 8);

        x7875.reset();

        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
        assertEquals(1, x7875.getVersion());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        String toString = x7875.toString();
        assertEquals("0x7875 Zip Extra Field: UID=51205 GID=51205", toString);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        X7875_NewUnix clone = (X7875_NewUnix) x7875.clone();

        assertEquals(x7875.getUID(), clone.getUID());
        assertEquals(x7875.getGID(), clone.getGID());
        assertEquals(x7875.getVersion(), clone.getVersion());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        X7875_NewUnix x7875Copy = new X7875_NewUnix();
        x7875.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875Copy.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875Copy.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        assertTrue(x7875.equals(x7875Copy));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        int x7875HashCode = x7875.hashCode();

        X7875_NewUnix x7875Copy = new X7875_NewUnix();
        x7875Copy.setUID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));
        x7875Copy.setGID(new BigInteger(new byte[]{(byte) 0x05, 0x00, 0x00, 0x00, 0x05}));

        int x7875CopyHashCode = x7875Copy.hashCode();

        assertEquals(x7875HashCode, x7875CopyHashCode);
    }
}