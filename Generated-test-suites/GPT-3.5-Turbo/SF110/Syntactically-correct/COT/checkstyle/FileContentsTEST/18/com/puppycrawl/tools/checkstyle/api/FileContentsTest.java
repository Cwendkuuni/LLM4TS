import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorLines() {
        String[] lines = {"Hello", "World"};
        FileContents fileContents = new FileContents("testFile.txt", lines);
        assertEquals("testFile.txt", fileContents.getFileName());
        assertEquals("Hello", fileContents.getLine(0));
        assertEquals("World", fileContents.getLine(1));
    }
    
    @Test
    public void testFileContentsConstructorFileText() {
        String[] lines = {"Hello", "World"};
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList(lines));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("testFile.txt", fileContents.getFileName());
        assertEquals("Hello", fileContents.getLine(0));
        assertEquals("World", fileContents.getLine(1));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "// This is a comment");
        fileContents.reportSingleLineComment("type", 2, 1);
        assertEquals("// This is a comment", fileContents.getCppComments().get(2).getText()[0]);
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "/*", "This is a block", "comment", "*/");
        fileContents.reportBlockComment("type", 2, 1, 4, 2);
        assertEquals("This is a block", fileContents.getCComments().get(2).get(0).getText()[0]);
        assertEquals("comment", fileContents.getCComments().get(2).get(0).getText()[1]);
        assertEquals("*/", fileContents.getCComments().get(2).get(0).getText()[2]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "/**", "* This is a", "* Javadoc comment", "*/");
        assertEquals("This is a Javadoc comment", 
                     fileContents.getJavadocBefore(5).getText()[0]);
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "World");
        FileText fileText = fileContents.getText();
        assertEquals("Hello", fileText.get(0));
        assertEquals("World", fileText.get(1));
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "World");
        String[] lines = fileContents.getLines();
        assertEquals("Hello", lines[0]);
        assertEquals("World", lines[1]);
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "World");
        assertEquals("testFile.txt", fileContents.getFileName());
    }
    
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "World");
        assertEquals("testFile.txt", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "");
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(0));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "// This is a comment");
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "// This is a comment");
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 4));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 4));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents packageInfo = new FileContents("package-info.java", "This is package info");
        FileContents fileContents = new FileContents("testFile.txt", "Hello", "World");
        assertTrue(packageInfo.inPackageInfo());
        assertFalse(fileContents.inPackageInfo());
    }
}