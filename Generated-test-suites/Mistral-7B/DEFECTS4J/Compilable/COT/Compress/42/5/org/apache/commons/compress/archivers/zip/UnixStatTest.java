package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testPERM_MASK() {
        int expectedValue = 07777;
        int actualValue = UnixStat.PERM_MASK;
        assertEquals("PERM_MASK should be equal to 07777", expectedValue, actualValue);
    }

    @Test
    public void testLINK_FLAG() {
        int expectedValue = 0120000;
        int actualValue = UnixStat.LINK_FLAG;
        assertEquals("LINK_FLAG should be equal to 0120000", expectedValue, actualValue);
    }

    @Test
    public void testFILE_FLAG() {
        int expectedValue = 0100000;
        int actualValue = UnixStat.FILE_FLAG;
        assertEquals("FILE_FLAG should be equal to 0100000", expectedValue, actualValue);
    }

    @Test
    public void testDIR_FLAG() {
        int expectedValue = 040000;
        int actualValue = UnixStat.DIR_FLAG;
        assertEquals("DIR_FLAG should be equal to 040000", expectedValue, actualValue);
    }

    @Test
    public void testDEFAULT_LINK_PERM() {
        int expectedValue = 0777;
        int actualValue = UnixStat.DEFAULT_LINK_PERM;
        assertEquals("DEFAULT_LINK_PERM should be equal to 0777", expectedValue, actualValue);
    }

    @Test
    public void testDEFAULT_DIR_PERM() {
        int expectedValue = 0755;
        int actualValue = UnixStat.DEFAULT_DIR_PERM;
        assertEquals("DEFAULT_DIR_PERM should be equal to 0755", expectedValue, actualValue);
    }

    @Test
    public void testDEFAULT_FILE_PERM() {
        int expectedValue = 0644;
        int actualValue = UnixStat.DEFAULT_FILE_PERM;
        assertEquals("DEFAULT_FILE_PERM should be equal to 0644", expectedValue, actualValue);
    }

    // Add edge cases and exception handling test cases here

}