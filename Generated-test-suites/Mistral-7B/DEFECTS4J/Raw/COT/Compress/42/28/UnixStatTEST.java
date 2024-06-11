import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        int actual = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals(expected, actual);
    }

    @Test
    public void testLINK_FLAG() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, actual);
    }

    @Test
    public void testFILE_FLAG() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDIR_FLAG() {
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

    // Edge cases and exception handling tests

    @Test(expected = UnsupportedOperationException.class)
    public void testMethodWithNoImplementation() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public void someMethod() {
                throw new UnsupportedOperationException();
            }
        };
        unixStat.someMethod();
    }

    @Test
    public void testNegativePermissions() {
        int actual = UnixStat.PERM_MASK & 0xFFFFFF80;
        int expected = 07776; // 777 with lowest 3 bits cleared
        assertEquals(expected, actual);
    }

    @Test
    public void testMaxPermissions() {
        int actual = UnixStat.PERM_MASK | 0xFF;
        int expected = 07777 | 0xFF; // 777 + 255
        assertEquals(expected, actual);
    }
}