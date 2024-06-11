package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int permMask = UnixStat.PERM_MASK;
        assertEquals(permMask, 07777);
    }

    @Test
    public void testLinkFlag() {
        int linkFlag = UnixStat.LINK_FLAG;
        assertEquals(linkFlag, 0120000);
    }

    @Test(expected = AssertionError.class)
    public void testLinkFlagNegativeValue() {
        int linkFlag = UnixStat.LINK_FLAG - 1;
        assertEquals(linkFlag, UnixStat.LINK_FLAG);
    }

    @Test
    public void testLinkFlagWithFileFlag() {
        int combinedFlags = UnixStat.LINK_FLAG | UnixStat.FILE_FLAG;
        assertFalse(isFileOrDirectory(combinedFlags));
    }

    @Test
    public void testDefaultPermissions() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;

        assertEquals(defaultLinkPerm, 0777);
        assertEquals(defaultDirPerm, 0755);
        assertEquals(defaultFilePerm, 0644);
    }

    private boolean isFileOrDirectory(int flags) {
        return (flags & UnixStat.FILE_FLAG) != 0 || (flags & UnixStat.DIR_FLAG) != 0;
    }
}
