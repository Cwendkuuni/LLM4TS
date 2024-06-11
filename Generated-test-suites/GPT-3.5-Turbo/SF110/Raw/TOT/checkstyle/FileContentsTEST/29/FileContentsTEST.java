import static org.junit.Assert.*;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsStringStringArray() {
        // Typical use case
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        assertEquals("test.java", fileContents.getFileName());
        
        // Edge case - empty lines
        fileContents = new FileContents("test.java", "", "");
        assertEquals("test.java", fileContents.getFileName());
        
        // Error scenario - null filename
        try {
            fileContents = new FileContents(null, "line 1", "line 2");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
        
        // Error scenario - null lines
        try {
            fileContents = new FileContents("test.java", null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
    
    @Test
    public void testFileContentsFileText() {
        // Typical use case
        FileText fileText = new FileText(new File("test.java"), Arrays.asList("line 1", "line 2"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals("test.java", fileContents.getFileName());
        
        // Edge case - empty lines
        fileText = new FileText(new File("test.java"), Collections.emptyList());
        fileContents = new FileContents(fileText);
        assertEquals("test.java", fileContents.getFileName());
        
        // Error scenario - null FileText
        try {
            fileContents = new FileContents(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "// Comment", "line 3");
        fileContents.reportSingleLineComment("single", 2, 9);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        
        // Typical use case
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(2));
        
        // Edge case - empty comment
        fileContents = new FileContents("test.java", "line 1", "//", "line 3");
        fileContents.reportSingleLineComment("single", 2, 9);
        cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(2));
        
        // Error scenario - invalid start line number
        try {
            fileContents = new FileContents("test.java", "line 1", "// Comment", "line 3");
            fileContents.reportSingleLineComment("single", -1, 9);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "/* Comment */", "line 3");
        fileContents.reportBlockComment("block", 2, 2, 2, 15);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        
        // Typical use case
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(2));
        assertEquals(1, cComments.get(2).size());
        
        // Edge case - empty comment
        fileContents = new FileContents("test.java", "line 1", "/**/", "line 3");
        fileContents.reportBlockComment("block", 2, 2, 2, 4);
        cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(2));
        assertEquals(1, cComments.get(2).size());
        
        // Error scenario - invalid start line number
        try {
            fileContents = new FileContents("test.java", "line 1", "/* Comment */", "line 3");
            fileContents.reportBlockComment("block", -1, 2, 2, 15);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
        
        // Error scenario - invalid end column number
        try {
            fileContents = new FileContents("test.java", "line 1", "/* Comment */", "line 3");
            fileContents.reportBlockComment("block", 2, 2, 2, 40);
        } catch (Exception e) {
            assertTrue(e instanceof StringIndexOutOfBoundsException);
        }
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", "line 1");
        fileContents.reportBlockComment("block", 1, 2, 1, 15);
        TextBlock javadoc = fileContents.getJavadocBefore(1);
        
        // Typical use case
        assertNotNull(javadoc);
        
        // Edge case - no javadoc comment
        fileContents = new FileContents("test.java", "line 1");
        javadoc = fileContents.getJavadocBefore(1);
        assertNull(javadoc);
        
        // Error scenario - invalid line number
        try {
            fileContents = new FileContents("test.java", "line 1");
            javadoc = fileContents.getJavadocBefore(-1);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        FileText fileText = fileContents.getText();
        
        // Typical use case
        assertNotNull(fileText);
        assertArrayEquals(new String[] {"line 1", "line 2"}, fileText.toLinesArray());
        
        // Edge case - empty lines
        fileContents = new FileContents("test.java", "", "");
        fileText = fileContents.getText();
        assertNotNull(fileText);
        assertArrayEquals(new String[] {"", ""}, fileText.toLinesArray());
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        String[] lines = fileContents.getLines();
        
        // Typical use case
        assertNotNull(lines);
        assertArrayEquals(new String[] {"line 1", "line 2"}, lines);
        
        // Edge case - empty lines
        fileContents = new FileContents("test.java", "", "");
        lines = fileContents.getLines();
        assertNotNull(lines);
        assertArrayEquals(new String[] {"", ""}, lines);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.java", "line 1", "line 2");
        String line = fileContents.getLine(0);
        
        // Typical use case
        assertEquals("line 1", line);
        
        // Edge case - invalid index
        try {
            fileContents.getLine(2);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", "line 1", "", "line 3");
        
        // Typical use case
        assertTrue(fileContents.lineIsBlank(1));
        
        // Edge case - non-empty line
        assertFalse(fileContents.lineIsBlank(0));
        
        // Edge case - empty line
        assertTrue(fileContents.lineIsBlank(1));
        
        // Error scenario - invalid line number
        try {
            fileContents.lineIsBlank(-1);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "// Comment", "/* Comment */");
        
        // Typical use case
        assertFalse(fileContents.lineIsComment(0));
        
        // Edge case - single-line comment
        assertTrue(fileContents.lineIsComment(1));
        
        // Edge case - block comment
        assertFalse(fileContents.lineIsComment(2));
        
        // Error scenario - invalid line number
        try {
            fileContents.lineIsComment(3);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java", "line 1", "// Comment", "/* Comment */");
        
        // Typical use case
        assertTrue(fileContents.hasIntersectionWithComment(3, 2, 3, 10));
        
        // Edge case - no intersection
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 5));
        
        // Edge case - intersection with C++ comment
        assertTrue(fileContents.hasIntersectionWithComment(2, 10, 2, 20));
        
        // Edge case - intersection with block comment
        assertTrue(fileContents.hasIntersectionWithComment(2, 2, 2, 15));
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line 1");
        
        // Typical use case
        assertTrue(fileContents.inPackageInfo());
        
        // Edge case - not in package-info.java
        fileContents = new FileContents("test.java", "line 1");
        assertFalse(fileContents.inPackageInfo());
    }
}