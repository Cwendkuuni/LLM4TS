import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Testing constants for permissions
        int expectedPermMask = 07777;
        assertEquals("Unexpected PERM_MASK value", expectedPermMask, UnixStat.PERM_MASK);

        // Testing constants for file type flags
        int expectedLinkFlag = 0120000;
        assertEquals("Unexpected LINK_FLAG value", expectedLinkFlag, UnixStat.LINK_FLAG);
        int expectedFileFlag = 0100000;
        assertEquals("Unexpected FILE_FLAG value", expectedFileFlag, UnixStat.FILE_FLAG);
        int expectedDirFlag = 040000;
        assertEquals("Unexpected DIR_FLAG value", expectedDirFlag, UnixStat.DIR_FLAG);

        // Testing default permissions
        int expectedDefaultLinkPerm = 0777;
        assertEquals("Unexpected DEFAULT_LINK_PERM value", expectedDefaultLinkPerm, UnixStat.DEFAULT_LINK_PERM);
        int expectedDefaultDirPerm = 0755;
        assertEquals("Unexpected DEFAULT_DIR_PERM value", expectedDefaultDirPerm, UnixStat.DEFAULT_DIR_PERM);
        int expectedDefaultFilePerm = 0644;
        assertEquals("Unexpected DEFAULT_FILE_PERM value", expectedDefaultFilePerm, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidUnixStatConstant() {
        int invalidConstant = 0;
        fail("Unexpected valid constant value: " + invalidConstant);
    }

    @Test
    public void testGetFileTypeBitMask() {
        int fileType = UnixStat.FILE_FLAG | UnixStat.DIR_FLAG;
        int expectedFileType = 0111001;
        assertEquals("Unexpected file type value", expectedFileType, fileType);
    }
}