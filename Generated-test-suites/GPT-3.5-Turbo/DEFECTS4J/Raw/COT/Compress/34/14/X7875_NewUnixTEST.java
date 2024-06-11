
import org.junit.Test;
import java.math.BigInteger;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix field = new X7875_NewUnix();
        ZipShort headerId = field.getHeaderId();
        assert headerId.equals(new ZipShort(0x7875));
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(123);
        long uid = field.getUID();
        assert uid == 123;
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setGID(456);
        long gid = field.getGID();
        assert gid == 456;
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(123);
        assert field.getUID() == 123;
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setGID(456);
        assert field.getGID() == 456;
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix field = new X7875_NewUnix();
        ZipShort localFileDataLength = field.getLocalFileDataLength();
        assert localFileDataLength.equals(new ZipShort(3));
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix field = new X7875_NewUnix();
        ZipShort centralDirectoryLength = field.getCentralDirectoryLength();
        assert centralDirectoryLength.equals(new ZipShort(3));
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(123456);
        field.setGID(7890);
        byte[] localFileDataData = field.getLocalFileDataData();
        byte[] expectedData = new byte[]{0x01, 0x03, 0x40, (byte) 0xE2, 0x01, 0x50, 0x02};
        assert localFileDataData.equals(expectedData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix field = new X7875_NewUnix();
        byte[] centralDirectoryData = field.getCentralDirectoryData();
        byte[] expectedData = new byte[]{};
        assert centralDirectoryData.equals(expectedData);
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix field = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, 0x03, 0x40, (byte) 0xE2, 0x01, 0x50, 0x02};
        field.parseFromLocalFileData(data, 0, data.length);
        assert field.getUID() == 123456;
        assert field.getGID() == 7890;
    }

    @Test
    public void testEquals() {
        X7875_NewUnix field1 = new X7875_NewUnix();
        field1.setUID(123);
        field1.setGID(456);

        X7875_NewUnix field2 = new X7875_NewUnix();
        field2.setUID(123);
        field2.setGID(456);

        assert field1.equals(field2);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(123);
        field.setGID(456);
        assert field.hashCode() == field.hashCode();
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        X7875_NewUnix field = new X7875_NewUnix();
        byte[] array = new byte[]{0x00, 0x01, 0x02, 0x00, 0x00};
        byte[] trimmedArray = field.trimLeadingZeroesForceMinLength(array);
        byte[] expectedArray = new byte[]{0x01, 0x02};
        assert trimmedArray.equals(expectedArray);
    }
}
