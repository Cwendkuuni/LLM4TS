import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testGetPermMask() {
        int permMask = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals(expected, permMask);
    }

    @Test
    public void testGetLinkFlag() {
        int linkFlag = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, linkFlag);
    }

    @Test
    public void testGetFileFlag() {
        int fileFlag = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals(expected, fileFlag);
    }

    @Test
    public void testGetDirFlag() {
        int dirFlag = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals(expected, dirFlag);
    }

    @Test
    public void testgetDefaultLinkPerm() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals(expected, defaultLinkPerm);
    }

    @Test
    public void testgetDefaultDirPerm() {
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals(expected, defaultDirPerm);
    }

    @Test
    public void testgetDefaultFilePerm() {
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals(expected, defaultFilePerm);
    }

    // Edge Cases and Exception Handling

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationExceptionForInvalidMethodCall() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public String toString() {
                throw new UnsupportedOperationException();
            }
        };
        String result = unixStat.toString();
        // This line will never be executed due to the exception thrown above
        fail("toString method should not be called on UnixStat interface");
    }

    @Test
    public void testCheckIfLinkFlagIsSet() {
        int testMask = UnixStat.PERM_MASK | UnixStat.LINK_FLAG;
        boolean isLink = (testMask & UnixStat.LINK_FLAG) == UnixStat.LINK_FLAG;
        assertTrue(isLink);
    }

    @Test
    public void testCheckIfFileFlagIsSet() {
        int testMask = UnixStat.PERM_MASK | UnixStat.FILE_FLAG;
        boolean isFile = (testMask & UnixStat.FILE_FLAG) == UnixStat.FILE_FLAG;
        assertTrue(isFile);
    }

    @Test
    public void testCheckIfDirFlagIsSet() {
        int testMask = UnixStat.PERM_MASK | UnixStat.DIR_FLAG;
        boolean isDir = (testMask & UnixStat.DIR_FLAG) == UnixStat.DIR_FLAG;
        assertTrue(isDir);
    }

    @Test
    public void testCheckIfPermissionsAreSet() {
        int testMask = UnixStat.PERM_MASK & 0222;
        int expected = 0022;
        int result = testMask;
        assertEquals(expected, result);
    }

    @Test
    public void testCheckIfPermissionIsSetForSpecificBit() {
        int testMask = UnixStat.PERM_MASK | 00010;
        boolean isRead = (testMask & 00010) == 00010;
        assertTrue(isRead);
    }

    @Test
    public void testCheckIfPermissionIsNotSetForSpecificBit() {
        int testMask = UnixStat.PERM_MASK & ~00010;
        boolean isNotRead = ((testMask & 00010) == 0);
        assertTrue(isNotRead);
    }

    @Test
    public void testCheckIfLinkPermissionIsSet() {
        int testMask = UnixStat.PERM_MASK | UnixStat.LINK_FLAG | 0644;
        boolean isLinkReadable = ((testMask & (UnixStat.LINK_FLAG | 00400)) == (UnixStat.LINK_FLAG | 00400));
        assertTrue(isLinkReadable);
    }

    @Test
    public void testCheckIfLinkPermissionIsNotSet() {
        int testMask = UnixStat.PERM_MASK & ~(UnixStat.LINK_FLAG | 00400);
        boolean isNotLinkReadable = ((testMask & (UnixStat.LINK_FLAG | 00400)) == 0);
        assertTrue(isNotLinkReadable);
    }
}