import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FileTextTest {
    
    // Test case for constructor FileText(File file, String charsetName)
    @Test
    public void testConstructor() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }
    
    // Test case for constructor FileText(FileText fileText)
    @Test
    public void testCopyConstructor() {
        File file = new File("test.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        
        assertNotNull(copiedFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    // Test case for static method fromLines(File file, List<String> lines)
    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        File file = new File("test.txt");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }
    
    // Test case for method lineColumn(int pos)
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        LineColumn lineColumn = fileText.lineColumn(15);
        
        assertNotNull(lineColumn);
        assertEquals(2, lineColumn.getLine());
        assertEquals(7, lineColumn.getColumn());
    }
    
    // Test case for overridden method get(int lineNo)
    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String[] linesArray = fileText.toLinesArray();
        
        for (int i = 0; i < linesArray.length; i++) {
            assertEquals(linesArray[i], fileText.get(i));
        }
    }
    
    // Test case for overridden method size()
    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String[] linesArray = fileText.toLinesArray();
        
        assertEquals(linesArray.length, fileText.size());
    }
    
    // Test case for readFile(File inputFile, CharsetDecoder decoder) throws IOException
    @Test(expected = FileNotFoundException.class)
    public void testReadFile_FileNotFound() throws IOException {
        File file = new File("missing_file.txt");
        FileText.readFile(file, Charset.forName("UTF-8").newDecoder());
    }
    
    // Test case for findLineBreaks()
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        
        int[] lineBreaks = fileText.findLineBreaks();
        
        assertNotNull(lineBreaks);
        assertEquals(fileText.size() + 1, lineBreaks.length);
    }
}