import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int expected = UnixStat.LINK_FLAG;
        int actual = UnixStat.PERM_MASK & UnixStat.LINK_FLAG;
        assertEquals("Link flag should have correct value", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int expected = UnixStat.FILE_FLAG;
        int actual = UnixStat.PERM_MASK & UnixStat.FILE_FLAG;
        assertEquals("File flag should have correct value", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int expected = UnixStat.DIR_FLAG;
        int actual = UnixStat.PERM_MASK & UnixStat.DIR_FLAG;
        assertEquals("Directory flag should have correct value", expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeLinkFlag() {
        int linkFlag = UnixStat.PERM_MASK | 0xFFFFFF80;
        UnixStat.linkType(linkFlag);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeFileFlag() {
        int fileFlag = UnixStat.PERM_MASK | 0xFFFFFF80;
        UnixStat.fileType(fileFlag);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeDirFlag() {
        int dirFlag = UnixStat.PERM_MASK | 0xFFFFFF80;
        UnixStat.directoryType(dirFlag);
    }

    @Test
    public void testDefaultLinkPerm() {
        int expected = UnixStat.DEFAULT_LINK_PERM;
        int actual = UnixStat.PERM_MASK & UnixStat.DEFAULT_LINK_PERM;
        assertEquals("Default link permissions should have correct value", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int expected = UnixStat.DEFAULT_DIR_PERM;
        int actual = UnixStat.PERM_MASK & UnixStat.DEFAULT_DIR_PERM;
        assertEquals("Default directory permissions should have correct value", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int expected = UnixStat.DEFAULT_FILE_PERM;
        int actual = UnixStat.PERM_MASK & UnixStat.DEFAULT_FILE_PERM;
        assertEquals("Default file permissions should have correct value", expected, actual);
    }
}