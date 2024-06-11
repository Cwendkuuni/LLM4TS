
import com.google.common.collect.*;
import org.junit.Test;
import java.io.File;

public class FileContentsTest {

    @Test
    public void testConstructorWithFilenameAndLines() {
        String filename = "test.txt";
        String[] lines = {"line 1", "line 2"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
    }

    @Test
    public void testConstructorWithFileText() {
        String[] lines = {"line 1", "line 2"};
        FileText fileText = FileText.fromLines(new File("test.txt"), ImmutableList.copyOf(lines));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.txt", fileContents.getFileName());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "// line 2");
        fileContents.reportSingleLineComment("type", 2, 1);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "/**", "* block comment", "*/");
        fileContents.reportBlockComment("type", 2, 1, 4, 2);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "// line 2");
        fileContents.reportCppComment(2, 1);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "// line 2");
        fileContents.reportCppComment(2, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
    }

    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "/**", "* block comment", "*/");
        fileContents.reportCComment(2, 1, 4, 2);
        assertEquals(1, fileContents.getCComments().size());
    }

    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "/**", "* block comment", "*/");
        fileContents.reportCComment(2, 1, 4, 2);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.txt", "/**", "* javadoc comment", "*/", "line 4");
        TextBlock javadoc = fileContents.getJavadocBefore(4);
        assertNotNull(javadoc);
    }

    @Test
    public void testGetText() {
        String[] lines = {"line 1", "line 2"};
        FileText fileText = FileText.fromLines(new File("test.txt"), ImmutableList.copyOf(lines));
        FileContents fileContents = new FileContents(fileText);
        FileText text = fileContents.getText();
        assertEquals(fileText, text);
    }

    @Test
    public void testGetLines() {
        String[] lines = {"line 1", "line 2"};
        FileContents fileContents = new FileContents("test.txt", lines);
        String[] fileLines = fileContents.getLines();
        assertArrayEquals(lines, fileLines);
    }

    @Test
    public void testGetLine() {
        String[] lines = {"line 1", "line 2"};
        FileContents fileContents = new FileContents("test.txt", lines);
        String line = fileContents.getLine(1);
        assertEquals("line 2", line);
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.txt");
        String fileName = fileContents.getFileName();
        assertEquals("test.txt", fileName);
    }

    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.txt");
        String fileName = fileContents.getFilename();
        assertEquals("test.txt", fileName);
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.txt", "", "line 2");
        boolean isBlank = fileContents.lineIsBlank(1);
        assertTrue(isBlank);
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.txt", "// line 1", "line 2");
        boolean isComment = fileContents.lineIsComment(1);
        assertTrue(isComment);
    }

    @Test
    public void testHasIntersectionWithComment_CComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "/**", "* block comment", "*/");
        boolean hasIntersection = fileContents.hasIntersectionWithComment(2, 1, 4, 2);
        assertTrue(hasIntersection);
    }

    @Test
    public void testHasIntersectionWithComment_CppComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "// line 2");
        boolean hasIntersection = fileContents.hasIntersectionWithComment(2, 1, 2, 11);
        assertTrue(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        boolean inPackageInfo = fileContents.inPackageInfo();
        assertTrue(inPackageInfo);
    }

    private void assertEquals(Object expected, Object actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        org.junit.Assert.assertArrayEquals(expected, actual);
    }

    private void assertNotNull(Object object) {
        org.junit.Assert.assertNotNull(object);
    }

    private void assertTrue(boolean condition) {
        org.junit.Assert.assertTrue(condition);
    }
}
