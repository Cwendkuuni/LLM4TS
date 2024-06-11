import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testConstructor() throws IOException {
        // Create test file and write some content
        File testFile = new File("test.txt");
        FileWriter writer = new FileWriter(testFile);
        writer.write("Hello\nWorld");
        writer.close();
        
        // Create a FileText object with the test file
        FileText fileText = new FileText(testFile, "UTF-8");
        
        // Verify that the file, charset, fullText, and lines are correctly initialized
        assertEquals(testFile, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals("Hello\nWorld", fileText.getFullText().toString());
        assertArrayEquals(new String[] {"Hello", "World"}, fileText.toLinesArray());
        
        // Delete the test file
        testFile.delete();
    }

    @Test
    public void testCopyConstructor() throws IOException {
        // Create test file and write some content
        File testFile = new File("test.txt");
        FileWriter writer = new FileWriter(testFile);
        writer.write("Hello\nWorld");
        writer.close();
        
        // Create a FileText object with the test file
        FileText fileText = new FileText(testFile, "UTF-8");
        
        // Create a FileText object using the copy constructor
        FileText copy = new FileText(fileText);
        
        // Verify that the copied object has the same file, charset, fullText, and lines as the original
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText().toString(), copy.getFullText().toString());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
        
        // Delete the test file
        testFile.delete();
    }

    @Test
    public void testFromStringLines() {
        // Create a list of string lines
        List<String> lines = Arrays.asList("Hello", "World");
        
        // Create a FileText object from the string lines
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);
        
        // Verify that the file, charset, fullText, and lines are correctly initialized
        assertNull(fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Hello\nWorld", fileText.getFullText().toString());
        assertArrayEquals(new String[] {"Hello", "World"}, fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        // Create a File object
        File file = new File("test.txt");
        
        // Create a FileText object with the file
        FileText fileText = new FileText(file, "UTF-8");
        
        // Verify that the getFile method returns the correct file
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        // Create a File object
        File file = new File("test.txt");
        
        // Create a FileText object with the file and charset
        FileText fileText = new FileText(file, "UTF-8");
        
        // Verify that the getCharset method returns the correct charset
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testGetFullText() throws IOException {
        // Create test file and write some content
        File testFile = new File("test.txt");
        FileWriter writer = new FileWriter(testFile);
        writer.write("Hello\nWorld");
        writer.close();
        
        // Create a FileText object with the test file
        FileText fileText = new FileText(testFile, "UTF-8");
        
        // Verify that the getFullText method returns the correct full text content
        assertEquals("Hello\nWorld", fileText.getFullText().toString());
        
        // Delete the test file
        testFile.delete();
    }

    @Test
    public void testToLinesArray() {
        // Create a File object
        File file = new File("test.txt");
        
        // Create a FileText object with the file
        FileText fileText = new FileText(file, "UTF-8");
        
        // Verify that the toLinesArray method returns the correct array of lines
        assertArrayEquals(new String[] {"Hello", "World"}, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        // Create a File object
        File file = new File("test.txt");
        
        // Create a FileText object with the file
        FileText fileText = new FileText(file, "UTF-8");
        
        // Verify that the lineColumn method returns the correct line and column numbers
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new LineColumn(2, 0), fileText.lineColumn(6));
        assertEquals(new LineColumn(2, 5), fileText.lineColumn(11));
    }

    @Test
    public void testGet() {
        // Create a File object
        File file = new File("test.txt");
        
        // Create a FileText object with the file
        FileText fileText = new FileText(file, "UTF-8");
        
        // Verify that the get method returns the correct line
        assertEquals("Hello", fileText.get(0));
        assertEquals("World", fileText.get(1));
    }

    @Test
    public void testSize() {
        // Create a File object
        File file = new File("test.txt");
        
        // Create a FileText object with the file
        FileText fileText = new FileText(file, "UTF-8");
        
        // Verify that the size method returns the correct number of lines
        assertEquals(2, fileText.size());
    }
}