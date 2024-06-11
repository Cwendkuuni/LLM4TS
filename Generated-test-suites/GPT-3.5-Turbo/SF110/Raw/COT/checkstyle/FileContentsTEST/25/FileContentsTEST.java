
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileContentsTest {
    
    private FileContents fileContents;

    @Before
    public void setUp() {
        String filename = "test.txt";
        String[] lines = {
            "Hello World",
            "// This is a comment",
            "/* This is",
            "a block comment */"
        };
        fileContents = new FileContents(filename, lines);
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertNotNull(cppComments.get(2));
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertNotNull(cComments.get(3));
    }

    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents.extractCComment(3, 0, 4, 18);
        assertEquals(2, cComment.length);
        assertEquals("/* This is", cComment[0]);
        assertEquals("a block comment */", cComment[1]);
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadocBefore = fileContents.getJavadocBefore(3);
        assertNotNull(javadocBefore);
    }

    @Test
    public void testGetText() {
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals(4, lines.length);
        assertEquals("Hello World", lines[0]);
        assertEquals("// This is a comment", lines[1]);
        assertEquals("/* This is", lines[2]);
        assertEquals("a block comment */", lines[3]);
    }

    @Test
    public void testGetLine() {
        assertEquals("Hello World", fileContents.getLine(0));
        assertEquals("// This is a comment", fileContents.getLine(1));
        assertEquals("/* This is", fileContents.getLine(2));
        assertEquals("a block comment */", fileContents.getLine(3));
    }

    @Test
    public void testGetFileName() {
        assertEquals("test.txt", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("test.txt", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 1, 4));
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 3, 18));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}
