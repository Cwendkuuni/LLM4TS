import org.junit.Test;
import org.junit.Assert.*;
import static org.apache.commons.compress.archivers.zip.UnixStat.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = LINK_FLAG;
        int expected = 0120000;
        assertEquals("Link flag should be correct.", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = FILE_FLAG;
        int expected = 0100000;
        assertEquals("File flag should be correct.", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = DIR_FLAG;
        int expected = 040000;
        assertEquals("Directory flag should be correct.", expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Default link permissions should be correct.", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Default directory permissions should be correct.", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Default file permissions should be correct.", expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidMethod() {
        // This method does not exist in UnixStat interface,
        // it is just to test exception handling
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            int invalidMethod = PERM_MASK << 1;
        });

        String expectedMessage = "Unsupported operation: Unrecognized field value.";
        String actualMessage = exception.getMessage();
        assertTrue("Unexpected exception message.", actualMessage.contains(expectedMessage));
    }

    @Test
    public void testMaskValue() {
        int actual = PERM_MASK;
        int expected = 07777;
        assertEquals("Mask value should be correct.", expected, actual);
    }
}