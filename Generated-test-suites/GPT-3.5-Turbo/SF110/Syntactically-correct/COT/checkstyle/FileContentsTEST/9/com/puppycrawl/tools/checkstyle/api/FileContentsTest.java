import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    // Test cases for constructor with filename and lines parameters

    @Test
    public void testConstructorWithFilenameAndLines() {
        fileContents = new FileContents("filename", "line1", "line2");
        assertEquals("filename", fileContents.getFileName());
        assertArrayEquals(new String[]{"line1", "line2"}, fileContents.getLines());
    }

    // Test cases for constructor with FileText parameter

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("filename"), Arrays.asList("line1", "line2"));
        fileContents = new FileContents(fileText);
        assertEquals("filename", fileContents.getFileName());
        assertArrayEquals(new String[]{"line1", "line2"}, fileContents.getLines());
    }

    // Test case for reportSingleLineComment method

    @Test
    public void testReportSingleLineComment() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportSingleLineComment("type", 1, 1);
        assertNotNull(fileContents.getCppComments().get(1));
    }

    // Test case for reportBlockComment method

    @Test
    public void testReportBlockComment() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertNotNull(fileContents.getCComments().get(1));
    }

    // Test case for reportCppComment method

    @Test
    public void testReportCppComment() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportCppComment(1, 1);
        assertNotNull(fileContents.getCppComments().get(1));
    }

    // Test case for getCppComments method

    @Test
    public void testGetCppComments() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportCppComment(1, 1);
        assertEquals(1, fileContents.getCppComments().size());
    }

    // Test case for reportCComment method

    @Test
    public void testReportCComment() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportCComment(1, 1, 2, 2);
        assertNotNull(fileContents.getCComments().get(1));
    }

    // Test case for getCComments method

    @Test
    public void testGetCComments() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportCComment(1, 1, 2, 2);
        assertEquals(1, fileContents.getCComments().size());
    }

    // Test case for getJavadocBefore method

    @Test
    public void testGetJavadocBefore() {
        fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportCComment(1, 1, 2, 2);
        assertNotNull(fileContents.getJavadocBefore(2));
    }

    // Test case for getText method

    @Test
    public void testGetText() {
        fileContents = new FileContents("filename", "line1", "line2");
        FileText fileText = fileContents.getText();
        assertNotNull(fileText);
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
    }

    // Test case for getLines method

    @Test
    public void testGetLines() {
        fileContents = new FileContents("filename", "line1", "line2");
        assertArrayEquals(new String[]{"line1", "line2"}, fileContents.getLines());
    }

    // Test case for getLine method

    @Test
    public void testGetLine() {
        fileContents = new FileContents("filename", "line1", "line2");
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }

    // Test case for getFileName method

    @Test
    public void testGetFileName() {
        fileContents = new FileContents("filename", "line1", "line2");
        assertEquals("filename", fileContents.getFileName());
    }

    // Test case for getFilename method (deprecated)

    @Test
    public void testGetFilename() {
        fileContents = new FileContents("filename", "line1", "line2");
        assertEquals("filename", fileContents.getFilename());
    }

    // Test case for lineIsBlank method

    @Test
    public void testLineIsBlank() {
        fileContents = new FileContents("filename", "line1", "", "line3");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    // Test case for lineIsComment method

    @Test
    public void testLineIsComment() {
        fileContents = new FileContents("filename", "// comment", "line2");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    // Test case for hasIntersectionWithComment method

    @Test
    public void testHasIntersectionWithComment() {
        fileContents = new FileContents("filename", "line1", "line2");
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }

    // Test case for inPackageInfo method

    @Test
    public void testInPackageInfo() {
        fileContents = new FileContents("package-info.java", "line1", "line2");
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("filename", "line1", "line2");
        assertFalse(fileContents.inPackageInfo());
    }

}