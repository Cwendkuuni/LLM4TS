import org.junit.Test; 
import org.junit.Before; 
import static org.junit.Assert.*; 
import java.util.List; 
import java.util.Map;

public class FileContentsTest { 
    private FileContents fileContents;

    @Before 
    public void setUp() { 
        fileContents = new FileContents("test.txt", "This is line 1", "This is line 2"); 
    }

    @Test 
    public void testReportSingleLineComment() { 
        fileContents.reportSingleLineComment("type", 1, 1); 
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments(); 
        assertTrue(cppComments.containsKey(1)); 
        assertEquals("This is line 1", cppComments.get(1).getLines()[0]); 
    }

    @Test 
    public void testReportBlockComment() { 
        fileContents.reportBlockComment("type", 1, 1, 2, 1); 
        Map<Integer, List<TextBlock>> clangComments = fileContents.getCComments(); 
        assertTrue(clangComments.containsKey(1)); 
        assertEquals(1, clangComments.get(1).size()); 
        assertEquals("This is line 1", clangComments.get(1).get(0).getLines()[0]); 
        assertEquals("This is line 2", clangComments.get(1).get(0).getLines()[1]); 
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents.reportBlockComment("type", 1, 1, 2, 1);
        TextBlock javadoc = fileContents.getJavadocBefore(3);
        assertEquals("This is line 1", javadoc.getLines()[0]);
        assertEquals("This is line 2", javadoc.getLines()[1]);
    }

    @Test
    public void testLineIsBlank() {
        assertTrue(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 1));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}