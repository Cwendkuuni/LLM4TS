import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java");
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        // Add assertions here
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        // Add assertions here
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 1);
        // Add assertions here
    }

    @Test
    public void testGetCppComments() {
        // Add assertions here
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 1, 2, 2);
        // Add assertions here
    }

    @Test
    public void testGetCComments() {
        // Add assertions here
    }

    @Test
    public void testGetJavadocBefore() {
        // Add assertions here
    }

    @Test
    public void testGetText() {
        // Add assertions here
    }

    @Test
    public void testGetLines() {
        // Add assertions here
    }

    @Test
    public void testGetLine() {
        // Add assertions here
    }

    @Test
    public void testGetFileName() {
        // Add assertions here
    }

    @Test
    public void testGetFilename() {
        // Add assertions here
    }

    @Test
    public void testLineIsBlank() {
        // Add assertions here
    }

    @Test
    public void testLineIsComment() {
        // Add assertions here
    }

    @Test
    public void testHasIntersectionWithComment() {
        // Add assertions here
    }

    @Test
    public void testInPackageInfo() {
        // Add assertions here
    }

    @Test
    public void testHasIntersectionWithCComment() {
        // Add assertions here
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        // Add assertions here
    }
}