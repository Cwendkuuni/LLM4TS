import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FileContentsTest {
    private final String[] lines = {
        "public class HelloWorld {",
        "    // This is a single line comment",
        "    /*",
        "     * This is a multi",
        "     * line comment",
        "     */",
        "    /**",
        "     * This is a JavaDoc comment",
        "     */",
        "}"
    };
    
    @Test
    public void testFileContentsConstructor() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        assertEquals("FileName.java", fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        assertEquals(lines[0], fileContents.getLine(0));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportSingleLineComment("single", 1, 14);
        
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportBlockComment("block", 3, 5, 5, 19);
        
        assertFalse(fileContents.lineIsComment(2));
        assertTrue(fileContents.lineIsComment(3));
        assertTrue(fileContents.lineIsComment(4));
        assertTrue(fileContents.lineIsComment(5));
        assertFalse(fileContents.lineIsComment(6));
    }
    
    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportCppComment(1, 21);
        
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportCppComment(1, 21);
        
        assertEquals(1, fileContents.getCppComments().size());
        assertTrue(fileContents.getCppComments().containsKey(1));
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportCComment(3, 5, 5, 19);
        
        assertFalse(fileContents.lineIsComment(2));
        assertTrue(fileContents.lineIsComment(3));
        assertTrue(fileContents.lineIsComment(4));
        assertTrue(fileContents.lineIsComment(5));
        assertFalse(fileContents.lineIsComment(6));
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportCComment(3, 5, 5, 19);
        
        assertEquals(1, fileContents.getCComments().size());
        assertTrue(fileContents.getCComments().containsKey(3));
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        fileContents.reportCComment(6, 5, 6, 34);
        
        assertNull(fileContents.getJavadocBefore(5));
        assertEquals("/**\n * This is a JavaDoc comment\n */", fileContents.getJavadocBefore(6).toString());
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        assertEquals("public class HelloWorld {\n    // This is a single line comment\n    /*\n     * This is a multi\n     * line comment\n     */\n    /**\n     * This is a JavaDoc comment\n     */\n}", fileContents.getText().toString());
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        assertEquals("FileName.java", fileContents.getFileName());
        assertEquals("FileName.java", fileContents.getFilename());
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("FileName.java", lines);
        
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 10));
        assertTrue(fileContents.hasIntersectionWithComment(1, 10, 5, 0));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", lines);
        
        assertTrue(fileContents.inPackageInfo());
    }
}