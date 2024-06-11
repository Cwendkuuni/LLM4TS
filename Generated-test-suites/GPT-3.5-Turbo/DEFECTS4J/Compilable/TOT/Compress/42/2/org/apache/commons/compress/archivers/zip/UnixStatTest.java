package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        // Typical use case
        assertEquals(07777, UnixStat.PERM_MASK);

        // Edge case
        assertNotEquals(0, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        // Typical use case
        assertEquals(0120000, UnixStat.LINK_FLAG);

        // Edge case
        assertNotEquals(0, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        // Typical use case
        assertEquals(0100000, UnixStat.FILE_FLAG);

        // Edge case
        assertNotEquals(0, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        // Typical use case
        assertEquals(040000, UnixStat.DIR_FLAG);

        // Edge case
        assertNotEquals(0, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        // Typical use case
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);

        // Edge case
        assertNotEquals(0, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        // Typical use case
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);

        // Edge case
        assertNotEquals(0, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        // Typical use case
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);

        // Edge case
        assertNotEquals(0, UnixStat.DEFAULT_FILE_PERM);
    }
}