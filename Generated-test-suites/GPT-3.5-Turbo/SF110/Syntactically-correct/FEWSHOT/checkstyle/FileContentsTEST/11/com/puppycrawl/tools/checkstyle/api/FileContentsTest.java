import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FileContentsTest {
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportSingleLineComment("single", 1, 0);
        
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportBlockComment("block", 1, 0, 2, 5);
        
        assertEquals(1, fileContents.getCppComments().size());
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportCppComment(1, 0);
        
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportCppComment(1, 0);
        
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportCComment(1, 0, 2, 5);
        
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportCComment(1, 0, 2, 5);
        
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportCComment(1, 0, 2, 5);
        fileContents.getJavadocBefore(2);
        
        assertEquals(1, fileContents.getJavadocBefore(2).size());
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.java");
        
        assertEquals("test.java", fileContents.getText().getFile().toString());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        
        assertEquals(3, fileContents.getLines().length);
        assertEquals("line1", fileContents.getLines()[0]);
        assertEquals("line2", fileContents.getLines()[1]);
        assertEquals("line3", fileContents.getLines()[2]);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.java");
        
        assertEquals("test.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.java");
        
        assertEquals("test.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "", "line2", "line3");
        
        assertEquals(true, fileContents.lineIsBlank(0));
        assertEquals(false, fileContents.lineIsBlank(1));
        assertEquals(false, fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "// line1", "line2", "/* block comment */");
        
        assertEquals(true, fileContents.lineIsComment(0));
        assertEquals(false, fileContents.lineIsComment(1));
        assertEquals(true, fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java");
        fileContents.reportSingleLineComment("single", 1, 0);
        
        assertEquals(true, fileContents.hasIntersectionWithComment(1, 2, 2, 5));
        assertEquals(false, fileContents.hasIntersectionWithComment(2, 2, 3, 5));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        
        assertEquals(true, fileContents.inPackageInfo());
    }
}