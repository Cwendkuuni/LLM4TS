import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnixStatTest {

    private UnixStat unixStat;

    @Before
    public void setUp() {
        unixStat = new UnixStat();
    }

    // Test for PERM_MASK
    @Test
    public void testPermMask() {
        // Typical use case
        assertEquals(07777, unixStat.PERM_MASK);

        // Edge case
        assertEquals(0, unixStat.PERM_MASK & 00000);

        // Error scenario
        assertNotEquals(0666, unixStat.PERM_MASK);
    }

    // Test for LINK_FLAG
    @Test
    public void testLinkFlag() {
        // Typical use case
        assertEquals(0120000, unixStat.LINK_FLAG);

        // Edge case
        assertEquals(0100000, unixStat.LINK_FLAG & 0100000);

        // Error scenario
        assertNotEquals(040000, unixStat.LINK_FLAG);
    }

    // Test for FILE_FLAG
    @Test
    public void testFileFlag() {
        // Typical use case
        assertEquals(0100000, unixStat.FILE_FLAG);

        // Edge case
        assertEquals(0, unixStat.FILE_FLAG & 00000);

        // Error scenario
        assertNotEquals(0120000, unixStat.FILE_FLAG);
    }

    // Test for DIR_FLAG
    @Test
    public void testDirFlag() {
        // Typical use case
        assertEquals(040000, unixStat.DIR_FLAG);

        // Edge case
        assertEquals(0, unixStat.DIR_FLAG & 00000);

        // Error scenario
        assertNotEquals(0100000, unixStat.DIR_FLAG);
    }

    // Test for DEFAULT_LINK_PERM
    @Test
    public void testDefaultLinkPerm() {
        // Typical use case
        assertEquals(0777, unixStat.DEFAULT_LINK_PERM);

        // Edge case
        assertEquals(0, unixStat.DEFAULT_LINK_PERM & 00000);

        // Error scenario
        assertNotEquals(0755, unixStat.DEFAULT_LINK_PERM);
    }

    // Test for DEFAULT_DIR_PERM
    @Test
    public void testDefaultDirPerm() {
        // Typical use case
        assertEquals(0755, unixStat.DEFAULT_DIR_PERM);

        // Edge case
        assertEquals(0, unixStat.DEFAULT_DIR_PERM & 00000);

        // Error scenario
        assertNotEquals(0777, unixStat.DEFAULT_DIR_PERM);
    }

    // Test for DEFAULT_FILE_PERM
    @Test
    public void testDefaultFilePerm() {
        // Typical use case
        assertEquals(0644, unixStat.DEFAULT_FILE_PERM);

        // Edge case
        assertEquals(0, unixStat.DEFAULT_FILE_PERM & 00000);

        // Error scenario
        assertNotEquals(0777, unixStat.DEFAULT_FILE_PERM);
    }
}