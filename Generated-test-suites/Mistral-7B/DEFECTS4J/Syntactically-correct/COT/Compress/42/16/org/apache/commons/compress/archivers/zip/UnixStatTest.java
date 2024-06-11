import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        int actual = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals("PERM_MASK should be equal to 07777", expected, actual);
    }

    @Test
    public void testLINK_FLAG() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("LINK_FLAG should be equal to 0120000", expected, actual);
    }

    @Test
    public void testFILE_FLAG() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("FILE_FLAG should be equal to 0100000", expected, actual);
    }

    @Test
    public void testDIR_FLAG() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("DIR_FLAG should be equal to 040000", expected, actual);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("DEFAULT_LINK_PERM should be equal to 0777", expected, actual);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("DEFAULT_DIR_PERM should be equal to 0755", expected, actual);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("DEFAULT_FILE_PERM should be equal to 0644", expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidFlagValue() {
        int flag = 0xFFFFFFF;
        UnixStat.permMask(flag);
    }

    @Test
    public void testPermMaskWithValidAndInvalidPermission() {
        int validPerm = UnixStat.PERM_MASK & 0600;
        int invalidPerm = UnixStat.PERM_MASK & 0xFFF0;

        int expectedValidPerm = 0600;
        int expectedInvalidPerm = 00666; // 06000 in binary AND 01111110 in binary

        assertEquals("permMask with valid permission should be equal to 0600", expectedValidPerm, validPerm);
        assertEquals("permMask with invalid permission should be equal to 00666", expectedInvalidPerm, invalidPerm);
    }
}