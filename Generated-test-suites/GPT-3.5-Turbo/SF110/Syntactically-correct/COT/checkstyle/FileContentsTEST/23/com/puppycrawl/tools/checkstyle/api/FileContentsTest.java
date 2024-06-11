import org.junit.Test;

public class FileContentsTest {
    
    // Test constructor FileContents(String, String...)
    @Test
    public void testConstructorWithLines() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        assertEquals("test.java", fileContents.getFileName());
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
    }
    
    // Test constructor FileContents(FileText)
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("test.java"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.java", fileContents.getFileName());
    }
    
    // Test reportSingleLineComment(String, int, int)
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java", "// Single line comment");
        fileContents.reportSingleLineComment("-", 1, 3);
        assertEquals("comment", fileContents.getCppComments().get(1).getText()[0]);
    }
    
    // Test reportBlockComment(String, int, int, int, int)
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java", "/* Block comment */");
        fileContents.reportBlockComment("-", 1, 1, 1, 16);
        assertEquals("Block comment", fileContents.getCComments().get(1).get(0).getText()[0]);
    }
    
    // Test getJavadocBefore(int)
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", "/* Javadoc comment */", "line 1");
        fileContents.reportBlockComment("-", 1, 1, 1, 20);
        TextBlock javadoc = fileContents.getJavadocBefore(2);
        assertEquals("Javadoc comment", javadoc.getText()[0]);
    }

    // Test getText()
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        FileText text = fileContents.getText();
        assertEquals("line 1", text.get(0));
        assertEquals("line 2", text.get(1));
    }

    // Test getLines()
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        String[] lines = fileContents.getLines();
        assertEquals("line 1", lines[0]);
        assertEquals("line 2", lines[1]);
    }
    
    // Test getLine(int)
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        assertEquals("line 1", fileContents.getLine(0));
        assertEquals("line 2", fileContents.getLine(1));
    }
    
    // Test getFileName()
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        assertEquals("test.java", fileContents.getFileName());
    }
    
    // Test getFilename() - Deprecated
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        assertEquals("test.java", fileContents.getFilename());
    }
    
    // Test lineIsBlank(int)
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "line 1", "", "line 3");
        assertTrue(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(3));
    }

    // Test lineIsComment(int)
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "// Single line comment", "line 2");
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }
    
    // Test hasIntersectionWithComment(int, int, int, int)
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java", "/* Block comment */", "// Single line comment");
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 16));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 1, 16));
    }
    
    // Test inPackageInfo()
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        assertTrue(fileContents.inPackageInfo());
        fileContents = new FileContents("test.java");
        assertFalse(fileContents.inPackageInfo());
    }
    
    // Test static initializer
    @Test
    public void testStaticInitializer() {
        assertNotNull(FileContents.MATCH_SINGLELINE_COMMENT);
    }
    
}