import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class X7875_NewUnixTest {

    private X7875_NewUnix x7875NewUnix;
    private BigInteger uid;
    private BigInteger gid;

    public X7875_NewUnixTest(BigInteger uid, BigInteger gid) {
        this.uid = uid;
        this.gid = gid;
    }

    @Parameterized.Parameters(name = "{index}: uid={0}, gid={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {BigInteger.valueOf(1000), BigInteger.valueOf(1001)},
                {BigInteger.valueOf(2000), BigInteger.valueOf(2001)},
                {BigInteger.valueOf(3000), BigInteger.valueOf(3001)}
        });
    }

    @Test
    public void testConstructor() {
        x7875NewUnix = new X7875_NewUnix();
        assertNotNull(x7875NewUnix.getHeaderId());
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(BigInteger.ZERO, x7875NewUnix.getUID());
        assertEquals(BigInteger.ZERO, x7875NewUnix.getGID());
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
    }

    @Test
    public void testGetVersion() {
        assertEquals(1, x7875NewUnix.getVersion());
    }

    @Test
    public void testSetUidAndGetUid() {
        x7875NewUnix.setUID(uid);
        assertEquals(uid, x7875NewUnix.getUID());
    }

    @Test
    public void testSetGidAndGetGid() {
        x7875NewUnix.setGID(gid);
        assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        int uidSize = TrimLeadingZeroesForceMinLength(uid.toByteArray()).length;
        int gidSize = TrimLeadingZeroesForceMinLength(gid.toByteArray()).length;
        ZipShort length = new ZipShort(3 + uidSize + gidSize);
        assertEquals(length, x7875NewUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(getLocalFileDataLength(), x7875NewUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() throws IOException {
        byte[] expectedData = new byte[getLocalFileDataLength().getValue()];

        int pos = 0;
        expectedData[pos++] = (byte) unsignedIntToSignedByte(getVersion());
        expectedData[pos++] = (byte) unsignedIntToSignedByte(uid.toByteArray().length);
        System.arraycopy(uid.toByteArray(), 0, expectedData, pos, uid.toByteArray().length);
        pos += uid.toByteArray().length;
        expectedData[pos++] = (byte) unsignedIntToSignedByte(gid.toByteArray().length);
        System.arraycopy(gid.toByteArray(), 0, expectedData, pos, gid.toByteArray().length);

        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        assertEquals(expectedData, actualData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryData() throws IOException {
        x7875NewUnix.parseFromCentralDirectoryData(new byte[0], 0, 0);
    }

    @Test
    public void testParseFromLocalFileData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = x7875NewUnix.getLocalFileDataData();
        outputStream.write(data, 0, data.length);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(x7875NewUnix.getUID(), uid);
        assertEquals(x7875NewUnix.getGID(), gid);
    }

    @Test
    public void testReset() {
        x7875NewUnix.reset();
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(BigInteger.ZERO, x7875NewUnix.getUID());
        assertEquals(BigInteger.ZERO, x7875NewUnix.getGID());
    }

    @Test
    public void testToString() {
        assertEquals("0x7875 Zip Extra Field: UID=" + uid + " GID=" + gid, x7875NewUnix.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        x7875NewUnix2.setUID(uid);
        x7875NewUnix2.setGID(gid);

        assertEquals(x7875NewUnix, x7875NewUnix2);
        assertEquals(x7875NewUnix.hashCode(), x7875NewUnix2.hashCode());

        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        x7875NewUnix3.setUID(BigInteger.valueOf(1111));
        x7875NewUnix3.setGID(BigInteger.valueOf(2222));

        assertNotEquals(x7875NewUnix, x7875NewUnix3);
        assertNotEquals(x7875NewUnix.hashCode(), x7875NewUnix3.hashCode());
    }

    private static int unsignedIntToSignedByte(int value) {
        return (value & 0xFF);
    }

    private static int trimLeadingZeroesForceMinLength(byte[] array) {
        return Arrays.stream(array)
                .filter(b -> b != 0)
                .mapToInt(i -> i)
                .sum();
    }

    private static ZipShort getLocalFileDataLength() {
        int uidSize = TrimLeadingZeroesForceMinLength(uid.toByteArray()).length;
        int gidSize = TrimLeadingZeroesForceMinLength(gid.toByteArray()).length;
        return new ZipShort(3 + uidSize + gidSize);
    }
}