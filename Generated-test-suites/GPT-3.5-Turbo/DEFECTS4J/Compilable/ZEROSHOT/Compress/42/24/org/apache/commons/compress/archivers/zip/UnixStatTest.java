package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    UnixStat unixStat;

    @Test
    public void testGetPermMask() {
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testGetLinkFlag() {
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testGetFileFlag() {
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testGetDirFlag() {
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testGetDefaultLinkPerm() {
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testGetDefaultDirPerm() {
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testGetDefaultFilePerm() {
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }
}