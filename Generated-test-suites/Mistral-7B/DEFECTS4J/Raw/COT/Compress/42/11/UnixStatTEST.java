import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    /**
     * Test for getLinkFlag method.
     */
    @Test
    public void testGetLinkFlag() {
        int expected = UnixStat.LINK_FLAG;
        int actual = UnixStat.getLinkFlag();
        assertEquals(expected, actual);
    }

    /**
     * Test for getFileFlag method.
     */
    @Test
    public void testGetFileFlag() {
        int expected = UnixStat.FILE_FLAG;
        int actual = UnixStat.getFileFlag();
        assertEquals(expected, actual);
    }

    /**
     * Test for getDirFlag method.
     */
    @Test
    public void testGetDirFlag() {
        int expected = UnixStat.DIR_FLAG;
        int actual = UnixStat.getDirFlag();
        assertEquals(expected, actual);
    }

    /**
     * Test for getDefaultLinkPerm method.
     */
    @Test
    public void testGetDefaultLinkPerm() {
        int expected = UnixStat.DEFAULT_LINK_PERM;
        int actual = UnixStat.getDefaultLinkPerm();
        assertEquals(expected, actual);
    }

    /**
     * Test for getDefaultDirPerm method.
     */
    @Test
    public void testGetDefaultDirPerm() {
        int expected = UnixStat.DEFAULT_DIR_PERM;
        int actual = UnixStat.getDefaultDirPerm();
        assertEquals(expected, actual);
    }

    /**
     * Test for getDefaultFilePerm method.
     */
    @Test
    public void testGetDefaultFilePerm() {
        int expected = UnixStat.DEFAULT_FILE_PERM;
        int actual = UnixStat.getDefaultFilePerm();
        assertEquals(expected, actual);
    }

    /**
     * Test for getPermMask method.
     */
    @Test
    public void testGetPermMask() {
        int expected = UnixStat.PERM_MASK;
        int actual = UnixStat.getPermMask();
        assertEquals(expected, actual);
    }

    /**
     * Test getLinkFlag with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLinkFlagNegative() {
        UnixStat.getLinkFlag(-1);
    }

    /**
     * Test getFileFlag with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFileFlagNegative() {
        UnixStat.getFileFlag(-1);
    }

    /**
     * Test getDirFlag with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDirFlagNegative() {
        UnixStat.getDirFlag(-1);
    }

    /**
     * Test getLinkFlag with zero value.
     */
    @Test
    public void testGetLinkFlagZero() {
        int expected = 0;
        int actual = UnixStat.getLinkFlag(0);
        assertEquals(expected, actual);
    }

    /**
     * Test getFileFlag with zero value.
     */
    @Test
    public void testGetFileFlagZero() {
        int expected = 0;
        int actual = UnixStat.getFileFlag(0);
        assertEquals(expected, actual);
    }

    /**
     * Test getDirFlag with zero value.
     */
    @Test
    public void testGetDirFlagZero() {
        int expected = 0;
        int actual = UnixStat.getDirFlag(0);
        assertEquals(expected, actual);
    }
}