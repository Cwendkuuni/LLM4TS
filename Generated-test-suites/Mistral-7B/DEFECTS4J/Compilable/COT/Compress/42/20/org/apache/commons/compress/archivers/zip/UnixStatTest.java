package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Link flag has incorrect value", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("File flag has incorrect value", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Directory flag has incorrect value", expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Default link permissions have incorrect value", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Default directory permissions have incorrect value", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Default file permissions have incorrect value", expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMethodWithNoImplementation() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public int hashCode() {
                throw new UnsupportedOperationException("Unsupported operation");
            }
        };
        unixStat.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameter() {
        String nullValue = null;
        int result = UnixStat.DEFAULT_FILE_PERM & ((1 << Integer.numberOfLeadingZeros(nullValue.length())) | 0xFFFFFFFF);
    }

    @Test
    public void testOutOfRangePermissions() {
        int result = UnixStat.PERM_MASK | (1 << 32);
        assertEquals("Out of range permissions should not affect the value", UnixStat.PERM_MASK, result);
    }
}