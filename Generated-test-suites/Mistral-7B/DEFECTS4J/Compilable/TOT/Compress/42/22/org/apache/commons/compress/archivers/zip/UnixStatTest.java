package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int unixStat = UnixStat.LINK_FLAG;
        boolean isLink = (unixStat & UnixStat.PERM_MASK) >>> 12 == UnixStat.LINK_FLAG;
        assertTrue("UnixStat.LINK_FLAG should represent symbolic links", isLink);
    }

    @Test
    public void testFileFlag() {
        int unixStat = UnixStat.FILE_FLAG;
        boolean isFile = (unixStat & UnixStat.PERM_MASK) >>> 12 == UnixStat.FILE_FLAG;
        assertTrue("UnixStat.FILE_FLAG should represent plain files", isFile);
    }

    @Test
    public void testDirFlag() {
        int unixStat = UnixStat.DIR_FLAG;
        boolean isDirectory = (unixStat & UnixStat.PERM_MASK) >>> 12 == UnixStat.DIR_FLAG;
        assertTrue("UnixStat.DIR_FLAG should represent directories", isDirectory);

        // Test incorrect value
        int incorrectDirFlag = UnixStat.PERM_MASK | UnixStat.LINK_FLAG;
        assertFalse("Incorrect value should not represent directories", (incorrectDirFlag & UnixStat.PERM_MASK) >>> 12 == UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultPermissions() {
        int linkPermission = UnixStat.DEFAULT_LINK_PERM;
        int dirPermission = UnixStat.DEFAULT_DIR_PERM;
        int filePermission = UnixStat.DEFAULT_FILE_PERM;

        // Test link permissions
        int maskedLinkPermission = linkPermission & UnixStat.PERM_MASK;
        assertEquals("Default link permissions should equal 0777", 0777, maskedLinkPermission);

        // Test directory permissions
        int maskedDirPermission = dirPermission & UnixStat.PERM_MASK;
        assertEquals("Default directory permissions should equal 0755", 0755, maskedDirPermission);

        // Test file permissions
        int maskedFilePermission = filePermission & UnixStat.PERM_MASK;
        assertEquals("Default file permissions should equal 0644", 0644, maskedFilePermission);
    }
}