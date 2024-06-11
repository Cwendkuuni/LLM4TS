import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        // Typical use case
        assertEquals(07777, UnixStat.PERM_MASK);

        // Edge case: Minimum value
        assertEquals(0, UnixStat.PERM_MASK);

        // Edge case: Maximum value
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        // Typical use case
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        // Typical use case
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        // Typical use case
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        // Typical use case
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        // Typical use case
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        // Typical use case
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

}