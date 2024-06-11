import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipException;

import static org.junit.Assert.assertEquals;

public class X7875_NewUnixTest {

    private X7875_NewUnix x7875;

    @Before
    public void setUp() {
        x7875 = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
    }

    @Test
    public void testGetUID() {
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    @Test
    public void testGetGID() {
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testSetUID() {
        x7875.setUID(2000);
        assertEquals(2000, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        x7875.setGID(2000);
        assertEquals(2000, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        int expectedLength = 3 + X7875_NewUnix.trimLeadingZeroesForceMinLength(X7875_NewUnix.ONE_THOUSAND.toByteArray()).length +
                X7875_NewUnix.trimLeadingZeroesForceMinLength(X7875_NewUnix.ONE_THOUSAND.toByteArray()).length;
        assertEquals(expectedLength, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(x7875.getLocalFileDataLength(), x7875.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expectedData = new byte[3 + X7875_NewUnix.trimLeadingZeroesForceMinLength(X7875_NewUnix.ONE_THOUSAND.toByteArray()).length +
                X7875_NewUnix.trimLeadingZeroesForceMinLength(X7875_NewUnix.ONE_THOUSAND.toByteArray()).length];
        expectedData[0] = (byte) X7875_NewUnix.unsignedIntToSignedByte(1);
        expectedData[1] = (byte) X7875_NewUnix.unsignedIntToSignedByte(X7875_NewUnix.trimLeadingZeroesForceMinLength(X7875_NewUnix.ONE_THOUSAND.toByteArray()).length);
        expectedData[expectedData.length - 1] = (byte) X7875_NewUnix.unsignedIntToSignedByte(X7875_NewUnix.trimLeadingZeroesForceMinLength(X7875_NewUnix.ONE_THOUSAND.toByteArray()).length);
        assertEquals(expectedData, x7875.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertEquals(new byte[0], x7875.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[10];
        int offset = 0;
        int length = data.length;
        x7875.parseFromLocalFileData(data, offset, length);
        assertEquals(1, x7875.getVersion());
        assertEquals(X7875_NewUnix.ONE_THOUSAND, x7875.getUID());
        assertEquals(X7875_NewUnix.ONE_THOUSAND, x7875.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[10];
        int offset = 0;
        int length = data.length;
        x7875.parseFromCentralDirectoryData(data, offset, length);
        // do assertion here
    }

    @Test
    public void testToString() {
        String expectedString = "0x7875 Zip Extra Field: UID=" + x7875.getUID() + " GID=" + x7875.getGID();
        assertEquals(expectedString, x7875.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix equalX7875 = new X7875_NewUnix();
        equalX7875.setUID(1000);
        equalX7875.setGID(1000);
        assertEquals(equalX7875, x7875);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = -1234567 * x7875.getVersion();
        expectedHashCode ^= Integer.rotateLeft(x7875.getUID().hashCode(), 16);
        expectedHashCode ^= x7875.getGID().hashCode();
        assertEquals(expectedHashCode, x7875.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        assertEquals(new byte[] {1}, X7875_NewUnix.trimLeadingZeroesForceMinLength(new byte[] {0, 0, 0, 0, 1}));
    }
}