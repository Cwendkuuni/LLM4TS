import org.apache.commons.compress.archivers.zip.UnixStat;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int actual = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals("Incorrect value for PERM_MASK", expected, actual);
    }

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Incorrect value for LINK_FLAG", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("Incorrect value for FILE_FLAG", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Incorrect value for DIR_FLAG", expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Incorrect value for DEFAULT_LINK_PERM", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Incorrect value for DEFAULT_DIR_PERM", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Incorrect value for DEFAULT_FILE_PERM", expected, actual);
    }

    // Test for edge cases and exceptions

    @Test(expected = IllegalStateException.class)
    public void testNegativePermMask() {
        int permMask = UnixStat.PERM_MASK - 1; // make it negative
        UnixStat.checkPermission(permMask);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeDefaultLinkPerm() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM - 1; // make it negative
        UnixStat.checkPermission(defaultLinkPerm);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeDefaultDirPerm() {
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM - 1; // make it negative
        UnixStat.checkPermission(defaultDirPerm);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeDefaultFilePerm() {
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM - 1; // make it negative
        UnixStat.checkPermission(defaultFilePerm);
    }
}