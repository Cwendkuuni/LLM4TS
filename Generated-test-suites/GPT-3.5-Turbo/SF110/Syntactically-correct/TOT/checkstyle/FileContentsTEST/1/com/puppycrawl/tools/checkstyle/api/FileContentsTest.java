import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructor_withStringLines() {
        String[] lines = {"line1", "line2", "line3"};
        FileContents fileContents = new FileContents("testFile.txt", lines);
        assertEquals("testFile.txt", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }

    @Test
    public void testFileContentsConstructor_withFileText() {
        FileText fileText = new FileText(new File("testFile.txt"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("testFile.txt", fileContents.getFileName());
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.txt");
        fileContents.reportSingleLineComment("type", 1, 1);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.txt");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile.txt");
        assertEquals(0, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile.txt");
        fileContents.reportCComment(1, 1, 2, 2);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.txt");
        assertNull(fileContents.getJavadocBefore(1));
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile.txt");
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testGetLines() {
        String[] lines = {"line1", "line2", "line3"};
        FileContents fileContents = new FileContents("testFile.txt", lines);
        assertArrayEquals(lines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        String[] lines = {"line1", "line2", "line3"};
        FileContents fileContents = new FileContents("testFile.txt", lines);
        assertEquals("line2", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile.txt");
        assertEquals("testFile.txt", fileContents.getFileName());
    }

    @Test
    public void testLineIsBlank() {
        String[] lines = {"", "line2", "", "line4"};
        FileContents fileContents = new FileContents("testFile.txt", lines);
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        String[] lines = {"// comment", "line2", "/* comment */", "line4"};
        FileContents fileContents = new FileContents("testFile.txt", lines);
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment_withNoIntersection() {
        FileContents fileContents = new FileContents("testFile.txt");
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }

    @Test
    public void testHasIntersectionWithComment_withCppCommentIntersection() {
        FileContents fileContents = new FileContents("testFile.txt");
        fileContents.reportCppComment(1, 1);
        assertTrue(fileContents.hasIntersectionWithComment(1, 4, 2, 2));
    }

    @Test
    public void testHasIntersectionWithComment_withCCommentIntersection() {
        FileContents fileContents = new FileContents("testFile.txt");
        fileContents.reportCComment(1, 1, 3, 2);
        assertTrue(fileContents.hasIntersectionWithComment(2, 1, 2, 2));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("testFile.txt");
        assertFalse(fileContents.inPackageInfo());
    }

}