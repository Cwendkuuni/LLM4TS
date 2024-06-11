
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        fileContents = new FileContents("testFile.java");
    }
    
    @Test
    public void testConstructor_StringVarargs() {
        String[] lines = {"test line 1", "test line 2"};
        FileContents fileContents = new FileContents("testFile.java", lines);
        
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals("test line 1", fileContents.getLine(0));
        assertEquals("test line 2", fileContents.getLine(1));
    }
    
    @Test
    public void testConstructor_FileText() {
        FileText fileText = new FileText(new File("testFile.java"));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 2);
        
        assertNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 2, 3, 4);
        
        assertNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 2);
        
        assertNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        
        assertNotNull(cppComments);
        assertEquals(0, cppComments.size());
    }
    
    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 2, 3, 4);
        
        assertNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        
        assertNotNull(cComments);
        assertEquals(0, cComments.size());
    }
    
    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 2, 3, 4);
        fileContents.reportCComment(2, 3, 4, 5);
        fileContents.reportBlockComment("type", 5, 6, 7, 8);
        fileContents.reportSingleLineComment("type", 8, 9);
        
        assertNull(fileContents.getJavadocBefore(1));
        assertNull(fileContents.getJavadocBefore(2));
        assertNotNull(fileContents.getJavadocBefore(5));
        assertNotNull(fileContents.getJavadocBefore(8));
    }
    
    @Test
    public void testGetText() {
        FileText fileText = fileContents.getText();
        
        assertNotNull(fileText);
        assertEquals(fileContents.getLines(), fileText.toLinesArray());
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        
        assertNotNull(lines);
        assertEquals(0, lines.length);
    }
    
    @Test
    public void testGetLine() {
        assertNull(fileContents.getLine(0));
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
    }
    
    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 2, 3, 4));
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}
