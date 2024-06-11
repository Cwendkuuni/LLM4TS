import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText);
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
            assertNotNull(fileText.getFullText());
            assertEquals(0, fileText.size());
        } catch(IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText copiedFileText = new FileText(originalFileText);
            
            assertNotNull(copiedFileText);
            assertEquals(originalFileText.getFile(), copiedFileText.getFile());
            assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
            assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        } catch(IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testFileTextFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText().toString());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);
        
        LineColumn lc = fileText.lineColumn(8);
        
        assertEquals(2, lc.line);
        assertEquals(3, lc.column);
    }
    
    // Add more test methods for other public methods
    
    @Test(expected = FileNotFoundException.class)
    public void testReadFileFileNotFound() throws IOException {
        File file = new File("non_existent_file.txt");
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        
        FileText.readFile(file, decoder);
    }
    
    // Add more test methods for edge cases and exception handling scenarios
    
}