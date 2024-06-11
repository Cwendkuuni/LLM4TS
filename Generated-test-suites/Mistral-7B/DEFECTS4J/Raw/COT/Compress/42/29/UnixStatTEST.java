import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        int expected = 07777;
        int actual = UnixStat.PERM_MASK;
        assertEquals("PERM_MASK should equal 07777", expected, actual);
    }

    @Test
    public void testLINK_FLAG() {
        int expected = 0120000;
        int actual = UnixStat.LINK_FLAG;
        assertEquals("LINK_FLAG should equal 0120000", expected, actual);
    }

    @Test
    public void testFILE_FLAG() {
        int expected = 0100000;
        int actual = UnixStat.FILE_FLAG;
        assertEquals("FILE_FLAG should equal 0100000", expected, actual);
    }

    @Test
    public void testDIR_FLAG() {
        int expected = 040000;
        int actual = UnixStat.DIR_FLAG;
        assertEquals("DIR_FLAG should equal 040000", expected, actual);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        int expected = 0777;
        int actual = UnixStat.DEFAULT_LINK_PERM;
        assertEquals("DEFAULT_LINK_PERM should equal 0777", expected, actual);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        int expected = 0755;
        int actual = UnixStat.DEFAULT_DIR_PERM;
        assertEquals("DEFAULT_DIR_PERM should equal 0755", expected, actual);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        int expected = 0644;
        int actual = UnixStat.DEFAULT_FILE_PERM;
        assertEquals("DEFAULT_FILE_PERM should equal 0644", expected, actual);
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPermissionMask() {
        int permissionMask = -1;
        UnixStat.PERM_MASK = permissionMask;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLinkFlag() {
        int linkFlag = 0;
        UnixStat.LINK_FLAG = linkFlag;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileFlag() {
        int fileFlag = 0;
        UnixStat.FILE_FLAG = fileFlag;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDirFlag() {
        int dirFlag = 0;
        UnixStat.DIR_FLAG = dirFlag;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDefaultLinkPerm() {
        int defaultLinkPerm = -1;
        UnixStat.DEFAULT_LINK_PERM = defaultLinkPerm;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDefaultDirPerm() {
        int defaultDirPerm = -1;
        UnixStat.DEFAULT_DIR_PERM = defaultDirPerm;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDefaultFilePerm() {
        int defaultFilePerm = -1;
        UnixStat.DEFAULT_FILE_PERM = defaultFilePerm;
    }
}