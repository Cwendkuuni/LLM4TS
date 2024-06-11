import static org.junit.Assert.*;
import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int perm = UnixStat.PERM_MASK;
        assertEquals(07777, perm);
    }

    @Test
    public void testLinkFlag() {
        int flag = UnixStat.LINK_FLAG;
        assertEquals(0120000, flag);
    }

    @Test
    public void testFileFlag() {
        int flag = UnixStat.FILE_FLAG;
        assertEquals(0100000, flag);
    }

    @Test
    public void testDirFlag() {
        int flag = UnixStat.DIR_FLAG;
        assertEquals(040000, flag);
    }

    @Test
    public void testDefaultLinkPerm() {
        int perm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals(0777, perm);
    }

    @Test
    public void testDefaultDirPerm() {
        int perm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals(0755, perm);
    }

    @Test
    public void testDefaultFilePerm() {
        int perm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals(0644, perm);
    }

}