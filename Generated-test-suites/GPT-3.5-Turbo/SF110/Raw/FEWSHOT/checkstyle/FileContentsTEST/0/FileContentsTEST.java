
package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileContentsTest {

    private static final String TEST_FILENAME = "testFile";
    private static final String[] TEST_LINES = {
        "line1",
        "line2",
        "line3"
    };
    private static final int TEST_LINE_NUMBER = 2;
    private static final int TEST_START_COLUMN_NUMBER = 3;
    private static final int TEST_END_COLUMN_NUMBER = 5;

    private FileContents fileContents;

    @Before
    public void setUp() {
        fileContents = new FileContents(TEST_FILENAME, TEST_LINES);
    }

    @Test
    public void testConstructorWithFilenameAndLines() {
        FileContents fileContents = new FileContents(TEST_FILENAME, TEST_LINES);
        assertNotNull(fileContents);
        assertEquals(TEST_FILENAME, fileContents.getFileName());
        assertArrayEquals(TEST_LINES, fileContents.getLines());
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File(TEST_FILENAME), Arrays.asList(TEST_LINES));
        FileContents fileContents = new FileContents(fileText);
        assertNotNull(fileContents);
        assertEquals(TEST_FILENAME, fileContents.getFileName());
        assertArrayEquals(TEST_LINES, fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(TEST_LINE_NUMBER));
        TextBlock comment = cppComments.get(TEST_LINE_NUMBER);
        assertTrue(comment instanceof Comment);
        assertEquals("line2", ((Comment) comment).getText()[0]);
        assertEquals(TEST_START_COLUMN_NUMBER, comment.getStartPosition());
        assertEquals(TEST_LINE_NUMBER, comment.getStartLine());
        assertEquals(TEST_START_COLUMN_NUMBER + 2, comment.getEndPosition());
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER, TEST_END_COLUMN_NUMBER);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(TEST_LINE_NUMBER));
        List<TextBlock> comments = cComments.get(TEST_LINE_NUMBER);
        assertFalse(comments.isEmpty());
        TextBlock comment = comments.get(0);
        assertTrue(comment instanceof Comment);
        assertArrayEquals(TEST_LINES, ((Comment) comment).getText());
        assertEquals(TEST_START_COLUMN_NUMBER, comment.getStartPosition());
        assertEquals(TEST_LINE_NUMBER, comment.getStartLine());
        assertEquals(TEST_END_COLUMN_NUMBER, comment.getEndPosition());
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(TEST_LINE_NUMBER));
        TextBlock comment = cppComments.get(TEST_LINE_NUMBER);
        assertTrue(comment instanceof Comment);
        assertEquals("line2", ((Comment) comment).getText()[0]);
        assertEquals(TEST_START_COLUMN_NUMBER, comment.getStartPosition());
        assertEquals(TEST_LINE_NUMBER, comment.getStartLine());
        assertEquals(4, comment.getEndPosition());
    }

    @Test
    public void testGetCppComments() {
        assertNull(fileContents.getCppComments());
        fileContents.reportCppComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertTrue(cppComments.containsKey(TEST_LINE_NUMBER));
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER, TEST_END_COLUMN_NUMBER);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(TEST_LINE_NUMBER));
        List<TextBlock> comments = cComments.get(TEST_LINE_NUMBER);
        assertFalse(comments.isEmpty());
        TextBlock comment = comments.get(0);
        assertTrue(comment instanceof Comment);
        assertArrayEquals(TEST_LINES, ((Comment) comment).getText());
        assertEquals(TEST_START_COLUMN_NUMBER, comment.getStartPosition());
        assertEquals(TEST_LINE_NUMBER, comment.getStartLine());
        assertEquals(TEST_END_COLUMN_NUMBER, comment.getEndPosition());
    }

    @Test
    public void testGetCComments() {
        assertNull(fileContents.getCComments());
        fileContents.reportCComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER, TEST_END_COLUMN_NUMBER);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertTrue(cComments.containsKey(TEST_LINE_NUMBER));
    }

    @Test
    public void testExtractCComment() {
        String[] expected = {"line2"};
        String[] actual = fileContents.extractCComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER, TEST_END_COLUMN_NUMBER);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(TEST_LINE_NUMBER - 1, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER - 1, TEST_END_COLUMN_NUMBER);
        TextBlock expected = new Comment(TEST_LINES, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER - 1, TEST_END_COLUMN_NUMBER);
        TextBlock actual = fileContents.getJavadocBefore(TEST_LINE_NUMBER);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetText() {
        FileText expected = new FileText(new File(TEST_FILENAME), Arrays.asList(TEST_LINES));
        FileText actual = fileContents.getText();
        assertEquals(expected.getFile(), actual.getFile());
        assertArrayEquals(expected.getLines().toArray(), actual.getLines().toArray());
    }

    @Test
    public void testGetLines() {
        assertArrayEquals(TEST_LINES, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("line2", fileContents.getLine(TEST_LINE_NUMBER));
    }

    @Test
    public void testGetFileName() {
        assertEquals(TEST_FILENAME, fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals(TEST_FILENAME, fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(TEST_LINE_NUMBER));
        assertTrue(fileContents.lineIsBlank(0));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(TEST_LINE_NUMBER));
        assertTrue(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER, TEST_END_COLUMN_NUMBER));
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, TEST_LINE_NUMBER, TEST_END_COLUMN_NUMBER));
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 0, TEST_END_COLUMN_NUMBER));
        assertTrue(fileContents.hasIntersectionWithComment(TEST_LINE_NUMBER, TEST_START_COLUMN_NUMBER, TEST_LINE_NUMBER, TEST_LINE_NUMBER));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
        fileContents = new FileContents("package-info.java", TEST_LINES);
        assertTrue(fileContents.inPackageInfo());
    }
    
}
