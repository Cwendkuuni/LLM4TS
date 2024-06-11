package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.UnixStat.DEFAULT_DIR_PERM;
import static org.apache.commons.compress.archivers.zip.UnixStat.DEFAULT_FILE_PERM;
import static org.apache.commons.compress.archivers.zip.UnixStat.DEFAULT_LINK_PERM;
import static org.apache.commons.compress.archivers.zip.UnixStat.FILE_FLAG;
import static org.apache.commons.compress.archivers.zip.UnixStat.DIR_FLAG;
import static org.apache.commons.compress.archivers.zip.UnixStat.PERM_MASK;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for UnixStat interface.
 */
public class UnixStatTest {

    /**
     * Test method for the UnixStat constants.
     */
    @Test
    public void testUnixStatConstants() {
        // Test PERM_MASK
        int permMask = UnixStat.PERM_MASK;
        assertEquals("PERM_MASK should be 07777", 07777, permMask);

        // Test LINK_FLAG
        int linkFlag = UnixStat.LINK_FLAG;
        assertEquals("LINK_FLAG should be 0120000", 0120000, linkFlag);

        // Test FILE_FLAG
        int fileFlag = UnixStat.FILE_FLAG;
        assertEquals("FILE_FLAG should be 0100000", 0100000, fileFlag);

        // Test DIR_FLAG
        int dirFlag = UnixStat.DIR_FLAG;
        assertEquals("DIR_FLAG should be 040000", 040000, dirFlag);

        // Test DEFAULT_LINK_PERM
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals("DEFAULT_LINK_PERM should be 0777", 0777, defaultLinkPerm);

        // Test DEFAULT_DIR_PERM
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals("DEFAULT_DIR_PERM should be 0755", 0755, defaultDirPerm);

        // Test DEFAULT_FILE_PERM
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals("DEFAULT_FILE_PERM should be 0644", 0644, defaultFilePerm);
    }

    /**
     * Test method for combining flags and permissions.
     */
    @Test
    public void testCombineFlagsAndPermissions() {
        // Test combining permissions with TYPE_FILE
        int filePermissions = DEFAULT_FILE_PERM;
        int fileType = FILE_FLAG;
        int result = UnixStat.combine(fileType, filePermissions);
        int expected = filePermissions | fileType;
        assertEquals("Combined result with TYPE_FILE should be " + expected, expected, result);

        // Test combining permissions with TYPE_DIR
        int dirPermissions = DEFAULT_DIR_PERM;
        int dirType = DIR_FLAG;
        result = UnixStat.combine(dirType, dirPermissions);
        expected = dirPermissions | dirType;
        assertEquals("Combined result with TYPE_DIR should be " + expected, expected, result);

        // Test combining permissions with TYPE_LINK
        int linkPermissions = DEFAULT_LINK_PERM;
        int linkType = LINK_FLAG;
        result = UnixStat.combine(linkType, linkPermissions);
        expected = linkPermissions | linkType;
        assertEquals("Combined result with TYPE_LINK should be " + expected, expected, result);
    }
}