
import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {
    
    @Test
    public void testConstructorWithFileAndCharsetName() {
        // Test with existing file and valid charset
        FileText fileText1 = new FileText(new File("test.txt"), "UTF-8");
        assertNotNull(fileText1.getFile());
        assertNotNull(fileText1.getCharset());
        assertNotNull(fileText1.getFullText());
        assertNotNull(fileText1.toLinesArray());
        
        // Test with non-existing file
        try {
            FileText fileText2 = new FileText(new File("nonExistingFile.txt"), "UTF-8");
            fail("Should have thrown FileNotFoundException");
        } catch (IOException e) {
            assertEquals("nonExistingFile.txt (No such file or directory)", e.getMessage());
        }
        
        // Test with unsupported charset
        try {
            FileText fileText3 = new FileText(new File("test.txt"), "InvalidCharset");
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: InvalidCharset", e.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithFileText() {
        FileText fileText1 = new FileText(new File("test.txt"), "UTF-8");
        FileText fileText2 = new FileText(fileText1);
        assertNotNull(fileText2.getFile());
        assertNotNull(fileText2.getCharset());
        assertNotNull(fileText2.getFullText());
        assertNotNull(fileText2.toLinesArray());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
        
        // Test with null lineBreaks
        FileText fileText3 = new FileText(new File("test.txt"), "UTF-8");
        fileText3.lineBreaks = null;
        FileText fileText4 = new FileText(fileText3);
        assertNull(fileText4.lineBreaks);
    }
    
    @Test
    public void testConstructorWithFileAndLines() {
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText = new FileText(new File("test.txt"), lines);
        assertNotNull(fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }
    
    @Test
    public void testFromLines() {
        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        FileText fileText1 = FileText.fromLines(new File("test.txt"), lines);
        assertNotNull(fileText1.getFile());
        assertNull(fileText1.getCharset());
        assertNotNull(fileText1.getFullText());
        assertNotNull(fileText1.toLinesArray());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText1.toLinesArray());
        
        FileText fileText2 = FileText.fromLines(new File("test.txt"), fileText1);
        assertSame(fileText1, fileText2);
    }
    
    @Test
    public void testGetFile() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        File file = fileText.getFile();
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
    }
    
    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        Charset charset = fileText.getCharset();
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }
    
    @Test
    public void testGetFullText() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        CharSequence fullText = fileText.getFullText();
        assertNotNull(fullText);
        assertEquals("line 1\nline 2\n", fullText.toString());
    }
    
    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        String[] linesArray = fileText.toLinesArray();
        assertNotNull(linesArray);
        assertArrayEquals(new String[]{"line 1", "line 2"}, linesArray);
    }
    
    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        LineColumn lineColumn1 = fileText.lineColumn(0);
        assertNotNull(lineColumn1);
        assertEquals(1, lineColumn1.getLine());
        assertEquals(0, lineColumn1.getColumn());
        
        LineColumn lineColumn2 = fileText.lineColumn(6);
        assertNotNull(lineColumn2);
        assertEquals(2, lineColumn2.getLine());
        assertEquals(0, lineColumn2.getColumn());
        
        LineColumn lineColumn3 = fileText.lineColumn(7);
        assertNotNull(lineColumn3);
        assertEquals(2, lineColumn3.getLine());
        assertEquals(1, lineColumn3.getColumn());
    }
    
    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertEquals("line 1", fileText.get(0));
        assertEquals("line 2", fileText.get(1));
    }
    
    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertEquals(2, fileText.size());
    }
}
