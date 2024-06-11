import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileContentsTest {
    
    @Test
    public void testReportSingleLineComment() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Mock the CommentListener
        CommentListener commentListener = mock(CommentListener.class);
        
        // Call the method under test
        fileContents.reportSingleLineComment("type", 1, 1);
        
        // Verify the method call
        verify(commentListener).reportSingleLineComment("type", 1, 1);
    }
    
    @Test
    public void testReportBlockComment() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Mock the CommentListener
        CommentListener commentListener = mock(CommentListener.class);
        
        // Call the method under test
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        
        // Verify the method call
        verify(commentListener).reportBlockComment("type", 1, 1, 2, 2);
    }
    
    @Test
    public void testReportCppComment() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        fileContents.reportCppComment(1, 1);
        
        // Get the CppComments and verify the result
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNotNull(cppComments);
    }
    
    @Test
    public void testGetCComments() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        
        // Verify the result
        assertNotNull(cComments);
    }
    
    @Test
    public void testGetJavadocBefore() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        TextBlock javadocBefore = fileContents.getJavadocBefore(1);
        
        // Verify the result
        assertNull(javadocBefore);
    }
    
    @Test
    public void testGetText() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        FileText text = fileContents.getText();
        
        // Verify the result
        assertNotNull(text);
    }
    
    @Test
    public void testGetLines() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        String[] lines = fileContents.getLines();
        
        // Verify the result
        assertNotNull(lines);
    }
    
    @Test
    public void testGetLine() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        String line = fileContents.getLine(1);
        
        // Verify the result
        assertNotNull(line);
    }
    
    @Test
    public void testGetFileName() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        String fileName = fileContents.getFileName();
        
        // Verify the result
        assertNotNull(fileName);
    }
    
    @Test
    public void testGetFilename() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        String filename = fileContents.getFilename();
        
        // Verify the result
        assertNotNull(filename);
    }
    
    @Test
    public void testLineIsBlank() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        boolean isBlank = fileContents.lineIsBlank(1);
        
        // Verify the result
        assertFalse(isBlank);
    }
    
    @Test
    public void testLineIsComment() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        boolean isComment = fileContents.lineIsComment(1);
        
        // Verify the result
        assertFalse(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 1, 2, 2);
        
        // Verify the result
        assertFalse(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        // Create an instance of FileContents
        FileContents fileContents = new FileContents("test.java");
        
        // Call the method under test
        boolean inPackageInfo = fileContents.inPackageInfo();
        
        // Verify the result
        assertFalse(inPackageInfo);
    }
    
}