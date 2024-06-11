
import static org.junit.Assert.*;
import org.junit.Test;

public class FileTextTest {

    // Test for the constructor FileText(File, String)
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        
        // Test error scenario - unsupported charset
        try {
            FileText fileText2 = new FileText(file, "invalidCharset");
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }

        // Test error scenario - file not found
        try {
            FileText fileText3 = new FileText(new File("nonExistingFile.txt"), charsetName);
            fail("Should throw FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    // Test for the constructor FileText(FileText)
    @Test
    public void testConstructorWithFileText() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText1 = new FileText(file, charsetName);
        
        FileText fileText2 = new FileText(fileText1);
        
        assertEquals(file, fileText2.getFile());
        assertEquals(charsetName, fileText2.getCharset().name());
        assertEquals(fileText1.getFullText(), fileText2.getFullText());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
        
        // Test when lineBreaks is null
        fileText1.lineBreaks = null;
        FileText fileText3 = new FileText(fileText1);
        
        assertNull(fileText3.lineBreaks);
    }
    
    // Test for the constructor FileText(File, List)
    @Test
    public void testConstructorWithFileAndLinesList() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        
        FileText fileText = new FileText(file, lines);
        
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }
    
    // Test for the static method fromLines(File, List)
    @Test
    public void testStaticMethodFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        
        FileText fileText1 = FileText.fromLines(file, lines);
        
        assertSame(lines, fileText1);
        
        // Test when lines is an instance of FileText
        FileText fileText2 = new FileText(fileText1);
        
        assertEquals(fileText1.getFile(), fileText2.getFile());
        assertEquals(fileText1.getCharset(), fileText2.getCharset());
        assertEquals(fileText1.getFullText(), fileText2.getFullText());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
    }
    
    // Test for the method getFile()
    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(file, fileText.getFile());
    }
    
    // Test for the method getCharset()
    @Test
    public void testGetCharset() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }
    
    // Test for the method getFullText()
    @Test
    public void testGetFullText() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertEquals("This is the content of the file.", fileText.getFullText());
    }
    
    // Test for the method toLinesArray()
    @Test
    public void testToLinesArray() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertArrayEquals(new String[] { "Line 1", "Line 2", "Line 3" }, fileText.toLinesArray());
    }
    
    // Test for the private method findLineBreaks()
    @Test
    public void testFindLineBreaks() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(new File("testFile.txt"), lines);
        
        int[] expectedLineBreaks = { 0, 7, 14, 21 };
        
        assertArrayEquals(expectedLineBreaks, fileText.findLineBreaks());
    }
    
    // Test for the method lineColumn(int)
    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 1), fileText.lineColumn(1));
        assertEquals(new LineColumn(1, 13), fileText.lineColumn(13));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(14));
        assertEquals(new LineColumn(2, 2), fileText.lineColumn(16));
        assertEquals(new LineColumn(2, 13), fileText.lineColumn(27));
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(28));
        assertEquals(new LineColumn(3, 5), fileText.lineColumn(33));
        assertEquals(new LineColumn(3, 11), fileText.lineColumn(39));
    }
    
    // Test for the method get(int)
    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }
    
    // Test for the method size()
    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        
        assertEquals(3, fileText.size());
    }
}
