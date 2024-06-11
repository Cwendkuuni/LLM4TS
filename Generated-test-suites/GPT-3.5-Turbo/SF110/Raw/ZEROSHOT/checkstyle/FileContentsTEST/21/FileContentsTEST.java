import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class FileContentsTest {

    @Test
    public void testReportSingleLineComment() {
        FileContents file = new FileContents("test.java");
        file.reportSingleLineComment("type", 1, 1);
        // assert statements
        assertNotNull(file.getCppComments());
    }

    @Test
    public void testReportBlockComment() {
        FileContents file = new FileContents("test.java");
        file.reportBlockComment("type", 1, 1, 2, 2);
        // assert statements
        assertNotNull(file.getCComments());
    }

    @Test
    public void testReportCppComment() {
        FileContents file = new FileContents("test.java");
        file.reportCppComment(1, 1);
        // assert statements
        assertNotNull(file.getCppComments());
    }

    @Test
    public void testGetCppComments() {
        FileContents file = new FileContents("test.java");
        // assert statements
        assertNotNull(file.getCppComments());
    }

    @Test
    public void testReportCComment() {
        FileContents file = new FileContents("test.java");
        file.reportCComment(1, 1, 2, 2);
        // assert statements
        assertNotNull(file.getCComments());
    }

    @Test
    public void testGetCComments() {
        FileContents file = new FileContents("test.java");
        // assert statements
        assertNotNull(file.getCComments());
    }

    @Test
    public void testExtractCComment() {
        FileContents file = new FileContents("test.java");
        String[] result = file.extractCComment(1, 1, 2, 2);
        // assert statements
        assertEquals(2, result.length);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents file = new FileContents("test.java");
        TextBlock result = file.getJavadocBefore(1);
        // assert statements
        assertNull(result);
    }

    @Test
    public void testLine() {
        FileContents file = new FileContents("test.java");
        String result = file.line(1);
        // assert statements
        assertNotNull(result);
    }

    @Test
    public void testGetText() {
        FileContents file = new FileContents("test.java");
        FileText result = file.getText();
        // assert statements
        assertNotNull(result);
    }

    @Test
    public void testGetLines() {
        FileContents file = new FileContents("test.java");
        String[] result = file.getLines();
        // assert statements
        assertNotNull(result);
    }

    @Test
    public void testGetLine() {
        FileContents file = new FileContents("test.java");
        String result = file.getLine(1);
        // assert statements
        assertNotNull(result);
    }

    @Test
    public void testGetFileName() {
        FileContents file = new FileContents("test.java");
        String result = file.getFileName();
        // assert statements
        assertNotNull(result);
    }

    @Test
    public void testGetFilename() {
        FileContents file = new FileContents("test.java");
        String result = file.getFilename();
        // assert statements
        assertNotNull(result);
    }

    @Test
    public void testLineIsBlank() {
        FileContents file = new FileContents("test.java");
        boolean result = file.lineIsBlank(1);
        // assert statements
        assertFalse(result);
    }

    @Test
    public void testLineIsComment() {
        FileContents file = new FileContents("test.java");
        boolean result = file.lineIsComment(1);
        // assert statements
        assertFalse(result);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents file = new FileContents("test.java");
        boolean result = file.hasIntersectionWithComment(1, 1, 2, 2);
        // assert statements
        assertFalse(result);
    }

    @Test
    public void testInPackageInfo() {
        FileContents file = new FileContents("test.java");
        boolean result = file.inPackageInfo();
        // assert statements
        assertFalse(result);
    }

    @Test
    public void testHasIntersectionWithCComment() {
        FileContents file = new FileContents("test.java");
        boolean result = file.hasIntersectionWithCComment(1, 1, 2, 2);
        // assert statements
        assertFalse(result);
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        FileContents file = new FileContents("test.java");
        boolean result = file.hasIntersectionWithCppComment(1, 1, 2, 2);
        // assert statements
        assertFalse(result);
    }
}