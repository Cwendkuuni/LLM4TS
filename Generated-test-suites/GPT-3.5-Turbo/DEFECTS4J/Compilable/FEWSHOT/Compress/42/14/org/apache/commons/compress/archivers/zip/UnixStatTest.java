package org.apache.commons.compress.archivers.zip;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnixStatTest {
    
    @Test
    public void testPERM_MASK() {
        assertEquals(07777, UnixStat.PERM_MASK);
    }
    
    @Test
    public void testLINK_FLAG() {
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }
    
    @Test
    public void testFILE_FLAG() {
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }
    
    @Test
    public void testDIR_FLAG() {
        assertEquals(040000, UnixStat.DIR_FLAG);
    }
    
    @Test
    public void testDEFAULT_LINK_PERM() {
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }
    
    @Test
    public void testDEFAULT_DIR_PERM() {
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }
    
    @Test
    public void testDEFAULT_FILE_PERM() {
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }
}