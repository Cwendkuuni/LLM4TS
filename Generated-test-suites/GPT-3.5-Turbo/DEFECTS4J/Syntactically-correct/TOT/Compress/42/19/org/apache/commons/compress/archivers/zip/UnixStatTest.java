import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    // Test for PERM_MASK constant
    @Test
    public void testPermMask() {
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    // Test for LINK_FLAG constant
    @Test
    public void testLinkFlag() {
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    // Test for FILE_FLAG constant
    @Test
    public void testFileFlag() {
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    // Test for DIR_FLAG constant
    @Test
    public void testDirFlag() {
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    // Test for DEFAULT_LINK_PERM constant
    @Test
    public void testDefaultLinkPerm() {
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    // Test for DEFAULT_DIR_PERM constant
    @Test
    public void testDefaultDirPerm() {
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    // Test for DEFAULT_FILE_PERM constant
    @Test
    public void testDefaultFilePerm() {
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

}