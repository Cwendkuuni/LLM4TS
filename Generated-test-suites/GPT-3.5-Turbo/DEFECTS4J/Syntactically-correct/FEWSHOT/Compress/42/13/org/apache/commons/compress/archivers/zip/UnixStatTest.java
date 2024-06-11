package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnixStatTest {
    @Test
    public void testPermMask() {
        // Test if perm mask is set to 07777
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        // Test if link flag is set to 0120000
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        // Test if file flag is set to 0100000
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        // Test if dir flag is set to 040000
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        // Test if default link permission is set to 0777
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        // Test if default directory permission is set to 0755
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        // Test if default file permission is set to 0644
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }
}