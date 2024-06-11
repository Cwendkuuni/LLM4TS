import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        // Test typical use case
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLINK_FLAG() {
        // Test typical use case
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFILE_FLAG() {
        // Test typical use case
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDIR_FLAG() {
        // Test typical use case
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        // Test typical use case
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        // Test typical use case
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        // Test typical use case
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testNegativePERM_MASK() {
        // Test negative value (error scenario)
        assertNotEquals(-1, UnixStat.PERM_MASK);
    }

    @Test
    public void testInvalidLINK_FLAG() {
        // Test invalid value (error scenario)
        assertNotEquals(1234, UnixStat.LINK_FLAG);
    }

    @Test
    public void testInvalidFILE_FLAG() {
        // Test invalid value (error scenario)
        assertNotEquals(5678, UnixStat.FILE_FLAG);
    }

    @Test
    public void testInvalidDIR_FLAG() {
        // Test invalid value (error scenario)
        assertNotEquals(9012, UnixStat.DIR_FLAG);
    }

    @Test
    public void testInvalidDEFAULT_LINK_PERM() {
        // Test invalid value (error scenario)
        assertNotEquals(9999, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testInvalidDEFAULT_DIR_PERM() {
        // Test invalid value (error scenario)
        assertNotEquals(8888, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testInvalidDEFAULT_FILE_PERM() {
        // Test invalid value (error scenario)
        assertNotEquals(7777, UnixStat.DEFAULT_FILE_PERM);
    }
}