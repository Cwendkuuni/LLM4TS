package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        assertEquals("PERM_MASK test", 07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        assertEquals("LINK_FLAG test", 0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        assertEquals("FILE_FLAG test", 0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        assertEquals("DIR_FLAG test", 040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        assertEquals("DEFAULT_LINK_PERM test", 0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        assertEquals("DEFAULT_DIR_PERM test", 0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        assertEquals("DEFAULT_FILE_PERM test", 0644, UnixStat.DEFAULT_FILE_PERM);
    }
}