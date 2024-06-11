import static org.apache.commons.compress.archivers.zip.UnixStat.*;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test PERM_MASK
        int permMask = 07777;
        assertEquals(PERM_MASK, permMask);

        // Test LINK_FLAG
        int linkFlag = 0120000;
        assertEquals(LINK_FLAG, linkFlag);

        // Test FILE_FLAG
        int fileFlag = 0100000;
        assertEquals(FILE_FLAG, fileFlag);

        // Test DIR_FLAG
        int dirFlag = 040000;
        assertEquals(DIR_FLAG, dirFlag);

        // Test DEFAULT_LINK_PERM
        int defaultLinkPerm = 0777;
        assertEquals(DEFAULT_LINK_PERM, defaultLinkPerm);

        // Test DEFAULT_DIR_PERM
        int defaultDirPerm = 0755;
        assertEquals(DEFAULT_DIR_PERM, defaultDirPerm);

        // Test DEFAULT_FILE_PERM
        int defaultFilePerm = 0644;
        assertEquals(DEFAULT_FILE_PERM, defaultFilePerm);
    }
}