package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testConstants() {
        // Test UnixStat constants
        assertEquals(07777, UnixStat.PERM_MASK);
        assertEquals(0120000, UnixStat.LINK_FLAG);
        assertEquals(0100000, UnixStat.FILE_FLAG);
        assertEquals(040000, UnixStat.DIR_FLAG);

        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnmodifiableInterface() {
        // Test that Unmodifiable interface cannot be instantiated
        new UnixStat() {};
    }
}