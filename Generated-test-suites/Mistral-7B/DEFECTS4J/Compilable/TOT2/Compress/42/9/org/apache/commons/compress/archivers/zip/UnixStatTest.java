package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int expected = UnixStat.LINK_FLAG;
        int result = UnixStat.PERM_MASK & UnixStat.LINK_FLAG;
        assertEquals(expected, result);
    }

    @Test
    public void testFileFlag() {
        int expected = UnixStat.FILE_FLAG;
        int result = UnixStat.PERM_MASK & UnixStat.FILE_FLAG;
        assertEquals(expected, result);
    }

    @Test
    public void testDirFlag() {
        int expected = UnixStat.DIR_FLAG;
        int result = UnixStat.PERM_MASK & UnixStat.DIR_FLAG;
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultLinkPerm() {
        int expected = UnixStat.DEFAULT_LINK_PERM;
        int result = UnixStat.PERM_MASK & UnixStat.DEFAULT_LINK_PERM;
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultDirPerm() {
        int expected = UnixStat.DEFAULT_DIR_PERM;
        int result = UnixStat.PERM_MASK & UnixStat.DEFAULT_DIR_PERM;
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultFilePerm() {
        int expected = UnixStat.DEFAULT_FILE_PERM;
        int result = UnixStat.PERM_MASK & UnixStat.DEFAULT_FILE_PERM;
        assertEquals(expected, result);
    }
}