import org.junit.Before;
import org.junit.Test;
import java.util.zip.ZipException;

public class X7875_NewUnixTest {

    private X7875_NewUnix x7875_newUnix;

    @Before
    public void setUp() {
        x7875_newUnix = new X7875_NewUnix();
    }

    @Test
    public void testDefaultValues() {
        assert(x7875_newUnix.getHeaderId().getValue() == 0x7875);
        assert(x7875_newUnix.getUID() == 1000);
        assert(x7875_newUnix.getGID() == 1000);
        assert(x7875_newUnix.getLocalFileDataLength().getValue() == 5);
        assert(x7875_newUnix.getCentralDirectoryLength().getValue() == 5);
        assert(x7875_newUnix.getCentralDirectoryData().length == 0);
    }

    @Test
    public void testSetUID() {
        long uid = 123;
        x7875_newUnix.setUID(uid);
        assert(x7875_newUnix.getUID() == uid);
    }

    @Test
    public void testSetGID() {
        long gid = 456;
        x7875_newUnix.setGID(gid);
        assert(x7875_newUnix.getGID() == gid);
    }

    @Test
    public void testLocalFileDataData() {
        byte[] expectedData = {1, 1, 1, 1, 1};
        x7875_newUnix.setUID(1);
        x7875_newUnix.setGID(1);
        byte[] data = x7875_newUnix.getLocalFileDataData();
        assert(data.length == 5);
        for (int i = 0; i < data.length; i++) {
            assert(data[i] == expectedData[i]);
        }
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] inputData = {1, 2, 3, 4, 5};
        x7875_newUnix.parseFromLocalFileData(inputData, 0, inputData.length);
        assert(x7875_newUnix.getUID() == 2);
        assert(x7875_newUnix.getGID() == 3);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] inputData = {1, 2, 3, 4, 5};
        x7875_newUnix.parseFromCentralDirectoryData(inputData, 0, inputData.length);
        assert(x7875_newUnix.getUID() == 1000);
        assert(x7875_newUnix.getGID() == 1000);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] inputArray1 = {0, 1, 2, 3};
        byte[] trimmedArray1 = X7875_NewUnix.trimLeadingZeroesForceMinLength(inputArray1);
        assert(trimmedArray1.length == 4);
        assert(trimmedArray1[0] == 0);
        assert(trimmedArray1[1] == 1);
        assert(trimmedArray1[2] == 2);
        assert(trimmedArray1[3] == 3);

        byte[] inputArray2 = {0, 0, 1, 2, 3, 0};
        byte[] trimmedArray2 = X7875_NewUnix.trimLeadingZeroesForceMinLength(inputArray2);
        assert(trimmedArray2.length == 6);
        assert(trimmedArray2[0] == 1);
        assert(trimmedArray2[1] == 2);
        assert(trimmedArray2[2] == 3);
        assert(trimmedArray2[3] == 0);
        assert(trimmedArray2[4] == 0);
        assert(trimmedArray2[5] == 0);
    }

    @Test
    public void testEquals() throws CloneNotSupportedException {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = new X7875_NewUnix();
        assert(xf1.equals(xf1));
        assert(xf1.equals(xf2));
        xf1.setUID(1);
        assert(!xf1.equals(xf2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = new X7875_NewUnix();
        assert(xf1.hashCode() == xf2.hashCode());
        xf1.setUID(1);
        assert(xf1.hashCode() != xf2.hashCode());
    }

}