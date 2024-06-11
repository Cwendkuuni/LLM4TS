import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {
    
    @Test
    public void testFileContentsConstructor1() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }
    
    @Test
    public void testFileContentsConstructor2() {
        FileText fileText = new FileText(new File("filename"), Arrays.asList("line1", "line2"));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("filename", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportSingleLineComment("type", 1, 1);
        
        assertEquals(1, fileContents.getCppComments().size());
        assertNotNull(fileContents.getCppComments().get(1));
        assertEquals("line1", fileContents.getCppComments().get(1).getText()[0]);
        assertEquals(1, fileContents.getCppComments().get(1).getStartCol());
        assertEquals(1, fileContents.getCppComments().get(1).getStartLine());
        assertEquals(5, fileContents.getCppComments().get(1).getEndCol());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("filename", "line1", "line2");
        fileContents.reportBlockComment("type", 1, 1, 2, 3);
        
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments().get(1));
        assertEquals("line1", fileContents.getCComments().get(1).get(0).getText()[0]);
        assertEquals(1, fileContents.getCComments().get(1).get(0).getStartCol());
        assertEquals(1, fileContents.getCComments().get(1).get(0).getStartLine());
        assertEquals(4, fileContents.getCComments().get(1).get(0).getEndCol());
    }
    
    // Other test cases for the remaining methods in the FileContents class
    
}