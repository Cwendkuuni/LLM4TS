package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test PERM_MASK
        assertEquals(07777, UnixStat.PERM_MASK);

        // Test LINK_FLAG
        assertEquals(0120000, UnixStat.LINK_FLAG);

        // Test FILE_FLAG
        assertEquals(0100000, UnixStat.FILE_FLAG);

        // Test DIR_FLAG
        assertEquals(040000, UnixStat.DIR_FLAG);

        // Test DEFAULT_LINK_PERM
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);

        // Test DEFAULT_DIR_PERM
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);

        // Test DEFAULT_FILE_PERM
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }
}