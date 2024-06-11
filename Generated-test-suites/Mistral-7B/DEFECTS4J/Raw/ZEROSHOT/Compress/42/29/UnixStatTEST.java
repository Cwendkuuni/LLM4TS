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

        // Test link flag
        assertEquals(0b111110111111111, linkFlag);

        // Test file flag
        assertEquals(0b101111111111111, fileFlag);

        // Test dir flag
        assertEquals(0b101011000000111, dirFlag);

        // Test permMask
        assertEquals(0b1111111111111111, permMask);
    }

    @Test
    public void testDefaultPermissions() {
        // Test default permissions for symbolic links
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals(0777, defaultLinkPerm);

        // Test default permissions for directories
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals(0755, defaultDirPerm);

        // Test default permissions for plain files
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals(0644, defaultFilePerm);
    }
}