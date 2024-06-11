package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        fileContents = new FileContents("testfile.java", "line 1", "line 2", "// single line comment", "/** javadoc comment */");
    }
    
    @Test
    public void testConstructorWithFilenameAndLines() {
        assertNotNull(fileContents);
        assertEquals("testfile.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testfile.java"));
        FileContents fileContents = new FileContents(fileText);
        assertNotNull(fileContents);
        assertEquals("testfile.java", fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 3, 1);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(3));
        Comment comment = (Comment) cppComments.get(3);
        assertEquals("// single line comment", comment.getText()[0]);
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 4, 1, 4, 24);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(4));
        List<TextBlock> comments = cComments.get(4);
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("/** javadoc comment */", comment.getText()[0]);
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndColNo());
    }
    
    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type", 4, 1, 4, 24);
        TextBlock javadocComment = fileContents.getJavadocBefore(4);
        assertNotNull(javadocComment);
        assertEquals("/** javadoc comment */", javadocComment.getText()[0]);
    }
    
    @Test
    public void testGetText() {
        FileText fileText = fileContents.getText();
        assertNotNull(fileText);
        assertEquals("testfile.java", fileText.getFile().toString());
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(2, lines.length);
        assertEquals("line 1", lines[0]);
        assertEquals("line 2", lines[1]);
    }
    
    @Test
    public void testGetLine() {
        String line = fileContents.getLine(1);
        assertEquals("line 2", line);
    }
    
    @Test
    public void testLineIsBlank() {
        boolean isBlank = fileContents.lineIsBlank(0);
        assertFalse(isBlank);
        
        isBlank = fileContents.lineIsBlank(1);
        assertFalse(isBlank);
        
        isBlank = fileContents.lineIsBlank(2);
        assertTrue(isBlank);
    }
    
    @Test
    public void testLineIsComment() {
        boolean isComment = fileContents.lineIsComment(0);
        assertFalse(isComment);
        
        isComment = fileContents.lineIsComment(1);
        assertFalse(isComment);
        
        isComment = fileContents.lineIsComment(2);
        assertTrue(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        boolean hasIntersection = fileContents.hasIntersectionWithComment(0, 0, 0, 5);
        assertFalse(hasIntersection);
        
        hasIntersection = fileContents.hasIntersectionWithComment(2, 0, 3, 0);
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        boolean inPackageInfo = fileContents.inPackageInfo();
        assertFalse(inPackageInfo);
        
        FileContents packageInfoFileContents = new FileContents("package-info.java");
        inPackageInfo = packageInfoFileContents.inPackageInfo();
        assertTrue(inPackageInfo);
    }
    
    @After
    public void tearDown() {
        fileContents = null;
    }
    
}