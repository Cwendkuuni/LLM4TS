import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int result = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals(expected, result);
    }

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

    // Test for negative numbers
    @Test(expected = IllegalStateException.class)
    public void testNegativePermMask() {
        int result = UnixStat.PERM_MASK * -1;
    }

    // Test for checks if link_flag can be set as a file permission
    @Test(expected = AssertionError.class)
    public void testLinkFlagAsFilePermission() {
        int result = UnixStat.FILE_FLAG | UnixStat.LINK_FLAG;
    }

    // Test for checking if dir_flag can be set as a file permission
    @Test(expected = AssertionError.class)
    public void testDirFlagAsFilePermission() {
        int result = UnixStat.FILE_FLAG | UnixStat.DIR_FLAG;
    }
}