import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;

public class FileContentsTest {

    private static final String FILE_NAME = "test.txt";
    private static final String[] LINES = {"Line 1", "Line 2", "Line 3"};

    @Test
    public void testConstructorWithFilenameAndLines() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        assertEquals(FILE_NAME, fileContents.getFileName());
        assertArrayEquals(LINES, fileContents.getLines());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = FileText.fromLines(new File(FILE_NAME), Arrays.asList(LINES));
        FileContents fileContents = new FileContents(fileText);
        assertEquals(FILE_NAME, fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        fileContents.reportSingleLineComment("type", 1, 2);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(1));
        TextBlock comment = cppComments.get(1);
        assertEquals("ne 1", comment.getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        fileContents.reportBlockComment("type", 1, 2, 3, 4);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(1));
        List<TextBlock> comments = cComments.get(1);
        assertEquals(1, comments.size());
        TextBlock comment = comments.get(0);
        assertEquals("Line 2", comment.getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        fileContents.reportBlockComment("type", 1, 2, 3, 4);
        TextBlock javadocBefore = fileContents.getJavadocBefore(2);
        assertNull(javadocBefore);
        fileContents.reportSingleLineComment("type", 2, 2);
        javadocBefore = fileContents.getJavadocBefore(3);
        assertNotNull(javadocBefore);
        assertEquals("ne 2", javadocBefore.getText()[0]);
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        FileText fileText = fileContents.getText();
        assertEquals(FILE_NAME, fileText.getFile().toString());
        assertArrayEquals(LINES, fileText.toLinesArray());
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        assertEquals("Line 2", fileContents.getLine(1));
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents(FILE_NAME, LINES);
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 1, 1));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", LINES);
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("test.java", LINES);
        assertFalse(fileContents.inPackageInfo());
    }
}