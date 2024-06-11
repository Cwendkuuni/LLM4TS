import com.puppycrawl.tools.checkstyle.grammars.*;
import java.util.regex.*;
import java.io.*;
import com.google.common.collect.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents("test-file", "line 1", "line 2", "line 3");
    }

    @After
    public void tearDown() {
        fileContents = null;
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(1));
        TextBlock comment = cppComments.get(1);
        assertNotNull(comment);
        assertEquals("line 1", comment.getLines()[0]);
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getEndColNo());
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 3, 3);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(1));
        List<TextBlock> entries = cComments.get(1);
        assertNotNull(entries);
        assertEquals(1, entries.size());
        TextBlock comment = entries.get(0);
        assertNotNull(comment);
        assertEquals("line 1", comment.getLines()[0]);
        assertEquals("line 2", comment.getLines()[1]);
        assertEquals("line 3", comment.getLines()[2]);
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(3, comment.getEndColNo());
        assertEquals(3, comment.getEndLineNo());
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type", 1, 1, 3, 3);
        TextBlock javadoc = fileContents.getJavadocBefore(4);
        assertNotNull(javadoc);
        assertEquals("line 1", javadoc.getLines()[0]);
        assertEquals("line 2", javadoc.getLines()[1]);
        assertEquals("line 3", javadoc.getLines()[2]);
        assertEquals(1, javadoc.getStartColNo());
        assertEquals(1, javadoc.getStartLineNo());
        assertEquals(3, javadoc.getEndColNo());
        assertEquals(3, javadoc.getEndLineNo());
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 3, 3));
        assertFalse(fileContents.hasIntersectionWithComment(2, 1, 3, 3));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testGetFileName() {
        assertEquals("test-file", fileContents.getFileName());
    }

    @Test
    public void testGetLine() {
        assertEquals("line 1", fileContents.getLine(1));
        assertEquals("line 2", fileContents.getLine(2));
        assertEquals("line 3", fileContents.getLine(3));
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertEquals("test-file", text.getFile().toString());
        assertEquals("line 1", text.get(0));
        assertEquals("line 2", text.get(1));
        assertEquals("line 3", text.get(2));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(3, lines.length);
        assertEquals("line 1", lines[0]);
        assertEquals("line 2", lines[1]);
        assertEquals("line 3", lines[2]);
    }

    @Test
    public void testGetFilename() {
        assertEquals("test-file", fileContents.getFilename());
    }

}