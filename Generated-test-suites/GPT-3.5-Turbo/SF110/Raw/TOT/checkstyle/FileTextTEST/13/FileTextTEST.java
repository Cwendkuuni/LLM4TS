
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    private static FileText fileText;
    private static File testFile;
    private static List<String> testLines;

    @BeforeClass
    public static void setup() throws IOException {
        testFile = new File("testFile.txt");
        testLines = Arrays.asList("Line 1", "Line 2", "Line 3");
        fileText = FileText.fromLines(testFile, testLines);
    }

    @AfterClass
    public static void cleanup() {
        testFile.delete();
    }

    // Test case for constructor with file and charsetName parameters
    @Test
    public void testConstructor_File_CharsetName() throws IOException {
        FileText fileText = new FileText(testFile, "UTF-8");

        assertEquals(testFile, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
        assertArrayEquals(new String[] {"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
    }

    // Test case for constructor with FileText parameter
    @Test
    public void testConstructor_FileText() {
        FileText fileTextCopy = new FileText(fileText);

        assertEquals(testFile, fileTextCopy.getFile());
        assertEquals(fileText.getCharset(), fileTextCopy.getCharset());
        assertEquals(fileText.getFullText(), fileTextCopy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), fileTextCopy.toLinesArray());
    }

    // Test case for fromLines() method
    @Test
    public void testFromLines_File_List() {
        FileText fileTextFromLines = FileText.fromLines(testFile, testLines);

        assertNotSame(testLines, fileTextFromLines);
        assertEquals(testFile, fileTextFromLines.getFile());
        assertNull(fileTextFromLines.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileTextFromLines.getFullText());
        assertArrayEquals(new String[] {"Line 1", "Line 2", "Line 3"}, fileTextFromLines.toLinesArray());
    }

    // Test case for getFile() method
    @Test
    public void testGetFile() {
        assertEquals(testFile, fileText.getFile());
    }

    // Test case for getCharset() method
    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }

    // Test case for getFullText() method
    @Test
    public void testGetFullText() {
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText());
    }

    // Test case for toLinesArray() method
    @Test
    public void testToLinesArray() {
        assertArrayEquals(new String[] {"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
    }

    // Test case for lineColumn() method
    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(7);
        assertEquals(1, lineColumn.getLine());
        assertEquals(6, lineColumn.getColumn());

        lineColumn = fileText.lineColumn(13);
        assertEquals(2, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());

        lineColumn = fileText.lineColumn(21);
        assertEquals(3, lineColumn.getLine());
        assertEquals(4, lineColumn.getColumn());
    }

    // Test case for get() method
    @Test
    public void testGet() {
        assertEquals("Line 2", fileText.get(1));
    }

    // Test case for size() method
    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }

    // Test case for read file function without charset decoder
    @Test(expected = FileNotFoundException.class)
    public void testReadFile_FileNotFoundException() throws IOException {
        FileText.readFile(new File("nonExistingFile.txt"), null);
    }

    // Test case for read file function with charset decoder
    @Test
    public void testReadFile_WithCharsetDecoder() throws IOException {
        FileText.readFile(new File("existingFile.txt"), Charset.forName("UTF-8").newDecoder());
        // Additional assertions to verify the content of the file can be added here
    }

    // Test case for LINE_TERMINATOR pattern
    @Test
    public void testLINE_TERMINATOR() {
        Pattern pattern = Pattern.compile("\\n|\\r\\n?");
        Matcher matcher = pattern.matcher("Line 1\nLine 2\r\nLine 3");
        assertTrue(matcher.find());
        assertTrue(matcher.find());
        assertTrue(matcher.find());
        assertFalse(matcher.find());
    }

}

