import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test UnixStat constants
        int permMask = UnixStat.PERM_MASK;
        int linkFlag = UnixStat.LINK_FLAG;
        int fileFlag = UnixStat.FILE_FLAG;
        int dirFlag = UnixStat.DIR_FLAG;
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;

        assertEquals(07777, permMask);
        assertEquals(0120000, linkFlag);
        assertEquals(0100000, fileFlag);
        assertEquals(040000, dirFlag);
        assertEquals(0777, defaultLinkPerm);
        assertEquals(0755, defaultDirPerm);
        assertEquals(0644, defaultFilePerm);
    }
}