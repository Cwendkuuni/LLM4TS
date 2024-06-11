
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class FileTextTest {
    
    @Test
    public void testConstructorWithFileAndCharsetName() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        // Act
        try {
            FileText fileText = new FileText(file, charsetName);
            
            // Assert
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithFileText() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText;
        try {
            originalFileText = new FileText(file, charsetName);
            
            // Act
            FileText newFileText = new FileText(originalFileText);
            
            // Assert
            assertEquals(file, newFileText.getFile());
            assertEquals(originalFileText.getCharset(), newFileText.getCharset());
            assertEquals(originalFileText.toLinesArray(), newFileText.toLinesArray());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFromLines() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");

        // Act
        FileText fileText = FileText.fromLines(file, lines);
        
        // Assert
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }
    
    @Test
    public void testGetFullText() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText;
        try {
            fileText = new FileText(file, charsetName);
            
            // Act
            CharSequence fullText = fileText.getFullText();
            
            // Assert
            assertNotNull(fullText);
            assertEquals(fileText.toLinesArray(), fullText.toString().split("\\r?\\n"));
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLineColumn() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText;
        try {
            fileText = new FileText(file, charsetName);
            int pos = 15;
            
            // Act
            LineColumn lineColumn = fileText.lineColumn(pos);
            
            // Assert
            assertNotNull(lineColumn);
            assertEquals(2, lineColumn.getLine());
            assertEquals(1, lineColumn.getColumn());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText;
        try {
            fileText = new FileText(file, charsetName);
            int lineNo = 2;
            
            // Act
            String line = fileText.get(lineNo);
            
            // Assert
            assertEquals("Line 2", line);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText;
        try {
            fileText = new FileText(file, charsetName);
            
            // Act
            int size = fileText.size();
            
            // Assert
            assertEquals(3, size);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
