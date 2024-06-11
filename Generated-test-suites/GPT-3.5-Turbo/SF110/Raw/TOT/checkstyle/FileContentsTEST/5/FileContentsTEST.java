import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class FileContentsTest {
    
    private FileContents fileContents1;
    private FileContents fileContents2;
    
    @Before
    public void setUp() {
        fileContents1 = new FileContents("File1.java", "public class File1 {", "    // Single line comment", "    /* Block comment */", "}");
        fileContents2 = new FileContents("File2.java", "public class File2 {", "    // Single line comment", "    /* Block comment */", "    // Another single line comment", "}");
    }
    
    @Test
    public void testConstructorWithFileNameAndLines() {
        assertEquals("File1.java", fileContents1.getFileName());
        assertEquals(4, fileContents1.getLines().length);
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText("public class File3 {", "    // Single line comment", "}");
        FileContents fileContents3 = new FileContents(fileText);
        assertEquals("File3.java", fileContents3.getFileName());
        assertEquals(3, fileContents3.getLines().length);
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents1.reportSingleLineComment("type", 2, 4);
        assertTrue(fileContents1.getCppComments().containsKey(2));
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents1.reportBlockComment("type", 3, 5, 3, 22);
        assertTrue(fileContents1.getCComments().containsKey(3));
    }
    
    @Test
    public void testReportCppComment() {
        fileContents1.reportCppComment(2, 4);
        assertTrue(fileContents1.getCppComments().containsKey(2));
    }
    
    @Test
    public void testGetCppComments() {
        fileContents1.reportCppComment(2, 4);
        assertEquals(1, fileContents1.getCppComments().size());
    }
    
    @Test
    public void testReportCComment() {
        fileContents1.reportCComment(3, 5, 3, 22);
        assertTrue(fileContents1.getCComments().containsKey(3));
    }
    
    @Test
    public void testGetCComments() {
        fileContents1.reportCComment(3, 5, 3, 22);
        assertEquals(1, fileContents1.getCComments().size());
    }
    
    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents1.extractCComment(3, 5, 3, 22);
        assertEquals(1, cComment.length);
        assertEquals(" Block comment ", cComment[0]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        fileContents1.reportCComment(2, 5, 3, 10);
        TextBlock javadocBefore = fileContents1.getJavadocBefore(4);
        assertNotNull(javadocBefore);
    }
    
    @Test
    public void testGetText() {
        FileText text = fileContents1.getText();
        assertNotNull(text);
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents1.getLines();
        assertEquals(4, lines.length);
    }
    
    @Test
    public void testGetLine() {
        String line = fileContents1.getLine(1);
        assertEquals("// Single line comment", line);
    }
    
    @Test
    public void testGetFileName() {
        assertEquals("File1.java", fileContents1.getFileName());
    }
    
    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents1.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        assertTrue(fileContents1.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        boolean hasIntersection = fileContents1.hasIntersectionWithComment(2, 4, 2, 22);
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents1.inPackageInfo());
        assertTrue(fileContents2.inPackageInfo());
    }
    
    @Test
    public void testHasIntersectionWithCComment() {
        boolean hasIntersection = fileContents1.hasIntersectionWithCComment(3, 5, 3, 22);
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testHasIntersectionWithCppComment() {
        boolean hasIntersection = fileContents2.hasIntersectionWithCppComment(3, 5, 5, 22);
        assertTrue(hasIntersection);
    }
}