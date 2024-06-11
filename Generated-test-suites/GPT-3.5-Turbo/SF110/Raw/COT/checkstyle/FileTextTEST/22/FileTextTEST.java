import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.io.IOException;

public class FileTextTest {
    
    // Test public constructor FileText(File, String) throws IOException
    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(0, fileText.lineColumn(0).getLineNo());
        assertEquals(0, fileText.lineColumn(0).getColumnNo());
    }
    
    // Test public constructor FileText(FileText)
    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        FileText copiedFileText = new FileText(originalFileText);
        
        assertNotSame(originalFileText, copiedFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset().name(), copiedFileText.getCharset().name());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        assertSame(originalFileText.lineBreaks, copiedFileText.lineBreaks);
    }
    
    // Test public static method fromLines(File, List<String>)
    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("Hello", "World"));
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(lines.get(0), fileText.get(0));
        assertEquals(lines.get(1), fileText.get(1));
        assertEquals(lines.size(), fileText.size());
    }
    
    // Test public method getFile()
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(file, fileText.getFile());
    }
    
    // Test public method getCharset()
    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(charsetName, fileText.getCharset().name());
    }
    
    // Test public method getFullText()
    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText.getFullText());
    }
    
    // Test public method toLinesArray()
    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertNotNull(fileText.toLinesArray());
        assertArrayEquals(fileText.toLinesArray(), fileText.getFullText().split("\n"));
    }
    
    // Test private method findLineBreaks()
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals(fileText.getFullText().split("\n").length + 1, lineBreaks.length);
    }
    
    // Test public method lineColumn(int)
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(1, fileText.lineColumn(0).getLineNo());
        assertEquals(0, fileText.lineColumn(0).getColumnNo());
        
        int[] lineBreaks = fileText.findLineBreaks();
        int position = lineBreaks[lineBreaks.length - 2] + 5;
        assertEquals(lineBreaks.length - 1, fileText.lineColumn(position).getLineNo());
        assertEquals(5, fileText.lineColumn(position).getColumnNo());
    }
    
    // Test overridden method get(int)
    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(fileText.getFullText().split("\n")[0], fileText.get(0));
        assertEquals(fileText.getFullText().split("\n")[1], fileText.get(1));
    }
    
    // Test overridden method size()
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        assertEquals(fileText.getFullText().split("\n").length, fileText.size());
    }
}