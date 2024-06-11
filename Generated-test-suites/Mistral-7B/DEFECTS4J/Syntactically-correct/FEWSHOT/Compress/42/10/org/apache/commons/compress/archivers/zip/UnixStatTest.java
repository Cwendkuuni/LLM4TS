package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermissionsMask() {
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testDifferentPermissions() {
        assertNotEquals(UnixStat.DEFAULT_FILE_PERM, UnixStat.DEFAULT_DIR_PERM);
        assertNotEquals(UnixStat.DEFAULT_DIR_PERM, UnixStat.DEFAULT_LINK_PERM);
        assertNotEquals(UnixStat.DEFAULT_FILE_PERM, UnixStat.DEFAULT_LINK_PERM);
    }
}