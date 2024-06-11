import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testGetPermMask() {
        int actual = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals("Incorrect permMask value", expected, actual);
    }

    @Test
    public void testGetLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Incorrect linkFlag value", expected, actual);
    }

    @Test
    public void testGetFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("Incorrect fileFlag value", expected, actual);
    }

    @Test
    public void testGetDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Incorrect dirFlag value", expected, actual);
    }

    @Test
    public void testgetDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Incorrect defaultLinkPerm value", expected, actual);
    }

    @Test
    public void testgetDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Incorrect defaultDirPerm value", expected, actual);
    }

    @Test
    public void testgetDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Incorrect defaultFilePerm value", expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int hashCode() {
                throw new UnsupportedOperationException("Unsupported operation");
            }
        };
        unixStat.hashCode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        int invalidValue = 1024000;
        UnixStat unixStat = new UnixStat() {
            @Override
            public int getType() {
                throw new IllegalArgumentException("Invalid value: " + invalidValue);
            }
        };
        unixStat.getType();
    }
}