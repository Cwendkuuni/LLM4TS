import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {"Line 1", "Line 2", "Line 3"};
        fileContents = new FileContents("testFile.txt", lines);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 0);
        assertEquals("Line 1", fileContents.getCppComments().get(1).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 0, 1, 6);
        assertEquals("Line 1", fileContents.getCComments().get(1).get(0).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 0, 1, 6);
        assertNull(fileContents.getJavadocBefore(1));
        fileContents.reportCComment(2, 0, 2, 6);
        assertNotNull(fileContents.getJavadocBefore(2));
        assertEquals("Line 1", fileContents.getJavadocBefore(2).getText()[0]);
    }

    @Test
    public void testGetLine() {
        assertEquals("Line 1", fileContents.getLine(0));
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.txt", fileContents.getFileName());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        fileContents.reportCppComment(1, 0);
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 1, 5));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        FileContents packageInfo = new FileContents("package-info.java", "Line 1");
        assertTrue(packageInfo.inPackageInfo());
    }
}