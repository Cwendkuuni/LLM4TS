import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.util.List;

public class FileContentsTest {
    
    @Test
    public void testConstructorWithFileNameAndLines() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2", "line3");
        assertEquals("testFile", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
        assertFalse(fileContents.inPackageInfo());
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile"), Arrays.asList("line1", "line2", "line3"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("testFile", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile", "// single line comment");
        fileContents.reportSingleLineComment("type", 1, 3);
        assertTrue(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile", "/* block", "comment */");
        fileContents.reportBlockComment("type", 1, 3, 2, 10);
        assertTrue(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
    }
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("testFile", "// cpp comment");
        fileContents.reportCppComment(1, 3);
        assertTrue(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile", "// cpp comment");
        fileContents.reportCppComment(1, 3);
        assertEquals("cpp comment", fileContents.getCppComments().get(1).getText()[0]);
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile", "/* c", "comment */");
        fileContents.reportCComment(1, 3, 2, 10);
        assertTrue(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("testFile", "/* c", "comment */");
        fileContents.reportCComment(1, 3, 2, 10);
        assertEquals("c comment", fileContents.getCComments().get(1).get(0).getText()[0]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile", "/**", "* Javadoc */", "public class MyClass {");
        fileContents.reportBlockComment("type", 1, 3, 2, 14);
        assertEquals("Javadoc", fileContents.getJavadocBefore(4).getText()[0]);
    }
    
    @Test
    public void testGetText() {
        FileText fileText = new FileText(new File("testFile"), Arrays.asList("line1", "line2", "line3"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2", "line3");
        String[] lines = fileContents.getLines();
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
        assertEquals("line3", lines[2]);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2", "line3");
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2", "line3");
        assertEquals("testFile", fileContents.getFileName());
    }
    
    @Test
    public void testDeprecatedGetFilename() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2", "line3");
        assertEquals("testFile", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile", "line1", "", "line3");
        assertTrue(fileContents.lineIsBlank(1));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile", "// single line comment");
        assertTrue(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile", "// single line comment");
        assertTrue(fileContents.hasIntersectionWithComment(1, 3, 1, 20));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line1", "line2", "line3");
        assertTrue(fileContents.inPackageInfo());
    }
}