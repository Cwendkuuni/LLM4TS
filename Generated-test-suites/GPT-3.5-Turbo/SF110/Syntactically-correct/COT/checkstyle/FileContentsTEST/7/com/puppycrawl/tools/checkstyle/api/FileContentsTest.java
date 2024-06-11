
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {
    private FileContents fileContents;
    
    @Before
    public void setUp() {
        String[] lines = {
            "// This is a comment",
            "public class MyClass {",
            "    /* This is a block comment",
            "       spanning multiple lines */",
            "    int myVariable;",
            "}",
            "/*",
            "    This is another block comment",
            "*/"
        };
        fileContents = new FileContents("test_file.java", lines);
    }
    
    @Test
    public void testReportCppComment_singleLineComment() {
        fileContents.reportCppComment(1, 4);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue(cppComments.containsKey(1));
        assertEquals(" This is a comment", cppComments.get(1).getText()[0]);
    }
    
    @Test
    public void testReportCComment_blockComment() {
        fileContents.reportCComment(3, 4, 5, 16);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertTrue(cComments.containsKey(3));
        assertEquals(1, cComments.get(3).size());
        assertEquals(" This is a block comment\n       spanning multiple lines ", cComments.get(3).get(0).getText()[0]);
    }
    
    @Test
    public void testExtractCComment_singleLineComment() {
        String[] cComment = fileContents.extractCComment(7, 4, 7, 26);
        assertEquals(1, cComment.length);
        assertEquals("    This is another block comment", cComment[0]);
    }
    
    @Test
    public void testExtractCComment_blockComment() {
        String[] cComment = fileContents.extractCComment(3, 4, 7, 3);
        assertEquals(5, cComment.length);
        assertEquals(" This is a block comment", cComment[0]);
        assertEquals("       spanning multiple lines ", cComment[1]);
        assertEquals("    int myVariable;", cComment[2]);
        assertEquals("}", cComment[3]);
        assertEquals("/*", cComment[4]);
    }
    
    @Test
    public void testGetJavadocBefore_noJavadoc() {
        assertNull(fileContents.getJavadocBefore(3));
    }
    
    @Test
    public void testGetJavadocBefore_withJavadoc() {
        fileContents.reportCComment(2, 4, 3, 16);
        fileContents.reportCComment(5, 4, 6, 2);
        TextBlock javadoc = fileContents.getJavadocBefore(6);
        assertNotNull(javadoc);
        assertEquals(1, javadoc.getText().length);
        assertEquals(" This is a block comment\n       spanning multiple lines ", javadoc.getText()[0]);
    }
    
    @Test
    public void testLineIsBlank_blankLine() {
        assertTrue(fileContents.lineIsBlank(4));
    }
    
    @Test
    public void testLineIsBlank_nonBlankLine() {
        assertFalse(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment_commentLine() {
        assertTrue(fileContents.lineIsComment(1));
    }
    
    @Test
    public void testLineIsComment_nonCommentLine() {
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment_withIntersection() {
        assertTrue(fileContents.hasIntersectionWithComment(1, 5, 1, 20));
    }
    
    @Test
    public void testHasIntersectionWithComment_noIntersection() {
        assertFalse(fileContents.hasIntersectionWithComment(2, 8, 3, 12));
    }
    
    @Test
    public void testInPackageInfo_withPackageInfo() {
        fileContents = new FileContents("package-info.java");
        assertTrue(fileContents.inPackageInfo());
    }
    
    @Test
    public void testInPackageInfo_withoutPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

}
