package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        int perm = UnixStat.PERM_MASK;
        assertEquals(07777, perm);
    }

    @Test
    public void testLINK_FLAG() {
        int flag = UnixStat.LINK_FLAG;
        assertEquals(0120000, flag);
    }

    @Test
    public void testFILE_FLAG() {
        int flag = UnixStat.FILE_FLAG;
        assertEquals(0100000, flag);
    }

    @Test
    public void testDIR_FLAG() {
        int flag = UnixStat.DIR_FLAG;
        assertEquals(040000, flag);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        int perm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals(0777, perm);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        int perm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals(0755, perm);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        int perm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals(0644, perm);
    }
}

