import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.*;
import com.google.common.io.*;

public class FileTextTest {
    
    @Test
    public void testConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        // Test case: Create FileText object with valid file and charset name
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
        
        // Test case: Create FileText object with non-existent file
        File nonExistentFile = new File("test2.txt");
        try {
            FileText fileText2 = new FileText(nonExistentFile, charsetName);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals(nonExistentFile.getPath() + " (No such file or directory)", e.getMessage());
        }
        
        // Test case: Create FileText object with unsupported charset name
        String unsupportedCharsetName = "invalidCharset";
        try {
            FileText fileText3 = new FileText(file, unsupportedCharsetName);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: " + unsupportedCharsetName, e.getMessage());
        }
    }

    @Test
    public void testConstructorCopy() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        
        // Test case: Create FileText object as a copy of an existing FileText object
        FileText copyFileText = new FileText(originalFileText);
        assertNotNull(copyFileText);
        assertSame(originalFileText.getFile(), copyFileText.getFile());
        assertSame(originalFileText.getCharset(), copyFileText.getCharset());
        assertSame(originalFileText.getFullText(), copyFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
    }
    
    @Test
    public void testFromFileLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        
        // Test case: Create FileText object from lines
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText());
        assertArrayEquals(new String[] {"line1", "line2", "line3"}, fileText.toLinesArray());
        
        // Test case: Create FileText object from existing FileText object
        FileText copyFileText = FileText.fromLines(file, fileText);
        assertSame(fileText, copyFileText);
    }
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Test case: Get file
        assertEquals(file, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Test case: Get charset
        Charset expectedCharset = Charset.forName(charsetName);
        assertEquals(expectedCharset, fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Test case: Get full text
        assertEquals(fileText.toLinesArray()[0] + "\n" + fileText.toLinesArray()[1] + "\n" +
                     fileText.toLinesArray()[2] + "\n", fileText.getFullText().toString());
    }
    
    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Test case: Get lines array
        assertArrayEquals(fileText.toLinesArray(), fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int position = 5;
        
        // Test case: Get line and column from position
        LineColumn expectedLineColumn = new LineColumn(1, 5);
        assertEquals(expectedLineColumn, fileText.lineColumn(position));
    }
    
    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int lineNo = 1;
        
        // Test case: Get line by line number
        String expectedLine = fileText.toLinesArray()[lineNo];
        assertEquals(expectedLine, fileText.get(lineNo));
    }
    
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        // Test case: Get number of lines
        int expectedSize = fileText.toLinesArray().length;
        assertEquals(expectedSize, fileText.size());
    }
}