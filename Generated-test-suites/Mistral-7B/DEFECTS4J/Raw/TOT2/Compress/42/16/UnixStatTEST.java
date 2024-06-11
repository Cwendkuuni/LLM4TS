// UnixStatTest.java

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {
    // Test for getLinkFlag() method
    @Test
    public void testGetLinkFlag() {
        int actualLinkFlag = UnixStat.LINK_FLAG;
        int expectedLinkFlag = 0120000;
        assertEquals(expectedLinkFlag, actualLinkFlag);
    }

    // Test for getFileFlag() method
    @Test
    public void testGetFileFlag() {
        int actualFileFlag = UnixStat.FILE_FLAG;
        int expectedFileFlag = 0100000;
        assertEquals(expectedFileFlag, actualFileFlag);
    }

    // Test for getDirFlag() method
    @Test
    public void testGetDirFlag() {
        int actualDirFlag = UnixStat.DIR_FLAG;
        int expectedDirFlag = 040000;
        assertEquals(expectedDirFlag, actualDirFlag);
    }

    // Test for getDefaultLinkPerm() method
    @Test
    public void testGetDefaultLinkPerm() {
        int actualDefaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int expectedDefaultLinkPerm = 0777;
        assertEquals(expectedDefaultLinkPerm, actualDefaultLinkPerm);
    }

    // Test for getDefaultDirPerm() method
    @Test
    public void testGetDefaultDirPerm() {
        int actualDefaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int expectedDefaultDirPerm = 0755;
        assertEquals(expectedDefaultDirPerm, actualDefaultDirPerm);
    }

    // Test for getDefaultFilePerm() method
    @Test
    public void testGetDefaultFilePerm() {
        int actualDefaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        int expectedDefaultFilePerm = 0644;
        assertEquals(expectedDefaultFilePerm, actualDefaultFilePerm);
    }
}