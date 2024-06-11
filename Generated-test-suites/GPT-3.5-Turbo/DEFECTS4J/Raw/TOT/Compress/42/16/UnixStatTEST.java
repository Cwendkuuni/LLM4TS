import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    private UnixStat unixStat;

    @Before
    public void setUp() {
        unixStat = new UnixStatImplementation();
    }

    @Test
    public void testPERM_MASK() {
        assertEquals(07777, unixStat.PERM_MASK);
    }

    @Test
    public void testLINK_FLAG() {
        assertEquals(0120000, unixStat.LINK_FLAG);
    }

    @Test
    public void testFILE_FLAG() {
        assertEquals(0100000, unixStat.FILE_FLAG);
    }

    @Test
    public void testDIR_FLAG() {
        assertEquals(040000, unixStat.DIR_FLAG);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        assertEquals(0777, unixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        assertEquals(0755, unixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        assertEquals(0644, unixStat.DEFAULT_FILE_PERM);
    }

}