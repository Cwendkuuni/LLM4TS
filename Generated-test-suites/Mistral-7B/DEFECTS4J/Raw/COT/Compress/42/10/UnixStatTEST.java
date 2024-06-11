import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testGetPermsMask() {
        UnixStat unixStat = new UnixStat();
        int permsMask = unixStat.PERM_MASK;
        assertEquals(07777, permsMask);
    }

    @Test
    public void testGetLinkFlag() {
        UnixStat unixStat = new UnixStat();
        int linkFlag = unixStat.LINK_FLAG;
        assertEquals(0120000, linkFlag);
    }

    @Test
    public void testGetFileFlag() {
        UnixStat unixStat = new UnixStat();
        int fileFlag = unixStat.FILE_FLAG;
        assertEquals(0100000, fileFlag);
    }

    @Test
    public void testGetDirFlag() {
        UnixStat unixStat = new UnixStat();
        int dirFlag = unixStat.DIR_FLAG;
        assertEquals(040000, dirFlag);
    }

    @Test
    public void testGetDefaultLinkPerm() {
        UnixStat unixStat = new UnixStat();
        int defaultLinkPerm = unixStat.DEFAULT_LINK_PERM;
        assertEquals(0777, defaultLinkPerm);
    }

    @Test
    public void testGetDefaultDirPerm() {
        UnixStat unixStat = new UnixStat();
        int defaultDirPerm = unixStat.DEFAULT_DIR_PERM;
        assertEquals(0755, defaultDirPerm);
    }

    @Test
    public void testGetDefaultFilePerm() {
        UnixStat unixStat = new UnixStat();
        int defaultFilePerm = unixStat.DEFAULT_FILE_PERM;
        assertEquals(0644, defaultFilePerm);
    }

    // Edge cases testing

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        UnixStat unixStat = new UnixStat() {
            @Override
            public void someUnsupportedMethod() {
                // Do something here
            }
        };
        unixStat.someUnsupportedMethod();
    }

    @Test
    public void testDefaultPermissionsForDifferentDataTypes() {
        UnixStat unixStat = new UnixStat();
        int defaultLinkPermForByte = unixStat.getDefaultLinkPerm();
        int defaultDirPermForShort = unixStat.getDefaultDirPerm();
        int defaultFilePermForInt = unixStat.getDefaultFilePerm();
        assertEquals(0777, defaultLinkPermForByte);
        assertEquals(0755, defaultDirPermForShort);
        assertEquals(0644, defaultFilePermForInt);
    }
}