
import org.junit.*;
import static org.junit.Assert.*;

public class FileTextTest {
    
    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull("FileText object should not be null", fileText);
        assertEquals("File should be set correctly", file, fileText.getFile());
        assertEquals("Charset should be set correctly", Charset.forName(charsetName), fileText.getCharset());
        assertNotNull("Full text should not be null", fileText.getFullText());
        assertNotNull("Lines array should not be null", fileText.toLinesArray());
        assertEquals("Size should match number of lines", fileText.toLinesArray().length, fileText.size());
    }
    
    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        FileText copy = new FileText(fileText);
        
        assertNotNull("Copied FileText object should not be null", copy);
        assertEquals("File should be set correctly in the copy", file, copy.getFile());
        assertEquals("Charset should be set correctly in the copy", Charset.forName(charsetName), copy.getCharset());
        assertEquals("FullText should be set correctly in the copy", fileText.getFullText(), copy.getFullText());
        assertArrayEquals("Lines array should be the same in the copy", fileText.toLinesArray(), copy.toLinesArray());
        assertArrayEquals("LineBreaks array should be the same in the copy", fileText.lineBreaks, copy.lineBreaks);
    }
    
    @Test
    public void testFromFileLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull("FileText object should not be null", fileText);
        assertEquals("File should be set correctly", file, fileText.getFile());
        assertNull("Charset should be null", fileText.getCharset());
        assertNotNull("Full text should not be null", fileText.getFullText());
        assertNotNull("Lines array should not be null", fileText.toLinesArray());
        assertEquals("Size should match number of lines", fileText.toLinesArray().length, fileText.size());
    }
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals("File should be returned correctly", file, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals("Charset should be returned correctly", Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull("Full text should not be null", fileText.getFullText());
    }
    
    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull("Lines array should not be null", fileText.toLinesArray());
    }
    
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull("LineBreaks array should not be null", fileText.findLineBreaks());
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        int pos = 10;
        LineColumn lineColumn = fileText.lineColumn(pos);
        
        assertNotNull("LineColumn object should not be null", lineColumn);
        assertEquals("Line number should be correct", 1, lineColumn.getLine());
        assertEquals("Column number should be correct", pos, lineColumn.getColumn());
    }
    
    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        int lineNo = 1;
        assertNotNull("Line should not be null", fileText.get(lineNo));
    }
    
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals("Size should be correct", fileText.toLinesArray().length, fileText.size());
    }
}
