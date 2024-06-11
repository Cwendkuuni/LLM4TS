package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testGetLinkFlag() {
        assertEquals(UnixStat.LINK_FLAG, 0120000);
    }

    @Test
    public void testGetFileFlag() {
        assertEquals(UnixStat.FILE_FLAG, 0100000);
    }

    @Test
    public void testGetDirFlag() {
        assertEquals(UnixStat.DIR_FLAG, 040000);
    }

    @Test
    public void testGetDefaultLinkPerm() {
        assertEquals(UnixStat.DEFAULT_LINK_PERM, 0777);
    }

    @Test
    public void testGetDefaultDirPerm() {
        assertEquals(UnixStat.DEFAULT_DIR_PERM, 0755);
    }

    @Test
    public void testGetDefaultFilePerm() {
        assertEquals(UnixStat.DEFAULT_FILE_PERM, 0644);
    }

    @Test
    public void testGetPermMask() {
        assertEquals(UnixStat.PERM_MASK, 07777);
    }
}