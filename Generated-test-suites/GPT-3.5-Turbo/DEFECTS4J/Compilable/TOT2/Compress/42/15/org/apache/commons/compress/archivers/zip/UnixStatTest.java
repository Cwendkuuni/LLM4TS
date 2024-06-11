package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int expected = 07777;
        assertEquals(expected, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        int expected = 0120000;
        assertEquals(expected, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        int expected = 0100000;
        assertEquals(expected, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        int expected = 040000;
        assertEquals(expected, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        int expected = 0777;
        assertEquals(expected, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        int expected = 0755;
        assertEquals(expected, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        int expected = 0644;
        assertEquals(expected, UnixStat.DEFAULT_FILE_PERM);
    }

}