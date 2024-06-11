import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;
import java.nio.charset.*;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() {
        try {
            File file = new File("test.txt");
            String charsetName = "UTF-8";
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithFileText() {
        try {
            File file = new File("test.txt");
            String charsetName = "UTF-8";
            FileText originalFileText = new FileText(file, charsetName);
            FileText fileText = new FileText(originalFileText);
            
            assertEquals(originalFileText.getFile(), fileText.getFile());
            assertEquals(originalFileText.getCharset(), fileText.getCharset());
            assertEquals(originalFileText.getFullText(), fileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), fileText.toLinesArray());
            
            // The lineBreaks array should be null if the original FileText's lineBreaks is null
            if (originalFileText.lineBreaks == null) {
                assertNull(fileText.lineBreaks);
            } else {
                assertArrayEquals(originalFileText.lineBreaks, fileText.lineBreaks);
            }
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithFileAndLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
        assertArrayEquals(new String[] {"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
    }
    
    @Test
    public void testReadFile() {
        try {
            File file = new File("test.txt");
            String charsetName = "UTF-8";
            CharsetDecoder decoder = Charset.forName(charsetName).newDecoder();
            
            String expectedText = "Line 1\nLine 2\nLine 3\n";
            
            String actualText = FileText.readFile(file, decoder);
            
            assertEquals(expectedText, actualText);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        
        FileText originalFileText = new FileText(file, lines);
        
        FileText fileText = FileText.fromLines(file, lines);
        
        assertSame(originalFileText, fileText);
    }
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals(file, fileText.getFile());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertArrayEquals(new String[] {"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            int[] expectedLineBreaks = {0, 7, 14, 15};
            
            assertArrayEquals(expectedLineBreaks, fileText.findLineBreaks());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            int pos = 7;
            
            LineColumn lineColumn = fileText.lineColumn(pos);
            
            assertEquals(1, lineColumn.line);
            assertEquals(7, lineColumn.column);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals("Line 2", fileText.get(1));
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            
            assertEquals(3, fileText.size());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}