package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.grammars.Comment;
import com.puppycrawl.tools.checkstyle.grammars.TextBlock;
import com.puppycrawl.tools.checkstyle.grammars.FileText;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;
    private final String fileName = "test_file.java";
    private final String[] lines = {"// line 1", "/* comment */", "// line 2"};
    private final int startLineNo = 1;
    private final int startColNo = 5;
    private final int endLineNo = 2;
    private final int endColNo = 15;
    
    @Before
    public void setUp() {
        fileContents = new FileContents(fileName, lines);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fileContents);
        assertEquals(fileName, fileContents.getFileName());
        assertArrayEquals(lines, fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", startLineNo, startColNo);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertNotNull(cppComments);
        assertTrue(cppComments.containsKey(startLineNo));
        TextBlock comment = cppComments.get(startLineNo);
        assertNotNull(comment);
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
        assertEquals(line(startLineNo - 1).substring(startColNo), comment.getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", startLineNo, startColNo, endLineNo, endColNo);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertNotNull(cComments);
        assertTrue(cComments.containsKey(startLineNo));
        List<TextBlock> comments = cComments.get(startLineNo);
        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        TextBlock comment = comments.get(0);
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
        assertEquals(endLineNo, comment.getEndLineNo());
        assertEquals(endColNo, comment.getEndColNo());
        assertEquals(Arrays.asList(extractCComment(startLineNo, startColNo, endLineNo, endColNo)), Arrays.asList(comment.getText()));
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNo = 2;
        TextBlock comment = fileContents.getJavadocBefore(lineNo);

        assertNull(comment);

        fileContents.reportBlockComment("type1", lineNo - 1, startColNo, endLineNo, endColNo);
        fileContents.reportBlockComment("type2", lineNo - 2, startColNo, endLineNo, endColNo);
        comment = fileContents.getJavadocBefore(lineNo);

        assertNotNull(comment);
        assertEquals(lineNo - 2, comment.getStartLineNo());
        assertEquals(lineNo - 2, comment.getEndLineNo());
        assertEquals(Arrays.asList(extractCComment(lineNo - 2, startColNo, endLineNo, endColNo)), Arrays.asList(comment.getText()));
    }

    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertNotSame(fileContents.getText(), text);
        assertArrayEquals(fileContents.getLines(), text.toLinesArray());
    }

    @Test
    public void testGetLine() {
        for (int i = 0; i < lines.length; i++) {
            assertEquals(lines[i], fileContents.getLine(i));
        }
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, 0, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        FileContents fileContentsPackageInfo = new FileContents("package-info.java");
        assertTrue(fileContentsPackageInfo.inPackageInfo());
    }

    private String line(int lineNo) {
        return fileContents.getLine(lineNo);
    }

    private String[] extractCComment(int startLineNo, int startColNo, int endLineNo, int endColNo) {
        String[] comment;
        if(startLineNo == endLineNo) {
            comment = new String[] { line(startLineNo).substring(startColNo, endColNo + 1) };
        } else {
            comment = new String[endLineNo - startLineNo + 1];
            comment[0] = line(startLineNo).substring(startColNo);
            for(int i = startLineNo + 1; i < endLineNo; i++) {
                comment[i - startLineNo] = line(i);
            }
            comment[comment.length - 1] = line(endLineNo).substring(0, endColNo + 1);
        }
        return comment;
    }

}