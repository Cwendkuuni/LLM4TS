import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.charset.*;
import java.io.IOException;
import java.util.regex.*;

public class FileTextTest {
    
    private File file;
    private List<String> lines;
    private String charsetName;
    private FileText fileText;
    
    @Before
    public void setUp() {
        file = new File("test.txt");
        lines = Arrays.asList("line 1", "line 2", "line 3");
        charsetName = "UTF-8";
        fileText = null;
    }
    
    @After
    public void tearDown() {
        file.delete();
    }
    
    @Test
    public void testFileTextConstructor() {
        try {
            fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
            assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFileTextConstructor_FileNotFound() {
        file.delete();
        try {
            fileText = new FileText(file, charsetName);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals(file.getPath() + " (No such file or directory)", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testFileTextConstructor_UnsupportedCharset() {
        charsetName = "INVALID_CHARSET";
        try {
            fileText = new FileText(file, charsetName);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: " + charsetName, e.getMessage());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testFileTextCopyConstructor() {
        FileText originalFileText = new FileText(file, charsetName);
        fileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), fileText.getFile());
        assertEquals(originalFileText.getCharset(), fileText.getCharset());
        assertEquals(originalFileText.getFullText(), fileText.getFullText());
        assertNotSame(originalFileText.getLinesArray(), fileText.getLinesArray());
    }
    
    @Test
    public void testFileTextFromLines() {
        fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("line 1\nline 2\nline 3\n", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.getLinesArray());
    }
    
    @Test
    public void testFileTextFromLines_FileTextInstance() {
        FileText inputText = new FileText(file, charsetName);
        fileText = FileText.fromLines(file, inputText);
        assertSame(inputText, fileText);
    }
    
    @Test
    public void testGetFile() {
        FileText text = new FileText(file, charsetName);
        assertEquals(file, text.getFile());
    }
    
    @Test
    public void testGetCharset() {
        FileText text = new FileText(file, charsetName);
        assertEquals(Charset.forName(charsetName), text.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        FileText text = new FileText(file, charsetName);
        assertEquals("line 1\nline 2\nline 3\n", text.getFullText());
    }
    
    @Test
    public void testToLinesArray() {
        FileText text = new FileText(file, charsetName);
        assertArrayEquals(lines.toArray(new String[0]), text.toLinesArray());
    }
    
    @Test
    public void testFindLineBreaks() {
        FileText text = new FileText(file, charsetName);
        int[] lineBreaks = {0, 6, 12, 18};
        assertArrayEquals(lineBreaks, text.findLineBreaks());
    }
    
    @Test
    public void testLineColumn() {
        FileText text = new FileText(file, charsetName);
        assertEquals(new LineColumn(1, 0), text.lineColumn(0));
        assertEquals(new LineColumn(2, 2), text.lineColumn(7));
        assertEquals(new LineColumn(3, 3), text.lineColumn(12));
        assertEquals(new LineColumn(3, 10), text.lineColumn(19));
    }
    
    @Test
    public void testGet() {
        FileText text = new FileText(file, charsetName);
        assertEquals("line 1", text.get(0));
        assertEquals("line 2", text.get(1));
        assertEquals("line 3", text.get(2));
    }
    
    @Test
    public void testSize() {
        FileText text = new FileText(file, charsetName);
        assertEquals(lines.size(), text.size());
    }

}