
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        // Test PERM_MASK constant
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        // Test LINK_FLAG constant
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        // Test FILE_FLAG constant
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        // Test DIR_FLAG constant
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        // Test DEFAULT_LINK_PERM constant
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        // Test DEFAULT_DIR_PERM constant
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        // Test DEFAULT_FILE_PERM constant
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

}
