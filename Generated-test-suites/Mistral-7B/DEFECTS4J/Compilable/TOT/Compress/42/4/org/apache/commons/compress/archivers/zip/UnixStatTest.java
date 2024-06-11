package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int linkStat = UnixStat.LINK_FLAG;
        boolean isLink = (linkStat & UnixStat.PERM_MASK) == UnixStat.LINK_FLAG;
        assertTrue("Incorrect link flag", isLink);
    }

    @Test
    public void testFileFlag() {
        int fileStat = UnixStat.FILE_FLAG;
        boolean isFile = (fileStat & UnixStat.DIR_FLAG) == 0;
        assertTrue("Incorrect file flag", isFile);
    }

    @Test
    public void testDirFlag() {
        int dirStat = UnixStat.DIR_FLAG;
        boolean isDir = (dirStat & UnixStat.FILE_FLAG) == 0;
        assertTrue("Incorrect directory flag", isDir);
    }

    @Test
    public void testDefaultPerms() {
        int linkDefault = UnixStat.DEFAULT_LINK_PERM;
        int dirDefault = UnixStat.DEFAULT_DIR_PERM;
        int fileDefault = UnixStat.DEFAULT_FILE_PERM;

        int linkExpected = 0777;
        int dirExpected = 0755;
        int fileExpected = 0644;

        assertEquals("Incorrect default permissions for symbolic links", linkExpected, linkDefault);
        assertEquals("Incorrect default permissions for directories", dirExpected, dirDefault);
        assertEquals("Incorrect default permissions for plain files", fileExpected, fileDefault);
    }

    @Test
    public void testInvalidBitCombinationLink() {
        int linkWithInvalidBits = UnixStat.LINK_FLAG | UnixStat.DIR_FLAG;
        boolean isLink = (linkWithInvalidBits & UnixStat.PERM_MASK) == UnixStat.LINK_FLAG;
        assertFalse("Unexpected link flag result with invalid bits", isLink);
    }

    @Test
    public void testInvalidBitCombinationFile() {
        int fileWithInvalidBits = UnixStat.FILE_FLAG | UnixStat.DIR_FLAG;
        boolean isFile = (fileWithInvalidBits & UnixStat.PERM_MASK) == UnixStat.FILE_FLAG;
        assertFalse("Unexpected file flag result with invalid bits", isFile);
    }

    @Test
    public void testInvalidBitCombinationDir() {
        int dirWithInvalidBits = UnixStat.DIR_FLAG | UnixStat.FILE_FLAG;
        boolean isDir = (dirWithInvalidBits & UnixStat.PERM_MASK) == UnixStat.DIR_FLAG;
        assertFalse("Unexpected directory flag result with invalid bits", isDir);
    }
}