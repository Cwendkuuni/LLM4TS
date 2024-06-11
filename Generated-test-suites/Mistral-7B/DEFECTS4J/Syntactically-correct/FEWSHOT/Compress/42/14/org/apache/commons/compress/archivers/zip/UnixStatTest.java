package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testConstants() {
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

        // Test different combinations of flags
        int linkWithWritablePermission = UnixStat.LINK_FLAG | 0222;
        int dirWithExecutablePermissionForOthers = UnixStat.DIR_FLAG | 0755 >> 3;
        int fileWithReadablePermissionForGroup = UnixStat.FILE_FLAG | 0664 >> 3;

        assertEquals(linkWithWritablePermission, 012210000);
        assertEquals(dirWithExecutablePermissionForOthers, 041755);
        assertEquals(fileWithReadablePermissionForGroup, 010664);
    }

    @Test
    public void testFlagMasks() {
        int linkWithWritablePermission = UnixStat.LINK_FLAG | 0222;
        int dirWithExecutablePermissionForOthers = UnixStat.DIR_FLAG | 0755 >> 3;
        int fileWithReadablePermissionForGroup = UnixStat.FILE_FLAG | 0664 >> 3;

        // Test masking different flags
        int linkMask = UnixStat.PERM_MASK & linkWithWritablePermission;
        int dirMask = UnixStat.PERM_MASK & dirWithExecutablePermissionForOthers;
        int fileMask = UnixStat.PERM_MASK & fileWithReadablePermissionForGroup;

        assertEquals(linkMask, 0122100);
        assertEquals(dirMask, 0417550);
        assertEquals(fileMask, 0106640);

        // Test checking if a flag is present
        assertTrue(UnixStat.isLink(linkWithWritablePermission));
        assertTrue(UnixStat.isDirectory(dirWithExecutablePermissionForOthers));
        assertTrue(UnixStat.isFile(fileWithReadablePermissionForGroup));

        // Test checking if a flag is not present
        assertFalse(UnixStat.isLink(fileWithReadablePermissionForGroup));
        assertFalse(UnixStat.isDirectory(fileWithReadablePermissionForGroup));
        assertFalse(UnixStat.isFile(dirWithExecutablePermissionForOthers));
    }

    private boolean isLink(int permissions) {
        return (permissions & UnixStat.LINK_FLAG) == UnixStat.LINK_FLAG;
    }

    private boolean isDirectory(int permissions) {
        return (permissions & UnixStat.DIR_FLAG) == UnixStat.DIR_FLAG;
    }

    private boolean isFile(int permissions) {
        return (permissions & UnixStat.FILE_FLAG) == UnixStat.FILE_FLAG;
    }

    @Test
    public void testDefaultPermissions() {
        assertEquals(UnixStat.DEFAULT_LINK_PERM, 0777);
        assertEquals(UnixStat.DEFAULT_DIR_PERM, 0755);
        assertEquals(UnixStat.DEFAULT_FILE_PERM, 0644);
    }

    @Test
    public void testDefaultPermissionsWithFlags() {
        int linkWithDefaultPermissions = UnixStat.DEFAULT_LINK_PERM | UnixStat.LINK_FLAG;
        int dirWithDefaultPermissions = UnixStat.DEFAULT_DIR_PERM | UnixStat.DIR_FLAG;
        int fileWithDefaultPermissions = UnixStat.DEFAULT_FILE_PERM | UnixStat.FILE_FLAG;

        assertEquals(linkWithDefaultPermissions, 077710000);
        assertEquals(dirWithDefaultPermissions, 075540755);
        assertEquals(fileWithDefaultPermissions, 0644640);
    }

    @Test
    public void testMiscellaneous() {
        assertEquals(0775, UnixStat.DEFAULT_DIR_PERM | 0111);
        assertEquals(0640, UnixStat.DEFAULT_FILE_PERM | 040);
        assertEquals(01000, UnixStat.PERM_MASK & ~UnixStat.PERM_MASK >> 3);
    }
}