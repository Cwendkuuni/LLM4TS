import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.UnixStat;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int expected = 07777;
        int actual = UnixStat.PERM_MASK;
        assertEquals(expected, actual);
    }

    @Test
    public void testLinkFlag() {
        int expected = 0120000;
        int actual = UnixStat.LINK_FLAG;
        assertEquals(expected, actual);
    }

    @Test
    public void testIncorrectPermissionsCombination() {
        int permissions = UnixStat.PERM_MASK & (~UnixStat.PERM_MASK >> 2); // Setting all bits except for the 3rd bit
        fail("Expected an IllegalArgumentException but got: " + permissions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDefaultPermissions() {
        int linkPerm = -1;
        int dirPerm = -1;
        int filePerm = -1;
        fail("Expected an IllegalArgumentException but got: " + UnixStat.getDefaultPermissions(linkPerm, dirPerm, filePerm));
    }

    @Test
    public void testGetDefaultLinkPerm() {
        int expected = 0777;
        int actual = UnixStat.getDefaultLinkPerm();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultDirPerm() {
        int expected = 0755;
        int actual = UnixStat.getDefaultDirPerm();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultFilePerm() {
        int expected = 0644;
        int actual = UnixStat.getDefaultFilePerm();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultPermissions() {
        int linkPerm = 0777;
        int dirPerm = 0755;
        int filePerm = 0644;
        int[] expected = {linkPerm, dirPerm, filePerm};
        int[] actual = UnixStat.getDefaultPermissions(linkPerm, dirPerm, filePerm);
        assertArrayEquals(expected, actual);
    }
}