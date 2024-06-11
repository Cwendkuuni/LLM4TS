
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;

public class FileContentsTest {

    @Test
    public void testConstructorWithFilenameAndLines() {
        String fileName = "testFile.java";
        String[] lines = {
            "public class Test {",
            "    private int num;",
            "}"
        };
        
        FileContents fileContents = new FileContents(fileName, lines);
        
        assertEquals(fileName, fileContents.getFileName());
        assertArrayEquals(lines, fileContents.getLines());
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText("testFile.java", "public class Test { }");
        
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals("public class Test { }", fileContents.getText().getText());
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile.java", "public class Test { // This is a comment }");
        
        fileContents.reportSingleLineComment("", 2, 21);
        
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        
        assertEquals(1, cppComments.size());
        assertNotNull(cppComments.get(2));
        assertEquals("// This is a comment ", cppComments.get(2).getText());
        assertEquals(21, cppComments.get(2).getStartCol());
        assertEquals(43, cppComments.get(2).getEndCol());
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile.java", "public class Test { /* This is a block comment */ }");
        
        fileContents.reportBlockComment("", 2, 21, 2, 46);
        
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        
        assertEquals(1, cComments.size());
        assertNotNull(cComments.get(2));
        assertEquals(1, cComments.get(2).size());
        
        TextBlock comment = cComments.get(2).get(0);
        
        assertEquals("/* This is a block comment */", comment.getText());
        assertEquals(21, comment.getStartCol());
        assertEquals(46, comment.getEndCol());
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile.java", "/**",
                                                      " * This is a Javadoc comment",
                                                      " */",
                                                      "public class Test { }");
        
        fileContents.reportBlockComment("", 3, 1, 5, 3);
        
        TextBlock javadocComment = fileContents.getJavadocBefore(3);
        
        assertEquals("/**\n * This is a Javadoc comment\n */", javadocComment.getText());
        assertEquals(1, javadocComment.getStartCol());
        assertEquals(3, javadocComment.getEndCol());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile.java", "",
                                                      "public class Test {",
                                                      "    private int num;",
                                                      "}");
        
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(3));
        assertFalse(fileContents.lineIsBlank(4));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile.java", "// This is a comment");
        
        assertTrue(fileContents.lineIsComment(0));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile.java", "public class Test { /* This is a comment */ }");
        
        assertTrue(fileContents.hasIntersectionWithComment(2, 25, 2, 42));
        assertFalse(fileContents.hasIntersectionWithComment(2, 0, 2, 10));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        
        assertTrue(fileContents.inPackageInfo());
        
        FileContents fileContents2 = new FileContents("testFile.java");
        
        assertFalse(fileContents2.inPackageInfo());
    }
}
