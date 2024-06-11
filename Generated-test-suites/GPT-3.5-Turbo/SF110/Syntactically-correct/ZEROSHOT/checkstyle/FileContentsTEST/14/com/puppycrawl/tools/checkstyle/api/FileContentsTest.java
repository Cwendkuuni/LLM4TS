import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileContentsTest {
    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java");
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(1));
        assertEquals("type", cppComments.get(1).getType());
        assertEquals(1, cppComments.get(1).getStartLineNo());
        assertEquals(1, cppComments.get(1).getStartColNo());
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(1));
        List<TextBlock> comments = cComments.get(1);
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("type", comments.get(0).getType());
        assertEquals(1, comments.get(0).getStartLineNo());
        assertEquals(1, comments.get(0).getStartColNo());
        assertEquals(2, comments.get(0).getEndLineNo());
        assertEquals(2, comments.get(0).getEndColNo());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        TextBlock javadocComment = new TextBlock("javadoc", 0, 0);
        fileContents.getJavadocComments().put(0, javadocComment);
        assertEquals(javadocComment, fileContents.getJavadocBefore(3));
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        fileContents.reportSingleLineComment("type", 1, 1);
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }

    @Test
    public void testInPackageInfo() {
        fileContents = new FileContents("package-info.java");
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("testFile.java");
        assertFalse(fileContents.inPackageInfo());
    }
}