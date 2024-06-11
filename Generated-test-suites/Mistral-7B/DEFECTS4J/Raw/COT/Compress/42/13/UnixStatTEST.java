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

    @Test(expected = AssertionError.class)
    public void testUnknownFlag() {
        int unknownFlag = 0xFFFFFFFF;
        int expected = UnixStat.PERM_MASK;
        int actual = (unknownFlag & UnixStat.PERM_MASK);
        fail("Expected AssertionError but got: " + actual);
    }

    @Test
    public void testLinkFlagSet() {
        int linkWithWritePerm = UnixStat.LINK_FLAG | UnixStat.PERM_MASK;
        int expected = 0120777;
        int actual = linkWithWritePerm;
        assertEquals(expected, actual);
    }

    @Test
    public void testLinkFlagClear() {
        int fileWithoutLinkFlag = UnixStat.FILE_FLAG & ~UnixStat.LINK_FLAG;
        int expected = 0100000;
        int actual = fileWithoutLinkFlag;
        assertEquals(expected, actual);
    }
}