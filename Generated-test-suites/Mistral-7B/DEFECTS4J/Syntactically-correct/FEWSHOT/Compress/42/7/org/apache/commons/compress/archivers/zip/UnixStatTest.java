package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    }

    @Test
    public void testCombinePermissions() {
        // Test combining permissions using UnixStat constants
        int read = 04;
        int write = 02;
        int execute = 01;

        int filePermissions = read | write | execute;
        int directoryPermissions = read | write | execute | 07; // 07 is the group and others permissions

        int combinedPermissions = UnixStat.PERM_MASK & (filePermissions | directoryPermissions);

        assertEquals(0755, combinedPermissions);
    }

    @Test
    public void testCheckLinkFlag() {
        // Test checking for link flag
        int filePerms = 0644;
        int linkPerms = filePerms | UnixStat.LINK_FLAG;

        assertTrue(UnixStat.isLink(linkPerms));
        assertFalse(UnixStat.isLink(filePerms));
    }

    @Test
    public void testCheckFileFlag() {
        // Test checking for file flag
        int dirPerms = 0755;
        int filePerms = dirPerms & ~UnixStat.DIR_FLAG;

        assertFalse(UnixStat.isFile(dirPerms));
        assertTrue(UnixStat.isFile(filePerms));
    }

    @Test
    public void testCheckDirFlag() {
        // Test checking for directory flag
        int filePerms = 0644;
        int dirPerms = filePerms | UnixStat.DIR_FLAG;

        assertFalse(UnixStat.isDir(filePerms));
        assertTrue(UnixStat.isDir(dirPerms));
    }
}