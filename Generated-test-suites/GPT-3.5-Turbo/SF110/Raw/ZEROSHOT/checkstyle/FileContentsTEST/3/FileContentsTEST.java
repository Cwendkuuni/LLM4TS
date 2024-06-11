import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename.java");
        fileContents.reportSingleLineComment("type", 1, 1);
        
        // assert statements
        assertNotNull(fileContents.getCppComments());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename.java");
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        
        // assert statements
        assertNotNull(fileContents.getCComments());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("filename.java");
        fileContents.reportCComment(1, 1, 2, 2);
        
        // assert statements
        assertNotNull(fileContents.getJavadocBefore(2));
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertNotNull(fileContents.getText());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertNotNull(fileContents.getLines());
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertNotNull(fileContents.getLine(0));
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertNotNull(fileContents.getFileName());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertTrue(fileContents.lineIsBlank(0));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertTrue(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("filename.java");
        
        // assert statements
        assertFalse(fileContents.inPackageInfo());
    }
}