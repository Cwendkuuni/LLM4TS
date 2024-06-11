import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testConstants() {
        // Test PERM_MASK
        int expectedPermMask = 07777;
        int actualPermMask = UnixStat.PERM_MASK;
        assertEquals("PERM_MASK should be: " + expectedPermMask, expectedPermMask, actualPermMask);

        // Test LINK_FLAG
        int expectedLinkFlag = 0120000;
        int actualLinkFlag = UnixStat.LINK_FLAG;
        assertEquals("LINK_FLAG should be: " + expectedLinkFlag, expectedLinkFlag, actualLinkFlag);

        // Test FILE_FLAG
        int expectedFileFlag = 0100000;
        int actualFileFlag = UnixStat.FILE_FLAG;
        assertEquals("FILE_FLAG should be: " + expectedFileFlag, expectedFileFlag, actualFileFlag);

        // Test DIR_FLAG
        int expectedDirFlag = 040000;
        int actualDirFlag = UnixStat.DIR_FLAG;
        assertEquals("DIR_FLAG should be: " + expectedDirFlag, expectedDirFlag, actualDirFlag);

        // Test DEFAULT_LINK_PERM
        int expectedDefaultLinkPerm = 0777;
        int actualDefaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals("DEFAULT_LINK_PERM should be: " + expectedDefaultLinkPerm, expectedDefaultLinkPerm, actualDefaultLinkPerm);

        // Test DEFAULT_DIR_PERM
        int expectedDefaultDirPerm = 0755;
        int actualDefaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals("DEFAULT_DIR_PERM should be: " + expectedDefaultDirPerm, expectedDefaultDirPerm, actualDefaultDirPerm);

        // Test DEFAULT_FILE_PERM
        int expectedDefaultFilePerm = 0644;
        int actualDefaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals("DEFAULT_FILE_PERM should be: " + expectedDefaultFilePerm, expectedDefaultFilePerm, actualDefaultFilePerm);
    }
}