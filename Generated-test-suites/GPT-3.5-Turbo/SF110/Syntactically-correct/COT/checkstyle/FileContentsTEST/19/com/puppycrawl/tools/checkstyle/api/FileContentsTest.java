
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

public class FileContentsTest {
    
    private FileContents fileContents;
    
    @Before
    public void setup() {
        fileContents = new FileContents("testFile.java", "public class TestFile {", "    // This is a single-line comment", "    /* This is a block comment */", "}");
    }
    
    @Test
    public void testConstructorWithFileNameAndLines() {
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile.java"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("testFile.java", fileContents.getFileName());
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 2, 5);
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 3, 5, 4, 7);
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(2, 5);
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testGetCppComments() {
        fileContents.reportCppComment(2, 5);
        assertEquals(1, fileContents.getCppComments().size());
    }
    
    @Test
    public void testReportCComment() {
        fileContents.reportCComment(3, 5, 4, 7);
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testGetCComments() {
        fileContents.reportCComment(3, 5, 4, 7);
        assertEquals(1, fileContents.getCComments().size());
    }
    
    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents.extractCComment(3, 5, 4, 7);
        assertEquals(2, cComment.length);
    }
    
    @Test
    public void testGetJavadocBefore() {
        assertEquals(null, fileContents.getJavadocBefore(1));
    }
    
    @Test
    public void testGetText() {
        FileText fileText = fileContents.getText();
        assertEquals("public class TestFile {", fileText.get(0));
    }
    
    @Test
    public void testGetLines() {
        String[] lines = fileContents.getLines();
        assertEquals(3, lines.length);
    }
    
    @Test
    public void testGetLine() {
        String line = fileContents.getLine(1);
        assertEquals("public class TestFile {", line);
    }
    
    @Test
    public void testGetFilename() {
        assertEquals("testFile.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 6));
        assertTrue(fileContents.hasIntersectionWithComment(2, 5, 2, 27));
    }
    
    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
    
    private class CommentListenerImpl implements CommentListener {
        @Override
        public void reportSingleLineComment(String type, int startLineNo, int startColNo) {
            
        }
        
        @Override
        public void reportBlockComment(String type, int startLineNo, int startColNo, int endLineNo, int endColNo) {
            
        }
    }
    
}

