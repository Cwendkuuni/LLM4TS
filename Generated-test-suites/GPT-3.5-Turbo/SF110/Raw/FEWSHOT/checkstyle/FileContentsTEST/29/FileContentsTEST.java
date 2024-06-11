
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class FileContentsTest {

    @Test
    public void testConstructorWithFilenameAndLines() {
        String[] lines = {"line 1", "line 2", "line 3"};
        FileContents fileContents = new FileContents("test.java", lines);
        
        assertEquals("test.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }
    
    @Test
    public void testConstructorWithFileText() {
        String[] lines = {"line 1", "line 2", "line 3"};
        FileText fileText = FileText.fromLines(new File("test.java"), Arrays.asList(lines));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("test.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "// single line comment", "line 3");
        fileContents.reportSingleLineComment("", 2, 1);
        
        assertTrue(fileContents.getCppComments().containsKey(2));
        assertEquals("// single line comment", fileContents.getCppComments().get(2).getText()[0]);
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "/* block comment", "line 3", "*/");
        fileContents.reportBlockComment("", 2, 1, 3, 2);
        
        assertTrue(fileContents.getCComments().containsKey(2));
        assertEquals("/* block comment", fileContents.getCComments().get(2).get(0).getText()[0]);
        assertEquals("line 3", fileContents.getCComments().get(2).get(1).getText()[0]);
        assertEquals("*/", fileContents.getCComments().get(2).get(1).getText()[1]);
    }
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.java", "line 1");
        fileContents.reportCppComment(1, 1);
        
        assertTrue(fileContents.getCppComments().containsKey(1));
        assertEquals("ine 1", fileContents.getCppComments().get(1).getText()[0]);
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("test.java", "line 1", "// single line comment", "line 3");
        fileContents.reportSingleLineComment("", 2, 1);
        
        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("// single line comment", fileContents.getCppComments().get(2).getText()[0]);
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("test.java", "line 1");
        fileContents.reportCComment(1, 1, 2, 2);
        
        assertTrue(fileContents.getCComments().containsKey(1));
        assertEquals("line 1", fileContents.getCComments().get(1).get(0).getText()[0]);
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("test.java", "line 1", "/* block comment", "line 3", "*/");
        fileContents.reportBlockComment("", 2, 1, 3, 2);
        
        assertEquals(1, fileContents.getCComments().size());
        assertEquals("/* block comment", fileContents.getCComments().get(2).get(0).getText()[0]);
        assertEquals("line 3", fileContents.getCComments().get(2).get(1).getText()[0]);
        assertEquals("*/", fileContents.getCComments().get(2).get(1).getText()[1]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", "line 1", "/* block comment", "/** javadoc comment", "* more javadoc comment", "line 5");
        fileContents.reportBlockComment("", 2, 1, 3, 2);
        fileContents.reportBlockComment("", 3, 1, 4, 2);
        fileContents.reportBlockComment("", 4, 1, 5, 2);
        
        TextBlock javadocComment = fileContents.getJavadocBefore(5);
        assertEquals(3, javadocComment.getText().length);
        assertEquals("/** javadoc comment", javadocComment.getText()[0]);
        assertEquals("* more javadoc comment", javadocComment.getText()[1]);
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
        
        assertEquals("test.java", fileContents.getText().getFile().toString());
        assertEquals("line 1", fileContents.getText().get(0));
        assertEquals("line 2", fileContents.getText().get(1));
        assertEquals("line 3", fileContents.getText().get(2));
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
        
        assertArrayEquals(new String[] {"line 1", "line 2", "line 3"}, fileContents.getLines());
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
        
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
        assertEquals("line 3", fileContents.getLine(2));
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
        
        assertEquals("test.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2", "line 3");
        
        assertEquals("test.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "", "  ", "line 3");
        
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "// single line comment", "", "/* block comment", "* javadoc comment", "*/");
        
        assertTrue(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(3));
        assertTrue(fileContents.lineIsComment(4));
        assertTrue(fileContents.lineIsComment(5));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "// single line comment", "/* block comment", "* javadoc comment", "*/");
        
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 0, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 4, 1, 6));
        assertTrue(fileContents.hasIntersectionWithComment(2, 2, 2, 4));
        assertTrue(fileContents.hasIntersectionWithComment(3, 1, 4, 2));
        assertTrue(fileContents.hasIntersectionWithComment(4, 1, 4, 7));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line 1");
        assertTrue(fileContents.inPackageInfo());
    }
}
