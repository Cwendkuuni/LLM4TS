package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test UnixStat constants
        assertEquals(07777, UnixStat.PERM_MASK);
        assertEquals(0120000, UnixStat.LINK_FLAG);
        assertEquals(0100000, UnixStat.FILE_FLAG);
        assertEquals(040000, UnixStat.DIR_FLAG);
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);

        // Test UnixStat constants with bitwise AND
        assertEquals(0644 & UnixStat.DEFAULT_FILE_PERM, 0644);
        assertEquals(0755 & UnixStat.DEFAULT_DIR_PERM, 0755);
        assertEquals(0100000 & UnixStat.FILE_FLAG, 0100000);
        assertEquals(040000 & UnixStat.DIR_FLAG, 040000);
        assertEquals(07777 & UnixStat.PERM_MASK, 07777);
        assertEquals(0120000 & UnixStat.LINK_FLAG, 0120000);
    }

    @Test
    public void testUnixStatFileFlagComparison() {
        // Test File_flag comparison
        assertNotEquals(UnixStat.FILE_FLAG, UnixStat.LINK_FLAG);
        assertNotEquals(UnixStat.FILE_FLAG, UnixStat.DIR_FLAG);
        assertEquals(UnixStat.FILE_FLAG, UnixStat.FILE_FLAG);
        assertNotEquals(UnixStat.DIR_FLAG, UnixStat.LINK_FLAG);
        assertEquals(UnixStat.DIR_FLAG, UnixStat.DIR_FLAG);
        assertEquals(UnixStat.LINK_FLAG, UnixStat.LINK_FLAG);
    }
}