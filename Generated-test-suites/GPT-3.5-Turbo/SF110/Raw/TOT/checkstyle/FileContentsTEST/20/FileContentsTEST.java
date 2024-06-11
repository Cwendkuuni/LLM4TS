import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setup() {
        // Test Setup
        String[] lines = {"This is line 1", "This is line 2"};
        fileContents = new FileContents("testFile.java", lines);
    }
    
    // Test for constructor with String... lines
    @Test
    public void testConstructorStringLines() {
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals("This is line 1", fileContents.getLine(0));
        assertEquals("This is line 2", fileContents.getLine(1));
        assertFalse(fileContents.inPackageInfo());
    }
    
    // Test for constructor with FileText object
    @Test
    public void testConstructorFileText() {
        FileText fileText = new FileText("testFile.java");
        fileContents = new FileContents(fileText);
        
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals("", fileContents.getLine(0));
        assertFalse(fileContents.inPackageInfo());
    }
    
    // Test for reportCppComment method
    @Test
    public void testReportCppComment() {
        int startLineNo = 1;
        int startColNo = 5;
        fileContents.reportCppComment(startLineNo, startColNo);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(startLineNo));
        assertEquals(" is line 1", cppComments.get(startLineNo).getText());
    }
    
    // Test for getCppComments method
    @Test
    public void testGetCppComments() {
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
        assertTrue(cppComments.isEmpty());
    }
    
    // Test for reportCComment method
    @Test
    public void testReportCComment() {
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 2;
        int endColNo = 10;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(startLineNo));
        List<TextBlock> comments = cComments.get(startLineNo);
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("This is lin", comments.get(0).getText());
    }
    
    // Test for getCComments method
    @Test
    public void testGetCComments() {
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNotNull(cComments);
        assertTrue(cComments.isEmpty());
    }
    
    // Test for getJavadocBefore method
    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 2;
        fileContents.reportCComment(1, 0, 3, 10);
        TextBlock javadoc = fileContents.getJavadocBefore(lineNoBefore);
        assertNotNull(javadoc);
        assertEquals("This is lin", javadoc.getText());
    }
    
    // Test for getText method
    @Test
    public void testGetText() {
        FileText text = fileContents.getText();
        assertNotNull(text);
        assertEquals("testFile.java", text.getFileText());
        assertEquals("This is line 1\nThis is line 2", text.toLinesString());
    }
    
    // Test for getLines method
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertNotNull(lines);
        assertEquals(2, lines.length);
        assertEquals("This is line 1", lines[0]);
        assertEquals("This is line 2", lines[1]);
    }
    
    // Test for getLine method
    @Test
    public void testGetLine() {
        assertEquals("This is line 1", fileContents.getLine(0));
        assertEquals("This is line 2", fileContents.getLine(1));
    }
    
    // Test for getFileName method
    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    // Test for deprecated getFilename method
    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFilename());
    }
    
    // Test for lineIsBlank method
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
    }
    
    // Test for lineIsComment method
    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }
    
    // Test for hasIntersectionWithComment method (no intersection)
    @Test
    public void testHasIntersectionWithCommentNoIntersection() {
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 2, 10));
    }
    
    // Test for hasIntersectionWithComment method (C comment intersection)
    @Test
    public void testHasIntersectionWithCommentCIntersection() {
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 2;
        int endColNo = 10;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 2, 10));
    }
    
    // Test for hasIntersectionWithComment method (CPP comment intersection)
    @Test
    public void testHasIntersectionWithCommentCppIntersection() {
        int startLineNo = 1;
        int startColNo = 5;
        fileContents.reportCppComment(startLineNo, startColNo);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, startLineNo, startColNo + 5));
    }
    
    // Test for inPackageInfo method
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
    
}