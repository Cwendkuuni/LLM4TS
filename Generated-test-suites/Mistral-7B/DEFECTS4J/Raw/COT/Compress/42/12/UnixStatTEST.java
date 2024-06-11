package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals(expected, actual);
    }

    @Test
    public void testPermMask() {
        int actual = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testNegativeNumbers() {
        int actual = UnixStat.PERM_MASK & -1;
        fail("Permission mask should not allow negative numbers");
    }

    @Test
    public void testLinkFlagWithPermissionBits() {
        int linkPermission = UnixStat.LINK_FLAG | UnixStat.PERM_MASK;
        int actual = UnixStat.linkType(linkPermission);
        int expected = UnixStat.LINK_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFlagWithPermissionBits() {
        int filePermission = UnixStat.FILE_FLAG | UnixStat.PERM_MASK;
        int actual = UnixStat.fileType(filePermission);
        int expected = UnixStat.FILE_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testDirFlagWithPermissionBits() {
        int dirPermission = UnixStat.DIR_FLAG | UnixStat.PERM_MASK;
        int actual = UnixStat.fileType(dirPermission);
        int expected = UnixStat.DIR_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testUnknownFileType() {
        int unknownFileType = 0xFFFF & ~(UnixStat.PERM_MASK);
        int actual = UnixStat.fileType(unknownFileType);
        int expected = -1;
        assertEquals(expected, actual);
    }
}