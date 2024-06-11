import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int result = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Link flag should be 0120000", expected, result);
    }

    @Test
    public void testFileFlag() {
        int result = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("File flag should be 0100000", expected, result);
    }

    @Test
    public void testDirFlag() {
        int result = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Directory flag should be 040000", expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeLinkFlag() {
        int linkFlag = UnixStat.LINK_FLAG - 1;
        UnixStat unixStat = new UnixStat() {
            @Override
            public int getLinkFlag() {
                return linkFlag;
            }
        };
        unixStat.getLinkFlag();
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeFileFlag() {
        int fileFlag = UnixStat.FILE_FLAG - 1;
        UnixStat unixStat = new UnixStat() {
            @Override
            public int getFileFlag() {
                return fileFlag;
            }
        };
        unixStat.getFileFlag();
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeDirFlag() {
        int dirFlag = UnixStat.DIR_FLAG - 1;
        UnixStat unixStat = new UnixStat() {
            @Override
            public int getDirFlag() {
                return dirFlag;
            }
        };
        unixStat.getDirFlag();
    }

    @Test
    public void testDefaultLinkPerm() {
        int result = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Default link permission should be 0777", expected, result);
    }

    @Test
    public void testDefaultDirPerm() {
        int result = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Default directory permission should be 0755", expected, result);
    }

    @Test
    public void testDefaultFilePerm() {
        int result = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Default file permission should be 0644", expected, result);
    }

    @Test
    public void testPermMask() {
        int result = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals("Perm mask should be 07777", expected, result);
    }
}