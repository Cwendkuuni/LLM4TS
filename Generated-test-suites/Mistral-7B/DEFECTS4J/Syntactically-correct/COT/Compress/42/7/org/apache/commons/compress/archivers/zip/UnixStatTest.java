import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testPermMaskNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int PERM_MASK() {
                return -1;
            }
        };
        int permMask = unixStat.PERM_MASK();
        fail("Expected IllegalStateException not thrown.");
    }

    @Test(expected = IllegalStateException.class)
    public void testLinkFlagNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int LINK_FLAG() {
                return -1;
            }
        };
        int linkFlag = unixStat.LINK_FLAG();
        fail("Expected IllegalStateException not thrown.");
    }

    @Test(expected = IllegalStateException.class)
    public void testFileFlagNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int FILE_FLAG() {
                return -1;
            }
        };
        int fileFlag = unixStat.FILE_FLAG();
        fail("Expected IllegalStateException not thrown.");
    }

    @Test(expected = IllegalStateException.class)
    public void testDirFlagNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int DIR_FLAG() {
                return -1;
            }
        };
        int dirFlag = unixStat.DIR_FLAG();
        fail("Expected IllegalStateException not thrown.");
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultLinkPermNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int DEFAULT_LINK_PERM() {
                return -1;
            }
        };
        int defaultLinkPerm = unixStat.DEFAULT_LINK_PERM();
        fail("Expected IllegalStateException not thrown.");
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultDirPermNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int DEFAULT_DIR_PERM() {
                return -1;
            }
        };
        int defaultDirPerm = unixStat.DEFAULT_DIR_PERM();
        fail("Expected IllegalStateException not thrown.");
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultFilePermNegative() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int DEFAULT_FILE_PERM() {
                return -1;
            }
        };
        int defaultFilePerm = unixStat.DEFAULT_FILE_PERM();
        fail("Expected IllegalStateException not thrown.");
    }
}