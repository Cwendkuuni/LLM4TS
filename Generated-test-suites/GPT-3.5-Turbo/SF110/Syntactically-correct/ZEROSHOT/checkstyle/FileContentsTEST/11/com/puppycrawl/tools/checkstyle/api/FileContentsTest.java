import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java");
        fileContents.reportCppComment(1, 3);
        fileContents.reportCComment(2, 1, 4, 5);
        fileContents.reportSingleLineComment("type", 3, 2);
    }
    
    @Test
    public void testReportCppComment() {
        assertEquals("firstCppComment", fileContents.getCppComments().get(1).getText()[0]);
    }
    
    @Test
    public void testReportCComment() {
        assertEquals("cComment1", fileContents.getCComments().get(2).get(0).getText()[0]);
    }
    
    @Test
    public void testReportSingleLineComment() {
        assertEquals("singleLineComment", fileContents.getCComments().get(3).get(0).getText()[0]);
    }
    
    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents.extractCComment(2, 1, 4, 5);
        assertEquals("cComment1", cComment[0]);
        assertEquals("cComment2", cComment[1]);
        assertEquals("cComment3", cComment[2]);
        assertEquals("cComment4", cComment[3]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        assertEquals("javadocComment", fileContents.getJavadocBefore(4).getText()[0]);
    }
    
    @Test
    public void testGetText() {
        assertEquals("line1", fileContents.getText().get(0));
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
    }
    
    @Test
    public void testGetLine() {
        assertEquals("line1", fileContents.getLine(0));
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
    }
    
    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(3));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(2, 1, 4, 3));
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}