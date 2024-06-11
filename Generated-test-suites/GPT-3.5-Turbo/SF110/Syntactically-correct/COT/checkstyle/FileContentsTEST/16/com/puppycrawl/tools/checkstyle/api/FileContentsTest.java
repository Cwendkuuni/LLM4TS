
import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testReportSingleLineComment() {
        FileContents file = new FileContents("test.java");
        file.reportSingleLineComment("type", 1, 1);
        assertEquals(1, file.getCppComments().size());
    }

    @Test
    public void testReportBlockComment() {
        FileContents file = new FileContents("test.java");
        file.reportBlockComment("type", 1, 1, 2, 2);
        assertEquals(1, file.getCComments().size());
    }

    @Test
    public void testReportCppComment() {
        FileContents file = new FileContents("test.java");
        file.reportCppComment(1, 1);
        assertEquals(1, file.getCppComments().size());
    }

    @Test
    public void testGetCppComments() {
        FileContents file = new FileContents("test.java");
        file.reportCppComment(1, 1);
        assertEquals(1, file.getCppComments().size());
    }

    @Test
    public void testReportCComment() {
        FileContents file = new FileContents("test.java");
        file.reportCComment(1, 1, 2, 2);
        assertEquals(1, file.getCComments().size());
    }

    @Test
    public void testGetCComments() {
        FileContents file = new FileContents("test.java");
        file.reportCComment(1, 1, 2, 2);
        assertEquals(1, file.getCComments().size());
    }

    @Test
    public void testExtractCComment() {
        FileContents file = new FileContents("test.java");
        String[] cComment = file.extractCComment(1, 1, 2, 2);
        assertEquals(2, cComment.length);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents file = new FileContents("test.java");
        file.reportCComment(1, 1, 2, 2);
        TextBlock comment = file.getJavadocBefore(3);
        assertNull(comment);
    }

    @Test
    public void testGetText() {
        FileContents file = new FileContents("test.java");
        FileText text = file.getText();
        assertNotNull(text);
    }

    @Test
    public void testGetLines() {
        FileContents file = new FileContents("test.java");
        String[] lines = file.getLines();
        assertNotNull(lines);
    }

    @Test
    public void testGetLine() {
        FileContents file = new FileContents("test.java");
        String line = file.getLine(1);
        assertNotNull(line);
    }

    @Test
    public void testGetFileName() {
        FileContents file = new FileContents("test.java");
        String fileName = file.getFileName();
        assertEquals("test.java", fileName);
    }

    @Deprecated
    @Test
    public void testGetFilename() {
        FileContents file = new FileContents("test.java");
        String fileName = file.getFilename();
        assertEquals("test.java", fileName);
    }

    @Test
    public void testLineIsBlank() {
        FileContents file = new FileContents("test.java");
        boolean isBlank = file.lineIsBlank(1);
        assertFalse(isBlank);
    }

    @Test
    public void testLineIsComment() {
        FileContents file = new FileContents("test.java");
        boolean isComment = file.lineIsComment(1);
        assertFalse(isComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents file = new FileContents("test.java");
        boolean hasIntersection = file.hasIntersectionWithComment(1, 1, 2, 2);
        assertFalse(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        FileContents file = new FileContents("test.java");
        boolean inPackageInfo = file.inPackageInfo();
        assertFalse(inPackageInfo);
    }

    @Test
    public void testHasIntersectionWithCComment() {
        FileContents file = new FileContents("test.java");
        boolean hasIntersection = file.hasIntersectionWithCComment(1, 1, 2, 2);
        assertFalse(hasIntersection);
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        FileContents file = new FileContents("test.java");
        boolean hasIntersection = file.hasIntersectionWithCppComment(1, 1, 2, 2);
        assertFalse(hasIntersection);
    }
}

