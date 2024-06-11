import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        String[] lines = {"line 1", "line 2", "", "line 4 // comment", "line 5 /* block comment */"};
        fileContents = new FileContents("test.java", lines);
    }
    
    @Test
    public void testReportCppComment_WhenValidParams_ShouldAddCppCommentToMap() {
        fileContents.reportCppComment(4, 7);
        assertEquals("comment", fileContents.getCppComments().get(4).getText()[0]);
    }
    
    @Test
    public void testReportCppComment_WhenInvalidParams_ShouldNotAddCppCommentToMap() {
        fileContents.reportCppComment(1, 10);
        assertNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testGetCppComments_WhenCppCommentsExist_ShouldReturnImmutableMap() {
        fileContents.reportCppComment(4, 7);
        assertNotNull(fileContents.getCppComments());
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testGetCppComments_WhenNoCppCommentsExist_ShouldReturnEmptyMap() {
        assertNotNull(fileContents.getCppComments());
        assertTrue(fileContents.getCppComments().isEmpty());
    }
    
    @Test
    public void testReportCComment_WhenValidParams_ShouldAddCCommentToMap() {
        fileContents.reportCComment(5, 7, 5, 23);
        assertNotNull(fileContents.getCComments().get(5));
        assertEquals("block comment", fileContents.getCComments().get(5).get(0).getText()[0]);
    }
    
    @Test
    public void testReportCComment_WhenInvalidParams_ShouldNotAddCCommentToMap() {
        fileContents.reportCComment(1, 10, 3, 20);
        assertNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testGetCComments_WhenCCommentsExist_ShouldReturnImmutableMap() {
        fileContents.reportCComment(5, 7, 5, 23);
        assertNotNull(fileContents.getCComments());
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testGetCComments_WhenNoCCommentsExist_ShouldReturnEmptyMap() {
        assertNotNull(fileContents.getCComments());
        assertTrue(fileContents.getCComments().isEmpty());
    }
    
    @Test
    public void testGetJavadocBefore_WhenJavadocExists_ShouldReturnJavadoc() {
        fileContents.reportCComment(4, 7, 5, 23);
        assertEquals("block comment", fileContents.getJavadocBefore(5).getText()[0]);
    }
    
    @Test
    public void testGetJavadocBefore_WhenJavadocDoesNotExist_ShouldReturnNull() {
        assertNull(fileContents.getJavadocBefore(5));
    }
    
    @Test
    public void testGetText_WhenFileTextExists_ShouldReturnFileTextObject() {
        assertNotNull(fileContents.getText());
        assertEquals("line 1", fileContents.getText().get(0));
    }
    
    @Test
    public void testGetLines_WhenFileTextExists_ShouldReturnArrayWithFileLines() {
        String[] expected = {"line 1", "line 2", "", "line 4 // comment", "line 5 /* block comment */"};
        assertArrayEquals(expected, fileContents.getLines());
    }
    
    @Test
    public void testGetLine_WhenValidIndex_ShouldReturnCorrectLine() {
        assertEquals("line 2", fileContents.getLine(1));
    }
    
    @Test
    public void testGetLine_WhenInvalidIndex_ShouldReturnNull() {
        assertNull(fileContents.getLine(10));
    }
    
    @Test
    public void testGetFileName_WhenFileNameExists_ShouldReturnFileName() {
        assertEquals("test.java", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename_WhenFileNameExists_ShouldReturnFileName() {
        assertEquals("test.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank_WhenLineIsBlank_ShouldReturnTrue() {
        assertTrue(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsBlank_WhenLineIsNotBlank_ShouldReturnFalse() {
        assertFalse(fileContents.lineIsBlank(0));
    }
    
    @Test
    public void testLineIsComment_WhenLineIsComment_ShouldReturnTrue() {
        assertTrue(fileContents.lineIsComment(3));
    }
    
    @Test
    public void testLineIsComment_WhenLineIsNotComment_ShouldReturnFalse() {
        assertFalse(fileContents.lineIsComment(1));
    }
    
    @Test
    public void testHasIntersectionWithComment_WhenIntersectionExists_ShouldReturnTrue() {
        assertTrue(fileContents.hasIntersectionWithComment(3, 5, 4, 12));
    }
    
    @Test
    public void testHasIntersectionWithComment_WhenNoIntersectionExists_ShouldReturnFalse() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 10, 2, 15));
    }
    
    @Test
    public void testInPackageInfo_WhenFileNameIsPackageInfo_ShouldReturnTrue() {
        assertTrue(fileContents.inPackageInfo());
    }
    
    @Test
    public void testInPackageInfo_WhenFileNameIsNotPackageInfo_ShouldReturnFalse() {
        assertFalse(fileContents.inPackageInfo());
    }
    
}