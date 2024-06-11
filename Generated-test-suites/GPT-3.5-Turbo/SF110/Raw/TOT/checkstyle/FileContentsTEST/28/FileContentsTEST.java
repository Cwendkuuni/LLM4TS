import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String[] lines = {
            "package com.puppycrawl.tools.checkstyle.api;",
            "",
            "import com.puppycrawl.tools.checkstyle.grammars.*;",
            "import java.util.regex.*;",
            "import java.io.*;",
            "import com.google.common.collect.*;",
            "import java.util.*;"
        };
        fileContents = new FileContents("test.java", lines);
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        TextBlock comment = cppComments.get(1);
        assertNotNull(comment);
        assertEquals(0, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(0, comment.getEndLineNo());
        assertEquals(0, comment.getEndColNo());
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 2);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        TextBlock comment = cppComments.get(2);
        assertNotNull(comment);
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(0, comment.getEndLineNo());
        assertEquals(0, comment.getEndColNo());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(3, 3, 4, 4);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        List<TextBlock> comments = cComments.get(3);
        assertNotNull(comments);
        assertEquals(1, comments.size());
        TextBlock comment = comments.get(0);
        assertNotNull(comment);
        assertEquals(2, comment.getStartLineNo());
        assertEquals(2, comment.getStartColNo());
        assertEquals(3, comment.getEndLineNo());
        assertEquals(3, comment.getEndColNo());
    }

    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents.extractCComment(3, 3, 4, 4);
        assertNotNull(cComment);
        assertEquals(2, cComment.length);
        assertEquals("import java.util.regex.*;", cComment[0]);
        assertEquals("import java.io.*;", cComment[1]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(2, 2, 3, 3);
        TextBlock javadocComment = fileContents.getJavadocBefore(3);
        assertNotNull(javadocComment);
        assertEquals(1, javadocComment.getStartLineNo());
        assertEquals(1, javadocComment.getStartColNo());
        assertEquals(1, javadocComment.getEndLineNo());
        assertEquals(1, javadocComment.getEndColNo());
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertEquals("package com.puppycrawl.tools.checkstyle.api;", text.get(0));
        assertEquals("import java.util.*;", text.get(6));
    }

    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(7, lines.length);
        assertEquals("package com.puppycrawl.tools.checkstyle.api;", lines[0]);
        assertEquals("import java.util.*;", lines[6]);
    }

    @Test
    public void testGetLine() {
        String line = fileContents.getLine(0);
        assertNotNull(line);
        assertEquals("package com.puppycrawl.tools.checkstyle.api;", line);
    }

    @Test
    public void testGetFileName() {
        String fileName = fileContents.getFileName();
        assertNotNull(fileName);
        assertEquals("test.java", fileName);
    }

    @Test
    public void testGetFilename() {
        String filename = fileContents.getFilename();
        assertNotNull(filename);
        assertEquals("test.java", filename);
    }

    @Test
    public void testLineIsBlank() {
        boolean isBlank = fileContents.lineIsBlank(1);
        assertFalse(isBlank);
    }

    @Test
    public void testLineIsComment() {
        boolean isComment = fileContents.lineIsComment(1);
        assertFalse(isComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 1, 1);
        assertFalse(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        boolean inPackageInfo = fileContents.inPackageInfo();
        assertFalse(inPackageInfo);
    }

    @Test
    public void testHasIntersectionWithCComment() {
        boolean hasIntersection = fileContents.hasIntersectionWithCComment(1, 1, 1, 1);
        assertFalse(hasIntersection);
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        fileContents.reportCppComment(2, 2);
        boolean hasIntersection = fileContents.hasIntersectionWithCppComment(2, 2, 2, 2);
        assertTrue(hasIntersection);
    }

}