package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int linkFlag = UnixStat.LINK_FLAG;
        assertEquals("Link flag value is incorrect.", 0120000, linkFlag);
    }

    @Test
    public void testFileFlag() {
        int fileFlag = UnixStat.FILE_FLAG;
        assertEquals("File flag value is incorrect.", 0100000, fileFlag);
    }

    @Test
    public void testDirFlag() {
        int dirFlag = UnixStat.DIR_FLAG;
        assertEquals("Directory flag value is incorrect.", 040000, dirFlag);
    }

    // Test other constants similarly

    // Test methods for edge cases and exceptions (if any) would be added here
}