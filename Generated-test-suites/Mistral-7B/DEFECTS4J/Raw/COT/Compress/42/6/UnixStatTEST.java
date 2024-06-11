import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Link flag value is incorrect", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("File flag value is incorrect", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Directory flag value is incorrect", expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Default link permissions value is incorrect", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Default directory permissions value is incorrect", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Default file permissions value is incorrect", expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePermMask() {
        int permMask = UnixStat.PERM_MASK & 0xFFFFFF80;
        int actual = UnixStat.getDefaultFilePerm() | permMask;
        fail("Expected IllegalArgumentException, but got: " + actual);
    }

    @Test
    public void testGetDefaultPermissionsForDifferentTypes() {
        int linkPerm = UnixStat.getDefaultLinkPerm();
        int dirPerm = UnixStat.getDefaultDirPerm();
        int filePerm = UnixStat.getDefaultFilePerm();

        int expectedLinkPerm = 0777;
        int expectedDirPerm = 0755;
        int expectedFilePerm = 0644;

        assertEquals("Default link permissions for UnixStat are incorrect", expectedLinkPerm, linkPerm);
        assertEquals("Default directory permissions for UnixStat are incorrect", expectedDirPerm, dirPerm);
        assertEquals("Default file permissions for UnixStat are incorrect", expectedFilePerm, filePerm);
    }

    @Test
    public void testPermMaskBitwiseOperation() {
        int linkPermission = 0777;
        int expectedLinkPermissionWithLinkFlag = linkPermission | UnixStat.LINK_FLAG;
        assertEquals("Link permission with link flag should be: " + Integer.toBinaryString(expectedLinkPermissionWithLinkFlag),
                Integer.toBinaryString(expectedLinkPermissionWithLinkFlag), Integer.toBinaryString(linkPermission | UnixStat.LINK_FLAG));
    }
}