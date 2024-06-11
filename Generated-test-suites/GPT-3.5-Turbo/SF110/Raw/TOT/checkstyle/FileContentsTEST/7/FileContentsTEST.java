import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {

    @Test
    public void testConstructorWithFilenameAndLines() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        assertEquals("testFile", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText = new FileText(new File("testFile"), Arrays.asList("line1", "line2"));
        FileContents fileContents = new FileContents(fileText);
        
        assertEquals("testFile", fileContents.getFileName());
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
    }
    
    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        fileContents.reportSingleLineComment("type", 1, 1);
        
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(1));
        assertEquals("line1", cppComments.get(1).getText()[0]);
    }
    
    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        fileContents.reportBlockComment("type", 1, 1, 2, 2);
        
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(1));
        assertEquals(1, cComments.get(1).size());
        assertEquals("line1", cComments.get(1).get(0).getText()[0]);
        assertEquals("line2", cComments.get(1).get(0).getText()[1]);
    }
    
    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        fileContents.reportSingleLineComment("type", 1, 1);
        
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        
        assertNotNull(cppComments);
        assertEquals(1, cppComments.size());
        assertTrue(cppComments.containsKey(1));
        assertEquals("line1", cppComments.get(1).getText()[0]);
    }
    
    @Test
    public void testReportCComment() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        fileContents.reportCComment(1, 1, 2, 2);
        
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(1));
        assertEquals(1, cComments.get(1).size());
        assertEquals("line1", cComments.get(1).get(0).getText()[0]);
        assertEquals("line2", cComments.get(1).get(0).getText()[1]);
    }
    
    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        fileContents.reportCComment(1, 1, 2, 2);
        
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        
        assertNotNull(cComments);
        assertEquals(1, cComments.size());
        assertTrue(cComments.containsKey(1));
        assertEquals(1, cComments.get(1).size());
        assertEquals("line1", cComments.get(1).get(0).getText()[0]);
        assertEquals("line2", cComments.get(1).get(0).getText()[1]);
    }
    
    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        fileContents.reportCComment(1, 1, 2, 2);
        fileContents.reportBlockComment("type", 3, 1, 4, 2);
        
        TextBlock javadocBefore = fileContents.getJavadocBefore(3);
        
        assertNotNull(javadocBefore);
        assertEquals("line1", javadocBefore.getText()[0]);
        assertEquals("line2", javadocBefore.getText()[1]);
    }
    
    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        FileText text = fileContents.getText();
        
        assertNotNull(text);
        assertEquals("testFile", text.getFile().toString());
        assertEquals("line1", text.get(0));
        assertEquals("line2", text.get(1));
    }
    
    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        String[] lines = fileContents.getLines();
        
        assertNotNull(lines);
        assertEquals(2, lines.length);
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
    }
    
    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        String line1 = fileContents.getLine(0);
        String line2 = fileContents.getLine(1);
        
        assertEquals("line1", line1);
        assertEquals("line2", line2);
    }
    
    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        String fileName = fileContents.getFileName();
        
        assertEquals("testFile", fileName);
    }
    
    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("testFile", "line1", "line2");
        
        String filename = fileContents.getFilename();
        
        assertEquals("testFile", filename);
    }
    
    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("testFile", "line1", "");
        
        boolean line1IsBlank = fileContents.lineIsBlank(0);
        boolean line2IsBlank = fileContents.lineIsBlank(1);
        
        assertFalse(line1IsBlank);
        assertTrue(line2IsBlank);
    }
    
    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("testFile", "line1", "// comment");
        
        boolean line1IsComment = fileContents.lineIsComment(0);
        boolean line2IsComment = fileContents.lineIsComment(1);
        
        assertFalse(line1IsComment);
        assertTrue(line2IsComment);
    }
    
    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("testFile", "line1", "// comment");
        
        boolean hasIntersection = fileContents.hasIntersectionWithComment(0, 0, 1, 10);
        
        assertTrue(hasIntersection);
    }
    
    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", "line1");
        
        boolean inPackageInfo = fileContents.inPackageInfo();
        
        assertTrue(inPackageInfo);
    }
}