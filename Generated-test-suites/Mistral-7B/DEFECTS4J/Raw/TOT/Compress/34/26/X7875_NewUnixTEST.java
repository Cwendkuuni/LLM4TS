import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnixWithValidUidAndGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 1001;
        long gid = 1002;

        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);

        Assertions.assertEquals(uid, x7875NewUnix.getUID());
        Assertions.assertEquals(gid, x7875NewUnix.getGID());

        ZipShort expectedLocalFileDataLength = new ZipShort(3 + Long.BYTES * 2);
        Assertions.assertEquals(expectedLocalFileDataLength, x7875NewUnix.getLocalFileDataLength());

        byte[] localFileDataData = x7875NewUnix.getLocalFileDataData();

        Assertions.assertEquals(3, localFileDataData.length);
        Assertions.assertEquals(x7875NewUnix.getVersion(), localFileDataData[0]);
        Assertions.assertEquals(x7875NewUnix.getUidSize(), localFileDataData[1]);
        Assertions.assertEquals(x7875NewUnix.getGidSize(), localFileDataData[2]);

        byte[] uidBytes = ZipUtil.longToByteArray(uid);
        byte[] gidBytes = ZipUtil.longToByteArray(gid);

        int pos = 0;
        for (int i = 0; i < x7875NewUnix.getUidSize(); i++) {
            Assertions.assertEquals(uidBytes[i], localFileDataData[pos++]);
        }

        for (int i = 0; i < x7875NewUnix.getGidSize(); i++) {
            Assertions.assertEquals(gidBytes[i], localFileDataData[pos++]);
        }
    }

    @Test
    public void testNewX7875_NewUnixWithMinimumLengthUidAndGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        x7875NewUnix.setUID(1);
        x7875NewUnix.setGID(1);

        Assertions.assertEquals(1, x7875NewUnix.getUID());
        Assertions.assertEquals(1, x7875NewUnix.getGID());

        ZipShort expectedLocalFileDataLength = new ZipShort(3);
        Assertions.assertEquals(expectedLocalFileDataLength, x7875NewUnix.getLocalFileDataLength());

        byte[] localFileDataData = x7875NewUnix.getLocalFileDataData();

        Assertions.assertEquals(3, localFileDataData.length);
        Assertions.assertEquals(x7875NewUnix.getVersion(), localFileDataData[0]);
        Assertions.assertEquals(1, localFileDataData[1]);
        Assertions.assertEquals(1, localFileDataData[2]);
    }

    @Test
    public void testNewX7875_NewUnixWithNegativeUidAndGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        x7875NewUnix.setUID(-1001);
        x7875NewUnix.setGID(-1002);

        Assertions.assertThrows(ZipException.class, () -> {
            x7875NewUnix.parseFromLocalFileData(x7875NewUnix.getLocalFileDataData(), 0, x78785NewUnix.getLocalFileDataLength().getValue());
        });
    }

    @Test
    public void testNewX7875_NewUnixWithZeroUidAndGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        x7875NewUnix.setUID(0);
        x7875NewUnix.setGID(0);

        Assertions.assertEquals(0, x7875NewUnix.getUID());
        Assertions.assertEquals(0, x7875NewUnix.getGID());

        ZipShort expectedLocalFileDataLength = new ZipShort(3);
        Assertions.assertEquals(expectedLocalFileDataLength, x7875NewUnix.getLocalFileDataLength());

        byte[] localFileDataData = x7875NewUnix.getLocalFileDataData();

        Assertions.assertEquals(3, localFileDataData.length);
        Assertions.assertEquals(x7875NewUnix.getVersion(), localFileDataData[0]);
        Assertions.assertEquals(0, localFileDataData[1]);
        Assertions.assertEquals(0, localFileDataData[2]);
    }

    @Test
    public void testNewX7875_NewUnixWithUidLargerThanMaxInteger() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        long maxLong = Long.MAX_VALUE + 1;
        x7875NewUnix.setUID(maxLong);

        Assertions.assertEquals(maxLong, x7875NewUnix.getUID());

        ZipShort expectedLocalFileDataLength = new ZipShort(Long.BYTES * 2 + 3);
        Assertions.assertEquals(expectedLocalFileDataLength, x7875NewUnix.getLocalFileDataLength());

        byte[] localFileDataData = x7875NewUnix.getLocalFileDataData();

        Assertions.assertEquals(localFileDataData.length, expectedLocalFileDataLength.getValue());

        byte[] uidBytes = ZipUtil.bigToByteArray(x7875NewUnix.getUID());

        int pos = 0;
        for (int i = 0; i < uidBytes.length; i++) {
            Assertions.assertEquals(uidBytes[i], localFileDataData[pos++]);
        }
    }

    @Test
    public void testNewX7875_NewUnixWithGidLargerThanMaxInteger() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        long maxLong = Long.MAX_VALUE + 1;
        x7875NewUnix.setGID(maxLong);

        Assertions.assertEquals(maxLong, x7875NewUnix.getGID());

        ZipShort expectedLocalFileDataLength = new ZipShort(Long.BYTES * 2 + 3);
        Assertions.assertEquals(expectedLocalFileDataLength, x7875NewUnix.getLocalFileDataLength());

        byte[] localFileDataData = x7875NewUnix.getLocalFileDataData();

        Assertions.assertEquals(localFileDataData.length, expectedLocalFileDataLength.getValue());

        byte[] gidBytes = ZipUtil.bigToByteArray(x7875NewUnix.getGID());

        int pos = 0;
        for (int i = 0; i < gidBytes.length; i++) {
            Assertions.assertEquals(gidBytes[i], localFileDataData[pos++]);
        }
    }

    @Test
    public void testNewX7875_NewUnixWithZeroLengthLocalFileData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        ZipShort expectedLocalFileDataLength = new ZipShort(0);
        Assertions.assertEquals(expectedLocalFileDataLength, x7875NewUnix.getLocalFileDataLength());

        Assertions.assertArrayEquals(new byte[0], x7875NewUnix.getLocalFileDataData());

        Assertions.assertThrows(ZipException.class, () -> {
            x7875NewUnix.parseFromLocalFileData(x7875NewUnix.getLocalFileDataData(), 0, x7875NewUnix.getLocalFileDataLength().getValue());
        });
    }
}