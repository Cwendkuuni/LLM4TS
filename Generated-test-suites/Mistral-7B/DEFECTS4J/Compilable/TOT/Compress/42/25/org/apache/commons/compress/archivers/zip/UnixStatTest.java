package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int expectedLinkFlag = UnixStat.LINK_FLAG;
        int actualLinkFlag = UnixStat.PERM_MASK & UnixStat.LINK_FLAG;
        assertEquals(expectedLinkFlag, actualLinkFlag);
    }

    @Test
    public void testFileFlag() {
        int expectedFileFlag = UnixStat.FILE_FLAG;
        int actualFileFlag = UnixStat.PERM_MASK & UnixStat.FILE_FLAG;
        assertEquals(expectedFileFlag, actualFileFlag);
    }

    @Test
    public void testDirFlag() {
        int expectedDirFlag = UnixStat.DIR_FLAG;
        int actualDirFlag = UnixStat.PERM_MASK & UnixStat.DIR_FLAG;
        assertEquals(expectedDirFlag, actualDirFlag);
    }

    @Test
    public void testDefaultLinkPerm() {
        int expectedDefaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int actualDefaultLinkPerm = UnixStat.DEFAULT_LINK_PERM & UnixStat.PERM_MASK;
        assertEquals(expectedDefaultLinkPerm, actualDefaultLinkPerm);
    }

    @Test
    public void testDefaultDirPerm() {
        int expectedDefaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int actualDefaultDirPerm = UnixStat.DEFAULT_DIR_PERM & UnixStat.PERM_MASK;
        assertEquals(expectedDefaultDirPerm, actualDefaultDirPerm);
    }

    @Test
    public void testDefaultFilePerm() {
        int expectedDefaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        int actualDefaultFilePerm = UnixStat.DEFAULT_FILE_PERM & UnixStat.PERM_MASK;
        assertEquals(expectedDefaultFilePerm, actualDefaultFilePerm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMask() {
        int mask = UnixStat.PERM_MASK & (-1);
        fail("Expected IllegalArgumentException but got: " + mask);
    }
}