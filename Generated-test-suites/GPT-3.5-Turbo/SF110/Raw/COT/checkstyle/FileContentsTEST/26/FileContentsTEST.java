import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithStringLines() {
        // Arrange
        String fileName = "testFile.java";
        String[] lines = {"line1", "line2", "line3"};
        
        // Act
        FileContents fileContents = new FileContents(fileName, lines);
        
        // Assert
        assertEquals(fileName, fileContents.getFileName());
        assertArrayEquals(lines, fileContents.getLines());
    }
    
    @Test
    public void testFileContentsConstructorWithFileText() {
        // Arrange
        FileText fileText = new FileText(new File("testFile.java"));
        
        // Act
        FileContents fileContents = new FileContents(fileText);
        
        // Assert
        assertEquals(fileText.getFile().toString(), fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "// comment", "line3");
        
        // Act
        fileContents.reportSingleLineComment("single", 2, 1);
        
        // Assert
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportBlockComment() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "/* comment", "comment line2 */", "line4");
        
        // Act
        fileContents.reportBlockComment("block", 2, 1, 3, 15);
        
        // Assert
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testGetJavadocBefore() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "/* comment", "line3", "/** javadoc */", "line5");
        
        // Act
        TextBlock javadocBefore = fileContents.getJavadocBefore(5);
        
        // Assert
        assertNotNull(javadocBefore);
        assertEquals("/** javadoc */", javadocBefore.getText()[0]);
    }
    
    @Test
    public void testGetLine() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "line2", "line3");
        
        // Act
        String line = fileContents.getLine(1);
        
        // Assert
        assertEquals("line2", line);
    }
    
    @Test
    public void testLineIsBlank() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "", "line3");
        
        // Act
        boolean isBlank = fileContents.lineIsBlank(1);
        
        // Assert
        assertFalse(isBlank);
    }
    
    @Test
    public void testLineIsComment() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "// comment", "line3");
        
        // Act
        boolean isComment = fileContents.lineIsComment(1);
        
        // Assert
        assertTrue(isComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        // Arrange
        FileContents fileContents = new FileContents("testFile.java", "line1", "// comment", "line3", "/* block comment */", "line5");
        
        // Act
        boolean hasIntersection = fileContents.hasIntersectionWithComment(2, 1, 3, 10);
        
        // Assert
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        // Arrange
        FileContents fileContents = new FileContents("package-info.java");
        
        // Act
        boolean inPackageInfo = fileContents.inPackageInfo();
        
        // Assert
        assertTrue(inPackageInfo);
    }

}