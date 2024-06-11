package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnixStatTest {
    @Test
    public void testLinkFlag() {
        assertEquals(120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        assertEquals(100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        assertEquals(40000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        assertEquals(1777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        assertEquals(1555, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        assertEquals(644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testPermMask() {
        assertEquals(77777, UnixStat.PERM_MASK);
    }
}