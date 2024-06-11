import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructor() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "Line 2", "Line 3");
        
        assertEquals("test.txt", fileContents.getFileName());
        String[] lines = fileContents.getLines();
        assertEquals("Line 1", lines[0]);
        assertEquals("Line 2", lines[1]);
        assertEquals("Line 3", lines[2]);
    }
    
    @Test
    public void testFileContentsFileTextConstructor() {
        FileText fileText = new FileText("test.txt", "Line 1", "Line 2", "Line 3");
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("test.txt", fileContents.getFileName());
        String[] lines = fileContents.getLines();
        assertEquals("Line 1", lines[0]);
        assertEquals("Line 2", lines[1]);
        assertEquals("Line 3", lines[2]);
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "// Single line comment", "Line 3");
        fileContents.reportSingleLineComment("type", 2, 1);
        
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(2));
        assertEquals("// Single line comment", cppComments.get(2).getText());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "/* Block comment", "Line 3", "*/");
        fileContents.reportBlockComment("type", 2, 1, 4, 2);
        
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(2));
        assertEquals(1, cComments.get(2).size());
        assertEquals("/* Block comment\nLine 3\n*/", cComments.get(2).get(0).getText());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.txt", "/** Javadoc", "Line 2", "Line 3", "*/");
        fileContents.reportCComment(1, 1, 4, 2);
        
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        assertNotNull(javadocComment);
        assertEquals("/** Javadoc\nLine 2\nLine 3\n*/", javadocComment.getText());
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "Line 2", "Line 3");
        FileText text = fileContents.getText();
        
        assertEquals("test.txt", text.getFile().toString());
        String[] lines = text.toLinesArray();
        assertEquals("Line 1", lines[0]);
        assertEquals("Line 2", lines[1]);
        assertEquals("Line 3", lines[2]);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "Line 2", "Line 3");
        
        assertEquals("Line 1", fileContents.getLine(0));
        assertEquals("Line 2", fileContents.getLine(1));
        assertEquals("Line 3", fileContents.getLine(2));
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "", "Line 3");
        
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "// Comment", "Line 3");
        
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.txt", "Line 1", "/* Comment", "Line 3", "*/");
        
        assertTrue(fileContents.hasIntersectionWithComment(2, 2, 4, 1));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 1));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "Line 1", "Line 2", "Line 3");
        
        assertTrue(fileContents.inPackageInfo());
    }
}