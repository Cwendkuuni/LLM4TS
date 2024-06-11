
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    // Test case for FileText(File, String) constructor
    @Test
    public void testFileTextConstructor() throws IOException {
        // Typical use case - file and valid charset
        File file1 = new File("testFile1.txt");
        FileText fileText1 = new FileText(file1, "UTF-8");
        assertNotNull(fileText1);
        assertEquals(file1, fileText1.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText1.getCharset());
        
        // Typical use case - file and unsupported charset
        File file2 = new File("testFile2.txt");
        try {
            FileText fileText2 = new FileText(file2, "invalidCharset");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: invalidCharset", e.getMessage());
        }
        
        // Edge case - non-existing file
        File file3 = new File("nonExistingFile.txt");
        try {
            FileText fileText3 = new FileText(file3, "UTF-8");
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals("nonExistingFile.txt (No such file or directory)", e.getMessage());
        }
    }
    
    // Test case for FileText(FileText) constructor
    @Test
    public void testFileTextCopyConstructor() {
        // Typical use case - clone FileText
        File file = new File("testFile.txt");
        FileText fileText1 = new FileText(file, "UTF-8");
        FileText fileText2 = new FileText(fileText1);
        assertNotNull(fileText2);
        assertEquals(file, fileText2.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText2.getCharset());
        assertEquals(fileText1.getFullText(), fileText2.getFullText());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
        assertNotSame(fileText1.toLinesArray(), fileText2.toLinesArray());
        
        // Edge case - null lineBreaks array
        fileText1 = new FileText(file, "UTF-8");
        fileText1.lineColumn(1);
        fileText1.lineColumn(2);
        fileText1.lineColumn(3);
        assertNull(fileText1.lineBreaks);
        fileText2 = new FileText(fileText1);
        assertNotNull(fileText2);
        assertNull(fileText2.lineBreaks);
    }
    
    // Test case for FileText(File, List<String>) constructor
    @Test
    public void testFromLines() {
        // Typical use case - create FileText from a list of lines
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
        assertNotSame(lines.toArray(), fileText.toLinesArray());
        
        // Edge case - create FileText from an existing FileText object
        FileText fileText1 = new FileText(file, lines);
        FileText fileText2 = FileText.fromLines(file, fileText1);
        assertSame(fileText1, fileText2);
    }
    
    // Test case for getFile() method
    @Test
    public void testGetFile() {
        // Typical use case - get file from FileText object
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }
    
    // Test case for getCharset() method
    @Test
    public void testGetCharset() {
        // Typical use case - get charset from FileText object
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }
    
    // Test case for getFullText() method
    @Test
    public void testGetFullText() {
        // Typical use case - get full text from FileText object
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("", fileText.getFullText().toString());
        
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        fileText = FileText.fromLines(file, lines);
        assertEquals("line1\nline2\nline3\n", fileText.getFullText().toString());
    }
    
    // Test case for toLinesArray() method
    @Test
    public void testToLinesArray() {
        // Typical use case - convert FileText to String array of lines
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }
    
    // Test case for lineColumn(int) method
    @Test
    public void testLineColumn() {
        // Typical use case - get line and column for a given position
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        
        // Edge case - position at the beginning of the file
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        
        // Typical use case - position in the middle of the file
        assertEquals(new LineColumn(2, 2), fileText.lineColumn(7));
        
        // Typical use case - position at the end of the file
        assertEquals(new LineColumn(3, 4), fileText.lineColumn(13));
        
        // Error scenario - position beyond the end of the file
        try {
            fileText.lineColumn(100);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("100", e.getMessage());
        }
    }
    
    // Test case for get(int) method
    @Test
    public void testGet() {
        // Typical use case - get line at a given index
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
        assertEquals("line3", fileText.get(2));
    }
    
    // Test case for size() method
    @Test
    public void testSize() {
        // Typical use case - get number of lines in FileText object
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(file, lines);
        
        assertEquals(3, fileText.size());
    }
}
