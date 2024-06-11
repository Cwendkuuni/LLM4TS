
import org.junit.*;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithStringArray() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "line 2", "line 3");
        assertNotNull(fileContents);
        assertEquals("test.txt", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        FileText fileText = new FileText(new File("test.txt"));
        FileContents fileContents = new FileContents(fileText);
        assertNotNull(fileContents);
        assertEquals("test.txt", fileContents.getFileName());
        assertSame(fileText, fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "line 2", "line 3");
        fileContents.reportSingleLineComment("type", 2, 2);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        assertNotNull(cppComments.get(2));
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "line 2", "line 3");
        fileContents.reportBlockComment("type", 1, 1, 3, 3);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertNotNull(cComments.get(1));
    }

    // Continue with similar test methods for the remaining public methods in FileContents

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "", "line 3");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.txt", "// line 1", "line 2", "/* block comment */");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.txt", "line 1", "// line 2", "/* block comment */");
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 1, 5));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line 1", "line 2", "line 3");
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("test.txt", "line 1", "line 2", "line 3");
        assertFalse(fileContents.inPackageInfo());
    }

}

