
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testConstructorWithFilenameAndLines() {
        String filename = "testFile.java";
        String[] lines = {"line 1", "line 2"};
        
        FileContents fileContents = new FileContents(filename, lines);
        
        assertEquals(filename, fileContents.getFileName());
        assertArrayEquals(lines, fileContents.getLines());
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile.java"));
        
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals(fileText.getFile().toString(), fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.java");
        
        fileContents.reportSingleLineComment("type", 1, 2);
        
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.java");
        
        fileContents.reportBlockComment("type", 1, 2, 3, 4);
        
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile.java");
        
        fileContents.reportCppComment(1, 2);
        
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile.java");
        
        fileContents.reportCComment(1, 2, 3, 4);
        
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("testFile.java");
        
        String[] cComment = fileContents.extractCComment(1, 2, 3, 4);
        
        assertNotNull(cComment);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.java");
        fileContents.reportCComment(1, 2, 3, 4);
        
        TextBlock javadocBefore = fileContents.getJavadocBefore(2);
        
        assertNull(javadocBefore);
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile.java");
        
        FileText text = fileContents.getText();
        
        assertEquals(fileContents.getLines().length, text.getLines().length);
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile.java", "line 1", "line 2");
        
        String[] lines = fileContents.getLines();
        
        assertEquals(2, lines.length);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testFile.java");
        
        String line = fileContents.getLine(1);
        
        assertNotNull(line);
    }
    
    @Test
    public void testGetFileName() {
        String filename = "testFile.java";
        
        FileContents fileContents = new FileContents(filename);
        
        assertEquals(filename, fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        String filename = "testFile.java";
        
        FileContents fileContents = new FileContents(filename);
        
        assertEquals(filename, fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.java");
        
        boolean isBlank = fileContents.lineIsBlank(1);
        
        assertFalse(isBlank);
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.java");
        
        boolean isComment = fileContents.lineIsComment(1);
        
        assertFalse(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.java");
        
        boolean hasIntersection = fileContents.hasIntersectionWithComment(1, 2, 3, 4);
        
        assertFalse(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        
        boolean inPackageInfo = fileContents.inPackageInfo();
        
        assertTrue(inPackageInfo);
    }
}
