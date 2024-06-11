import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = { "line 1", "// single line comment", "line 3" };
        fileContents = new FileContents("test.java", lines);
    }

    @Test
    public void testFileContentsConstructorWithFileNameAndLines() {
        assertEquals("test.java", fileContents.getFileName());
        String[] expectedLines = { "line 1", "// single line comment", "line 3" };
        assertArrayEquals(expectedLines, fileContents.getLines());
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        FileText text = fileContents.getText();
        assertEquals("test.java", text.getFile().toString());
        assertEquals("line 1", text.get(0));
        assertEquals("// single line comment", text.get(1));
        assertEquals("line 3", text.get(2));
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 10);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(2));
        Comment comment = (Comment) cppComments.get(2);
        assertEquals("// single line comment", comment.getComment()[0]);
        assertEquals(10, comment.getStartColNo());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(26, comment.getEndColNo());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(2, 3, 2, 20);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(2));
        List<TextBlock> comments = cComments.get(2);
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("// single line comment", comment.getComment()[0]);
        assertEquals(3, comment.getStartColNo());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(20, comment.getEndColNo());
        assertEquals(2, comment.getEndLineNo());
    }

    @Test
    public void testExtractCCommentWithSingleLineComment() {
        String[] cComment = fileContents.extractCComment(2, 3, 2, 20);
        assertEquals(1, cComment.length);
        assertEquals("// single line comment", cComment[0]);
    }

    @Test
    public void testExtractCCommentWithMultipleLineComment() {
        String[] cComment = fileContents.extractCComment(2, 3, 3, 10);
        assertEquals(2, cComment.length);
        assertEquals("// single line comment", cComment[0]);
        assertEquals("line 3", cComment[1]);
    }

    @Test
    public void testGetJavadocBeforeWithValidLineNo() {
        fileContents.reportCComment(1, 0, 2, 20);
        fileContents.reportCComment(4, 0, 4, 20);
        TextBlock javadocComment = fileContents.getJavadocBefore(3);
        assertNull(javadocComment);
        javadocComment = fileContents.getJavadocBefore(5);
        assertNotNull(javadocComment);
        Comment comment = (Comment) javadocComment;
        assertEquals("// single line comment", comment.getComment()[0]);
        assertEquals(0, comment.getStartColNo());
        assertEquals(4, comment.getStartLineNo());
        assertEquals(20, comment.getEndColNo());
        assertEquals(4, comment.getEndLineNo());
    }

    @Test(expected = NullPointerException.class)
    public void testGetJavadocBeforeWithInvalidLineNo() {
        fileContents.getJavadocBefore(0);
    }

    @Test
    public void testLineIsBlankWithBlankLine() {
        assertTrue(fileContents.lineIsBlank(0));
    }

    @Test
    public void testLineIsBlankWithoutBlankLine() {
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsCommentWithCommentLine() {
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testLineIsCommentWithoutCommentLine() {
        assertFalse(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithCommentWithIntersectionInCComment() {
        fileContents.reportCComment(1, 0, 2, 20);
        assertTrue(fileContents.hasIntersectionWithComment(1, 5, 2, 10));
    }

    @Test
    public void testHasIntersectionWithCommentWithIntersectionInCppComment() {
        fileContents.reportCppComment(2, 10);
        assertTrue(fileContents.hasIntersectionWithComment(2, 15, 2, 20));
    }

    @Test
    public void testHasIntersectionWithCommentWithoutIntersection() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 0, 2, 5));
    }

    @Test
    public void testInPackageInfoWithPackageInfoFile() {
        fileContents = new FileContents("package-info.java", "line 1", "line 2");
        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testInPackageInfoWithoutPackageInfoFile() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testPatternForSingleLineComment() {
        Pattern pattern = Pattern.compile("^\\s*//.*$");
        assertTrue(pattern.matcher("// single line comment").matches());
        assertFalse(pattern.matcher("/* multiline comment */").matches());
    }
}