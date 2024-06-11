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

    // Edge cases and exception handling tests

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        // Unsupported method test
        UnixStat instance = null; // Assuming the UnixStat is not instantiatable
        instance.unsupportedMethod();
    }

    @Test
    public void testNegativePermissionValues() {
        for (int permission : new int[]{-1, -10, -100}) {
            int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM | permission;
            int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM | permission;
            int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM | permission;

            int linkPerm = defaultLinkPerm & UnixStat.PERM_MASK;
            int dirPerm = defaultDirPerm & UnixStat.PERM_MASK;
            int filePerm = defaultFilePerm & UnixStat.PERM_MASK;

            // Check if the calculated permission values are still valid
            assertTrue("Invalid link permission value: " + linkPerm, linkPerm >= 0);
            assertTrue("Invalid directory permission value: " + dirPerm, dirPerm >= 0);
            assertTrue("Invalid file permission value: " + filePerm, filePerm >= 0);
        }
    }
}