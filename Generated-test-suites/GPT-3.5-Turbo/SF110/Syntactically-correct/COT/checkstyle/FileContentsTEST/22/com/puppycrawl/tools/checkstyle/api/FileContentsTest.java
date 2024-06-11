
import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorLines() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        assertEquals("test.java", fileContents.getFileName());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileContents.getLines());
    }

    @Test
    public void testFileContentsConstructorText() {
        FileText fileText = new FileText("test.java", Arrays.asList("line1", "line2", "line3"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.java", fileContents.getFileName());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        fileContents.reportSingleLineComment("type", 2, 0);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        Comment comment = (Comment) cppComments.get(2);
        assertEquals("line2", comment.getText()[0]);
        assertEquals(0, comment.getStartColumnNo());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(4, comment.getEndColumnNo());
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        fileContents.reportBlockComment("type", 1, 0, 3, 4);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        List<TextBlock> comments = cComments.get(1);
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, comment.getText());
        assertEquals(0, comment.getStartColumnNo());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(4, comment.getEndColumnNo());
        assertEquals(3, comment.getEndLineNo());
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        fileContents.reportCppComment(2, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        Comment comment = (Comment) cppComments.get(2);
        assertEquals("ine2", comment.getText()[0]);
        assertEquals(1, comment.getStartColumnNo());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(4, comment.getEndColumnNo());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        fileContents.reportCComment(2, 0, 4, 4);
        TextBlock textBlock = fileContents.getJavadocBefore(3);
        assertEquals("line2", textBlock.getText()[0]);
        assertEquals(0, textBlock.getStartColumnNo());
        assertEquals(2, textBlock.getStartLineNo());
        assertEquals(4, textBlock.getEndColumnNo());
        assertEquals(4, textBlock.getEndLineNo());
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        FileText fileText = fileContents.getText();
        assertEquals("test.java", fileText.getFile().toString());
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileText.toLinesArray());
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        assertEquals("line2", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        assertEquals("test.java", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        assertEquals("test.java", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "line1", "   ", "line3");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "line1", "   // comment", "line3");
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java", "line1", "   // comment", "line3");
        assertTrue(fileContents.hasIntersectionWithComment(2, 2, 2, 9));
        assertFalse(fileContents.hasIntersectionWithComment(1, 2, 1, 9));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line1", "line2", "line3");
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("test.java", "line1", "line2", "line3");
        assertFalse(fileContents.inPackageInfo());
    }
}

