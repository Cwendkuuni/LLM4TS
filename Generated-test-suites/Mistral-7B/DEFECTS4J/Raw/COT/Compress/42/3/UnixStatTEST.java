import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int result = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, result);
    }

    @Test
    public void testFileFlag() {
        int result = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals(expected, result);
    }

    @Test
    public void testDirFlag() {
        int result = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultLinkPerm() {
        int result = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultDirPerm() {
        int result = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultFilePerm() {
        int result = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testNegativePermMask() {
        int result = UnixStat.PERM_MASK & ~0xFFFF;
        fail("Perm mask should not allow negative bits.");
    }

    @Test
    public void testLinkFlagWithPermission() {
        int linkPermission = UnixStat.PERM_MASK | UnixStat.LINK_FLAG;
        int expected = 07777120000;
        int result = linkPermission;
        assertEquals(expected, result);
    }

    @Test
    public void testFileFlagWithPermission() {
        int filePermission = UnixStat.PERM_MASK | UnixStat.FILE_FLAG;
        int expected = 07777100000;
        int result = filePermission;
        assertEquals(expected, result);
    }

    @Test
    public void testDirFlagWithPermission() {
        int dirPermission = UnixStat.PERM_MASK | UnixStat.DIR_FLAG;
        int expected = 07777400000;
        int result = dirPermission;
        assertEquals(expected, result);
    }
}