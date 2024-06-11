import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        int expected = 07777;
        int actual = UnixStat.PERM_MASK;
        assertEquals("PERM_MASK should be 07777", expected, actual);
    }

    @Test
    public void testLINK_FLAG() {
        int expected = 0120000;
        int actual = UnixStat.LINK_FLAG;
        assertEquals("LINK_FLAG should be 0120000", expected, actual);
    }

    @Test
    public void testFILE_FLAG() {
        int expected = 0100000;
        int actual = UnixStat.FILE_FLAG;
        assertEquals("FILE_FLAG should be 0100000", expected, actual);
    }

    @Test
    public void testDIR_FLAG() {
        int expected = 040000;
        int actual = UnixStat.DIR_FLAG;
        assertEquals("DIR_FLAG should be 040000", expected, actual);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        int expected = 0777;
        int actual = UnixStat.DEFAULT_LINK_PERM;
        assertEquals("DEFAULT_LINK_PERM should be 0777", expected, actual);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        int expected = 0755;
        int actual = UnixStat.DEFAULT_DIR_PERM;
        assertEquals("DEFAULT_DIR_PERM should be 0755", expected, actual);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        int expected = 0644;
        int actual = UnixStat.DEFAULT_FILE_PERM;
        assertEquals("DEFAULT_FILE_PERM should be 0644", expected, actual);
    }

    // Edge cases and exception handling tests go here

    @Test(expected = AssertionError.class)
    public void testNegativePERM_MASK() {
        int actual = UnixStat.PERM_MASK & ~07777;
        fail("PERM_MASK should not have negative bits");
    }

    @Test
    public void testZeroPermissions() {
        int expected = 0;
        int actual = UnixStat.PERM_MASK & 0;
        assertEquals("Zero permissions should result in 0", expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLinkFlag() {
        UnixStat.LINK_FLAG = UnixStat.LINK_FLAG - 1;
        fail("LINK_FLAG should not be negative");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFileFlag() {
        UnixStat.FILE_FLAG = UnixStat.FILE_FLAG - 1;
        fail("FILE_FLAG should not be negative");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDirFlag() {
        UnixStat.DIR_FLAG = UnixStat.DIR_FLAG - 1;
        fail("DIR_FLAG should not be negative");
    }

    @Test
    public void testCombinePermissions() {
        int ownerRead = 04;
        int groupRead = 02;
        int othersRead = 01;

        int ownerWrite = 02;
        int groupWrite = 02;
        int othersWrite = 00;

        int expected = UnixStat.PERM_MASK & (ownerRead << 2) | (groupRead << 3) | (othersRead << 4);
        int actual = UnixStat.combinePermissions(ownerRead, groupRead, othersRead);
        assertEquals(" combinePermissions(4, 2, 1) should be " + Integer.toBinaryString(expected) +
                " but got " + Integer.toBinaryString(actual), expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombinePermissionsWithNegativeOwner() {
        int ownerRead = -1;
        int groupRead = 02;
        int othersRead = 01;
        UnixStat.combinePermissions(ownerRead, groupRead, othersRead);
        fail("combinePermissions should throw an IllegalArgumentException with negative owner");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombinePermissionsWithNegativeGroup() {
        int ownerRead = 04;
        int groupRead = -1;
        int othersRead = 01;
        UnixStat.combinePermissions(ownerRead, groupRead, othersRead);
        fail("combinePermissions should throw an IllegalArgumentException with negative group");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombinePermissionsWithNegativeOthers() {
        int ownerRead = 04;
        int groupRead = 02;
        int othersRead = -1;
        UnixStat.combinePermissions(ownerRead, groupRead, othersRead);
        fail("combinePermissions should throw an IllegalArgumentException with negative others");
    }

}