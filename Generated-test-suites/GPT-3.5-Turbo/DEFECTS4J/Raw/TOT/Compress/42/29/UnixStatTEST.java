import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnixStatTest {

    private UnixStat unixStat;

    @Before
    public void setup() {
        unixStat = new UnixStat();
    }

    @Test
    public void testDefaultLinkPerm() {
        assertEquals(0777, unixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        assertEquals(0755, unixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        assertEquals(0644, unixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testPermMask() {
        assertEquals(07777, unixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        assertEquals(0120000, unixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        assertEquals(0100000, unixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        assertEquals(040000, unixStat.DIR_FLAG);
    }
}