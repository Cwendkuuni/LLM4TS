

import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = { "Line 1", "Line 2", "Line 3" };
        fileContents = new FileContents("testfile.txt", lines);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fileContents);
        assertEquals("testfile.txt", fileContents.getFileName());
        assertEquals(3, fileContents.getLines().length);
    }

    @Test
    public void testReportSingleLineComment() {
        CommentListener listener = mock(CommentListener.class);
        fileContents.reportSingleLineComment("type", 1, 1);
        verify(listener).reportSingleLineComment("type", 1, 1);
    }

    @Test
    public void testReportBlockComment() {
        CommentListener listener = mock(CommentListener.class);
        fileContents.reportBlockComment("type", 1, 1, 3, 3);
        verify(listener).reportBlockComment("type", 1, 1, 3, 3);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 2);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals("Line 2", cppComments.get(2).getText()[0]);
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 1, 2, 2);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertEquals("Line 1", cComments.get(1).get(0).getText()[0]);
        assertEquals("Line 2", cComments.get(1).get(1).getText()[0]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 1, 2, 2);
        fileContents.reportCComment(3, 1, 4, 2);
        TextBlock javadoc = mock(TextBlock.class);
        fileContents.getJavadocComments().put(2, javadoc);
        assertEquals(javadoc, fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertEquals("Line 1", text.get(0));
        assertEquals("Line 2", text.get(1));
        assertEquals("Line 3", text.get(2));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals("Line 1", lines[0]);
        assertEquals("Line 2", lines[1]);
        assertEquals("Line 3", lines[2]);
    }

    @Test
    public void testGetLine() {
        assertEquals("Line 2", fileContents.getLine(1));
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 2, 1));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents(new FileText(new File("package-info.java")));
        assertTrue(fileContents.inPackageInfo());
    }

}
