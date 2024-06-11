import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnixWithValidUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);

        Assert.assertEquals(ZipShort.ZERO, x7875.getHeaderId());
        Assert.assertEquals(new ZipShort((short) 3 + x7875.getUidSize() + x7875.getGidSize()), x7875.getLocalFileDataLength());
        Assert.assertEquals((byte) 0x78, x7875.getLocalFileDataData()[0]);
        Assert.assertEquals((byte) 0x75, x7875.getLocalFileDataData()[1]);
        Assert.assertEquals((byte) 0x01, x7875.getLocalFileDataData()[2]);
        Assert.assertEquals(x7875.getUidSize(), x7875.getLocalFileDataData()[3]);
        Assert.assertArrayEquals(new byte[x7875.getUidSize()], x7875.getLocalFileDataData(), 3, 3 + x7875.getUidSize());
        Assert.assertEquals((byte) 0x01, x7875.getLocalFileDataData()[3 + x7875.getUidSize()]);
        Assert.assertEquals(x7875.getGidSize(), x7875.getLocalFileDataData()[4 + x7875.getUidSize()]);
        Assert.assertArrayEquals(new byte[x7875.getGidSize()], x7875.getLocalFileDataData(), 5 + x7875.getUidSize(), 5 + x7875.getUidSize() + x7875.getGidSize());

        X7875_NewUnix clonedX7875 = null;
        try {
            clonedX7875 = (X7875_NewUnix) x7875.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(clonedX7875);
        Assert.assertEquals(x7875.getUID(), clonedX7875.getUID());
        Assert.assertEquals(x7875.getGID(), clonedX7875.getGID());

        // Test equals and hashcode
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        x7875_1.setUID(123456789);
        x7875_1.setGID(987654321);
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        x7875_2.setUID(123456789);
        x7875_2.setGID(987654321);
        X7875_NewUnix x7875_3 = new X7875_NewUnix();
        x7875_3.setUID(123456788);
        x7875_3.setGID(987654321);
        Assert.assertTrue(x7875.equals(x7875_1));
        Assert.assertTrue(x7875.equals(clonedX7875));
        Assert.assertFalse(x7875.equals(x7875_2));
        Assert.assertFalse(x7875.equals(x7875_3));

        Assert.assertEquals(x7875.hashCode(), x7875_1.hashCode());
        Assert.assertEquals(x7875.hashCode(), clonedX7875.hashCode());
        Assert.assertNotEquals(x7875.hashCode(), x7875_2.hashCode());
        Assert.assertNotEquals(x7875.hashCode(), x7875_3.hashCode());

        // Test parsing from local file data
        byte[] localFileData = x7875.getLocalFileDataData();
        X7875_NewUnix parsedX7875 = new X7875_NewUnix();
        try {
            parsedX7875.parseFromLocalFileData(localFileData, 0, localFileData.length);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(x7875.getUID(), parsedX7875.getUID());
        Assert.assertEquals(x7875.getGID(), parsedX7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testNewX7875_NewUnixWithInvalidUIDOrGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-1);
        x7875.setGID(-1);
    }
}