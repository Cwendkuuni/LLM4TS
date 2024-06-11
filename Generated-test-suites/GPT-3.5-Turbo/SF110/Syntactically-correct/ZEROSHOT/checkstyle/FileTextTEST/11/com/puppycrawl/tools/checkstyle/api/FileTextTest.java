import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testConstructor_withValidFileAndCharsetName_shouldCreateFileTextObject() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructor_withInvalidCharsetName_shouldThrowException() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "InvalidCharset";
        
        FileText fileText = new FileText(file, charsetName);
    }
    
    @Test
    public void testConstructor_withFileTextObject_shouldCreateCopyOfObject() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        FileText fileText = new FileText(originalFileText);
        
        assertNotNull(fileText);
        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), fileText.toLinesArray());
    }
    
    @Test
    public void testFromFileLines_withFileAndListOfLines_shouldCreateFileTextObject() {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Line 1\nLine 2\n", fileText.getFullText());
        assertArrayEquals(new String[] {"Line 1", "Line 2"}, fileText.toLinesArray());
    }
    
    @Test
    public void testGetFile_shouldReturnFile() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        File returnedFile = fileText.getFile();
        
        assertEquals(file, returnedFile);
    }
    
    @Test
    public void testGetCharset_shouldReturnCharset() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        Charset charset = fileText.getCharset();
        
        assertEquals(Charset.forName(charsetName), charset);
    }
    
    @Test
    public void testGetFullText_shouldReturnFullTextOfFile() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        CharSequence fullText = fileText.getFullText();
        
        assertEquals("Line 1\nLine 2\n", fullText);
    }
    
    @Test
    public void testToLinesArray_shouldReturnArrayOfLines() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String[] linesArray = fileText.toLinesArray();
        
        assertArrayEquals(new String[] {"Line 1", "Line 2"}, linesArray);
    }
    
    @Test
    public void testLineColumn_withValidPosition_shouldReturnLineColumnObject() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        LineColumn lineColumn = fileText.lineColumn(7);
        
        assertNotNull(lineColumn);
        assertEquals(2, lineColumn.getLineNo());
        assertEquals(6, lineColumn.getColumnNo());
    }
    
    @Test
    public void testGet_withValidLineNo_shouldReturnLineString() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String line = fileText.get(1);
        
        assertEquals("Line 2", line);
    }
    
    @Test
    public void testSize_shouldReturnNumberOfLines() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        int size = fileText.size();
        
        assertEquals(2, size);
    }
    
}