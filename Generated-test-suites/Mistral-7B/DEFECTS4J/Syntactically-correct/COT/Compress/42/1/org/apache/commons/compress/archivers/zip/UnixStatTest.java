import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int result = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Link flag should be set correctly", expected, result);
    }

    @Test
    public void testFileFlag() {
        int result = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("File flag should be set correctly", expected, result);
    }

    @Test
    public void testDirFlag() {
        int result = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Directory flag should be set correctly", expected, result);
    }

    @Test
    public void testDefaultLinkPerm() {
        int result = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Default link permissions should be set correctly", expected, result);
    }

    @Test
    public void testDefaultDirPerm() {
        int result = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Default directory permissions should be set correctly", expected, result);
    }

    @Test
    public void testDefaultFilePerm() {
        int result = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Default file permissions should be set correctly", expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testPermissionMaskNegativeValue() {
        int result = UnixStat.PERM_MASK;
        int expected = -1; // Illegal negative value
        UnixStat unixStat = new UnixStat() {
            @Override
            public int permMask() {
                return expected;
            }
        };
        try {
            unixStat.checkPermissions(1, (short) 0xFF, (short) 0xFF);
        } catch (IllegalArgumentException e) {
            fail("Should throw IllegalStateException not IllegalArgumentException");
        }
        fail("Expected IllegalStateException, but got none");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPermissionsWithNegativePermissions() {
        int permissions = -1;
        try {
            new UnixStat().checkPermissions(1, (short) 0xFF, (short) permissions);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}