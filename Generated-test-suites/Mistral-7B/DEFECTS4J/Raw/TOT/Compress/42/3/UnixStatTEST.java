import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int unixStat = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Link flag should have the correct value", expected, unixStat);
    }

    @Test
    public void testFileFlag() {
        int unixStat = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("File flag should have the correct value", expected, unixStat);
    }

    @Test
    public void testDirFlag() {
        int unixStat = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Directory flag should have the correct value", expected, unixStat);
    }

    @Test
    public void testDefaultLinkPerm() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Default link permission should have the correct value", expected, defaultLinkPerm);
    }

    @Test
    public void testDefaultDirPerm() {
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Default directory permission should have the correct value", expected, defaultDirPerm);
    }

    @Test
    public void testDefaultFilePerm() {
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Default file permission should have the correct value", expected, defaultFilePerm);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidBitMask() {
        int invalidMask = 0xFFFF0000;
        int expectedException = 0;
        int actual = UnixStat.PERM_MASK & invalidMask;
    }
}