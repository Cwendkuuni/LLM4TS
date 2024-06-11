import org.junit.Test;

public class UnixStatTest {

    @Test
    public void test_permMask() {
        int expected = 07777;
        int actual = UnixStat.PERM_MASK;
        assertEquals(expected, actual);
    }

    @Test
    public void test_linkFlag() {
        int expected = 0120000;
        int actual = UnixStat.LINK_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void test_fileFlag() {
        int expected = 0100000;
        int actual = UnixStat.FILE_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void test_dirFlag() {
        int expected = 040000;
        int actual = UnixStat.DIR_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void test_defaultLinkPerm() {
        int expected = 0777;
        int actual = UnixStat.DEFAULT_LINK_PERM;
        assertEquals(expected, actual);
    }

    @Test
    public void test_defaultDirPerm() {
        int expected = 0755;
        int actual = UnixStat.DEFAULT_DIR_PERM;
        assertEquals(expected, actual);
    }

    @Test
    public void test_defaultFilePerm() {
        int expected = 0644;
        int actual = UnixStat.DEFAULT_FILE_PERM;
        assertEquals(expected, actual);
    }
}