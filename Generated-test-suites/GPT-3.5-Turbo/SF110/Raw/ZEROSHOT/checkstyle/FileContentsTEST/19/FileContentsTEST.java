
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.*;
import java.io.*;
import com.google.common.collect.*;
import java.util.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {"line 1", "line 2", "line 3"};
        fileContents = new FileContents("filename", lines);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 0);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(1));

        TextBlock comment = cppComments.get(1);
        assertNotNull(comment);
        assertEquals("line 1", comment.getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(2, 0, 2, 5);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(2));

        List<TextBlock> comments = cComments.get(2);
        assertNotNull(comments);
        assertEquals(1, comments.size());

        TextBlock comment = comments.get(0);
        assertNotNull(comment);
        assertEquals("line 2", comment.getText()[0]);
        assertEquals(0, comment.getStartColumn());
        assertEquals(2, comment.getEndLine());
        assertEquals(5, comment.getEndColumn());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(2, 0, 2, 5);
        TextBlock javadocComment = new Comment(new String[]{"line 1", "line 2"}, 0, 1, 5);
        fileContents.getJavadocComments().put(1, javadocComment);

        TextBlock result = fileContents.getJavadocBefore(2);

        assertNotNull(result);
        assertEquals(javadocComment, result);
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();

        assertNotNull(text);
        assertNotSame(fileContents.getText(), text);
        assertArrayEquals(new String[]{"line 1", "line 2", "line 3"}, text.toLinesArray());
    }

    @Test
    public void testGetLine() {
        assertEquals("line 2", fileContents.getLine(1));
    }

    @Test
    public void testGetFileName() {
        assertEquals("filename", fileContents.getFileName());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 1, 5));
        assertFalse(fileContents.hasIntersectionWithComment(1, 0, 1, 5));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        FileContents packageInfo = new FileContents("package-info.java", "package-info");
        assertTrue(packageInfo.inPackageInfo());
    }
    
    // Add more test cases to cover all methods in the FileContents class

}
