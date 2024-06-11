import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int expectedLinkFlag = UnixStat.LINK_FLAG;
        int actualLinkFlag = UnixStat.PERM_MASK & UnixStat.LINK_FLAG;
        assertEquals("Incorrect link flag value", expectedLinkFlag, actualLinkFlag);
    }

    @Test
    public void testFileFlag() {
        int expectedFileFlag = UnixStat.FILE_FLAG;
        int actualFileFlag = UnixStat.PERM_MASK & UnixStat.FILE_FLAG;
        assertEquals("Incorrect file flag value", expectedFileFlag, actualFileFlag);
    }

    @Test
    public void testDirFlag() {
        int expectedDirFlag = UnixStat.DIR_FLAG;
        int actualDirFlag = UnixStat.PERM_MASK & UnixStat.DIR_FLAG;
        assertEquals("Incorrect directory flag value", expectedDirFlag, actualDirFlag);
    }

    @Test
    public void testDefaultLinkPermissions() {
        int expectedDefaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int actualDefaultLinkPerm = (UnixStat.PERM_MASK & ~UnixStat.FILE_FLAG) | UnixStat.LINK_FLAG;
        assertEquals("Incorrect default link permissions", expectedDefaultLinkPerm, actualDefaultLinkPerm);
    }

    @Test
    public void testDefaultDirPermissions() {
        int expectedDefaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int actualDefaultDirPerm = (UnixStat.PERM_MASK & ~UnixStat.LINK_FLAG) | UnixStat.DIR_FLAG;
        assertEquals("Incorrect default directory permissions", expectedDefaultDirPerm, actualDefaultDirPerm);
    }

    @Test
    public void testDefaultFilePermissions() {
        int expectedDefaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        int actualDefaultFilePerm = UnixStat.PERM_MASK & ~UnixStat.DIR_FLAG;
        assertEquals("Incorrect default file permissions", expectedDefaultFilePerm, actualDefaultFilePerm);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int perm(int fileMode) {
                throw new UnsupportedOperationException("Unsupported method");
            }
        };
        unixStat.perm(0);
    }
}