import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class FileContentsTest {

    // Test case for FileContents constructor with filename and lines
    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        assertEquals("testFile.txt", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }
    
    // Test case for FileContents constructor with FileText
    @Test
    public void testFileContentsConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile.txt"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("testFile.txt", fileContents.getFileName());
        assertTrue(fileContents.getLine(0).isEmpty());
    }
    
    // Test case for reportSingleLineComment method
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportSingleLineComment("type", 1, 6);
        assertTrue(fileContents.lineIsComment(0));
    }
    
    // Test case for reportBlockComment method
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportBlockComment("type", 1, 0, 2, 3);
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }
    
    // Test case for reportCppComment method
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCppComment(1, 4);
        assertTrue(fileContents.lineIsComment(0));
    }
    
    // Test case for getCppComments method
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCppComment(1, 4);
        assertEquals(1, fileContents.getCppComments().size());
        assertEquals("line1", fileContents.getCppComments().get(1).getText()[0]);
    }
    
    // Test case for reportCComment method
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCComment(1, 0, 2, 3);
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }
    
    // Test case for getCComments method
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCComment(1, 0, 2, 3);
        assertEquals(1, fileContents.getCComments().size());
        assertEquals("line1", fileContents.getCComments().get(1).get(0).getText()[0]);
    }
    
    // Test case for extractCComment method
    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        String[] cComment = fileContents.extractCComment(1, 4, 2, 3);
        assertEquals(2, cComment.length);
        assertEquals("line1", cComment[0]);
        assertEquals("line2", cComment[1]);
    }
    
    // Test case for getJavadocBefore method
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCComment(1, 0, 2, 3);
        TextBlock textBlock = new Comment(fileContents.extractCComment(1, 0, 2, 3), 0, 2, 3);
        fileContents.getJavadocComments().put(1, textBlock);
        assertEquals(textBlock, fileContents.getJavadocBefore(2));
    }
    
    // Test case for getText method
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        FileText fileText = fileContents.getText();
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
    }
    
    // Test case for getLines method
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        String[] lines = fileContents.getLines();
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
    }
    
    // Test case for getLine method
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }
    
    // Test case for getFileName method
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        assertEquals("testFile.txt", fileContents.getFileName());
    }
    
    // Test case for getFilename method
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        assertEquals("testFile.txt", fileContents.getFilename());
    }
    
    // Test case for lineIsBlank method
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "");
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }
    
    // Test case for lineIsComment method
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.txt", "// comment", "line2");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }
    
    // Test case for hasIntersectionWithComment method
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.txt", "// comment", "line2");
        assertTrue(fileContents.hasIntersectionWithComment(0, 3, 0, 9));
        assertFalse(fileContents.hasIntersectionWithComment(0, 11, 1, 5));
    }
    
    // Test case for inPackageInfo method
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line1", "line2");
        assertTrue(fileContents.inPackageInfo());
    }
    
    // Test case for hasIntersectionWithCComment method
    @Test
    public void testHasIntersectionWithCComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCComment(1, 0, 2, 3);
        assertTrue(fileContents.hasIntersectionWithCComment(1, 0, 2, 3));
        assertFalse(fileContents.hasIntersectionWithCComment(0, 0, 1, 5));
    }
    
    // Test case for hasIntersectionWithCppComment method
    @Test
    public void testHasIntersectionWithCppComment() {
        FileContents fileContents = new FileContents("testFile.txt", "line1", "line2");
        fileContents.reportCppComment(1, 4);
        assertTrue(fileContents.hasIntersectionWithCppComment(1, 4, 1, 8));
        assertFalse(fileContents.hasIntersectionWithCppComment(0, 0, 1, 5));
    }

}