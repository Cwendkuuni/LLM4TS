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

        // Test UnixStat constants with bitwise AND and OR
        assertEquals(UnixStat.FILE_FLAG | UnixStat.PERM_MASK, 0x1000644);
        assertEquals(UnixStat.DIR_FLAG | UnixStat.PERM_MASK, 0x4100755);
        assertEquals((UnixStat.LINK_FLAG & UnixStat.PERM_MASK) | UnixStat.PERM_MASK, 0x12777);
    }

    @Test
    public void testUnixStatBitwiseOperations() {
        // Test UnixStat constants with bitwise AND, OR and XOR
        assertEquals((UnixStat.FILE_FLAG & UnixStat.PERM_MASK), 0x100000);
        assertEquals((UnixStat.DIR_FLAG | UnixStat.PERM_MASK) ^ UnixStat.LINK_FLAG, 0x411665);
        assertEquals((UnixStat.FILE_FLAG ^ UnixStat.PERM_MASK) | UnixStat.DIR_FLAG, 0x5100677);
        assertEquals((UnixStat.LINK_FLAG & UnixStat.PERM_MASK) ^ UnixStat.FILE_FLAG, 0x1177120);
    }

    @Test
    public void testUnixStatDifferentPermissions() {
        // Test different permissions for symbolic links, directories and plain files
        assertNotEquals(UnixStat.DEFAULT_LINK_PERM, UnixStat.DEFAULT_DIR_PERM);
        assertNotEquals(UnixStat.DEFAULT_DIR_PERM, UnixStat.DEFAULT_FILE_PERM);
        assertNotEquals(UnixStat.DEFAULT_FILE_PERM, UnixStat.PERM_MASK);
    }
}