import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPerMMask() {
        int actual = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals("PERM_MASK should be equal to 07777", expected, actual);
    }

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("LINK_FLAG should be equal to 0120000", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("FILE_FLAG should be equal to 0100000", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("DIR_FLAG should be equal to 040000", expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("DEFAULT_LINK_PERM should be equal to 0777", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("DEFAULT_DIR_PERM should be equal to 0755", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("DEFAULT_FILE_PERM should be equal to 0644", expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int hashCode() {
                throw new UnsupportedOperationException();
            }
        };
        unixStat.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        UnixStat unixStat = null;
        unixStat.DEFAULT_LINK_PERM;
    }

    @Test
    public void testNegativePermissions() {
        int actual = UnixStat.PERM_MASK & -1;
        int expected = 07777 & 0xFFFFFF80;
        assertEquals("PERM_MASK with negative permissions should be equal to 01000000000", expected, actual);
    }
}