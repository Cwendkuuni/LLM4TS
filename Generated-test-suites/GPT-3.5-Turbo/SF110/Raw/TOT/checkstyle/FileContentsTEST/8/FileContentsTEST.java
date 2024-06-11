import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

public class FileContentsTest {
    
    private FileContents fileContents;
    private String[] lines;
    private FileText fileText;
    
    @Before
    public void setUp() {
        lines = new String[]{"This is line 1", "This is line 2"};
        fileText = new FileText(new File("test.txt"), Arrays.asList(lines));
        fileContents = new FileContents(fileText);
    }
    
    @Test
    public void testReportSingleLineComment() {
        fileContents.reportSingleLineComment("type", 1, 1);
        assertNotNull(fileContents.getCppComments().get(1));
        assertEquals("This is line 1", fileContents.getCppComments().get(1).getText()[0]);
    }

    @Test
    public void testReportBlockComment() {
        fileContents.reportBlockComment("type", 1, 1, 2, 5);
        assertNotNull(fileContents.getCComments().get(1));
        assertEquals("This is line 1", fileContents.getCComments().get(1).get(0).getText()[0]);
        assertEquals("This is line 2", fileContents.getCComments().get(2).get(0).getText()[0]);
    }

    @Test
    public void testReportCppComment() {
        fileContents.reportCppComment(1, 1);
        assertNotNull(fileContents.getCppComments().get(1));
        assertEquals("This is line 1", fileContents.getCppComments().get(1).getText()[0]);
    }

    @Test
    public void testGetCppComments() {
        fileContents.reportCppComment(1, 1);
        assertEquals(1, fileContents.getCppComments().size());
    }

    @Test
    public void testReportCComment() {
        fileContents.reportCComment(1, 1, 2, 5);
        assertNotNull(fileContents.getCComments().get(1));
        assertEquals("This is line 1", fileContents.getCComments().get(1).get(0).getText()[0]);
        assertEquals("This is line 2", fileContents.getCComments().get(2).get(0).getText()[0]);
    }

    @Test
    public void testGetCComments() {
        fileContents.reportCComment(1, 1, 2, 5);
        assertEquals(1, fileContents.getCComments().size());
        assertEquals(2, fileContents.getCComments().get(1).size());
    }

    @Test
    public void testExtractCComment() {
        String[] cComment = fileContents.extractCComment(1, 1, 2, 5);
        assertNotNull(cComment);
        assertEquals("This is line 1", cComment[0]);
        assertEquals("This is line 2", cComment[1]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportCComment(1, 1, 2, 5);
        assertEquals(null, fileContents.getJavadocBefore(1));
        assertNotNull(fileContents.getJavadocBefore(2));
        assertEquals("This is line 1", fileContents.getJavadocBefore(2).getText()[0]);
        assertEquals("This is line 2", fileContents.getJavadocBefore(2).getText()[1]);
    }

    @Test
    public void testGetText() {
        assertEquals(fileText, fileContents.getText());
    }

    @Test
    public void testGetLines() {
        assertArrayEquals(lines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("This is line 1", fileContents.getLine(0));
    }

    @Test
    public void testGetFileName() {
        assertEquals("test.txt", fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        assertEquals("test.txt", fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 5));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testHasIntersectionWithCComment() {
        assertFalse(fileContents.hasIntersectionWithCComment(1, 1, 2, 5));
    }

    @Test
    public void testHasIntersectionWithCppComment() {
        assertFalse(fileContents.hasIntersectionWithCppComment(1, 1, 2, 5));
    }
}