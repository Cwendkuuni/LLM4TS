import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    private FileContents fileContents;

    @Test
    public void testFileContentsConstructorWithLines() {
        fileContents = new FileContents("TestFile", "Line 1", "Line 2", "Line 3");
        assertEquals("TestFile", fileContents.getFileName());
        assertNotNull(fileContents.getText());
        assertEquals("Line 2", fileContents.getLine(1));
    }
    
    @Test
    public void testFileContentsConstructorWithFileText() {
        FileText fileText = new FileText(new File("TestFile.txt"));
        fileContents = new FileContents(fileText);
        assertEquals("TestFile.txt", fileContents.getFileName());
        assertSame(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents = new FileContents("TestFile");
        fileContents.reportSingleLineComment("type", 1, 2);
        assertEquals(1, fileContents.getCppComments().size());
        assertNotNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents = new FileContents("TestFile");
        fileContents.reportBlockComment("type", 1, 2, 3, 4);
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testReportCppComment() {
        fileContents = new FileContents("TestFile");
        fileContents.reportCppComment(1, 2);
        assertEquals(1, fileContents.getCppComments().size());
        assertNotNull(fileContents.getCppComments().get(1));
    }
    
    @Test
    public void testGetCppComments() {
        fileContents = new FileContents("TestFile");
        fileContents.reportCppComment(1, 2);
        assertEquals(1, fileContents.getCppComments().size());
        assertNotNull(fileContents.getCppComments());
    }
    
    @Test
    public void testReportCComment() {
        fileContents = new FileContents("TestFile");
        fileContents.reportCComment(1, 2, 3, 4);
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments().get(1));
    }
    
    @Test
    public void testGetCComments() {
        fileContents = new FileContents("TestFile");
        fileContents.reportCComment(1, 2, 3, 4);
        assertEquals(1, fileContents.getCComments().size());
        assertNotNull(fileContents.getCComments());
    }
    
    @Test
    public void testGetJavadocBefore() {
        fileContents = new FileContents("TestFile", "Line 1", "/** Javadoc Comment */", "Line 3");
        TextBlock javadocBlock = fileContents.getJavadocBefore(3);
        assertNotNull(javadocBlock);
        assertEquals("/** Javadoc Comment */", javadocBlock.getText()[0]);
    }
    
    @Test
    public void testGetText() {
        fileContents = new FileContents("TestFile");
        FileText fileText = fileContents.getText();
        assertNotNull(fileText);
        assertSame(fileContents.getText(), fileText);
    }
    
    @Test
    public void testGetLines() {
        fileContents = new FileContents("TestFile", "Line 1", "Line 2", "Line 3");
        String[] lines = fileContents.getLines();
        assertEquals(3, lines.length);
        assertEquals("Line 2", lines[1]);
    }
    
    @Test
    public void testGetLine() {
        fileContents = new FileContents("TestFile", "Line 1", "Line 2", "Line 3");
        assertEquals("Line 2", fileContents.getLine(1));
    }
    
    @Test
    public void testGetFileName() {
        fileContents = new FileContents("TestFile");
        assertEquals("TestFile", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        fileContents = new FileContents("TestFile");
        assertEquals("TestFile", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        fileContents = new FileContents("TestFile", "Line 1", "", "Line 3");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        fileContents = new FileContents("TestFile", "// Comment", "Line 2", "Line 3");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        fileContents = new FileContents("TestFile");
        assertFalse(fileContents.hasIntersectionWithComment(1, 2, 3, 4));
    }
    
    @Test
    public void testInPackageInfo() {
        fileContents = new FileContents("package-info.java");
        assertTrue(fileContents.inPackageInfo());
    }

}