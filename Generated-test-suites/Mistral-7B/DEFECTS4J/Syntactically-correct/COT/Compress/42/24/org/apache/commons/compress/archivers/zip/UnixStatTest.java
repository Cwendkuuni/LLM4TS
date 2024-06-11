import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int linkFlag = UnixStat.LINK_FLAG;
        assertTrue(linkFlag > 0 && (linkFlag & UnixStat.PERM_MASK) == 0);
    }

    @Test
    public void testFileFlag() {
        int fileFlag = UnixStat.FILE_FLAG;
        assertTrue(fileFlag > 0 && (fileFlag & UnixStat.PERM_MASK) == 0);
    }

    @Test
    public void testDirFlag() {
        int dirFlag = UnixStat.DIR_FLAG;
        assertTrue(dirFlag > 0 && (dirFlag & UnixStat.PERM_MASK) == UnixStat.PERM_MASK);
    }

    @Test(expected = AssertionError.class)
    public void testNegativePermissions() {
        int invalidPermissions = -1;
        int validPermissions = UnixStat.PERM_MASK;
        try {
            int combinedPermissions = validPermissions | invalidPermissions;
            fail("Expected AssertionError but no exception was thrown.");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testDefaultLinkPerm() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals(defaultLinkPerm, 0777);
    }

    @Test
    public void testDefaultDirPerm() {
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals(defaultDirPerm, 0755);
    }

    @Test
    public void testDefaultFilePerm() {
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals(defaultFilePerm, 0644);
    }
}