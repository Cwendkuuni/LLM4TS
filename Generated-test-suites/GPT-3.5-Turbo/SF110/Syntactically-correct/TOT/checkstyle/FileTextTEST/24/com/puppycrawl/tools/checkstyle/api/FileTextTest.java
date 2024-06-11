
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileTextTest {

    private static FileText fileText;
    private static File file;
    private static List<String> lines;
    private static String charsetName;

    @BeforeClass
    public static void setUp() throws IOException {
        file = new File("testFile.txt");
        charsetName = "UTF-8";
        lines = Arrays.asList(
          "This is line 1",
          "This is line 2",
          "This is line 3"
        );
        
        fileText = new FileText(file, charsetName);
    }

    @Before
    public void clearData() {
        // Clear any previous data or changes made to the file or lines list
        // to ensure a consistent starting point for each test case
    }

    @Test
    public void testFileTextConstructor_FileAndCharsetName_Success() {
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", fileText.getFullText());
        assertArrayEquals(new String[]{"This is line 1", "This is line 2", "This is line 3"}, fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructor_FileAndUnsupportedCharsetName_ExceptionThrown() {
        String unsupportedCharsetName = "UTF-16";
        try {
            new FileText(file, unsupportedCharsetName);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: " + unsupportedCharsetName, e.getMessage());
        } catch (IOException e) {
            fail("Expected IllegalStateException to be thrown");
        }
    }

    @Test
    public void testFileTextConstructor_NonExistentFile_ExceptionThrown() {
        File nonExistentFile = new File("nonExistentFile.txt");
        try {
            new FileText(nonExistentFile, charsetName);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals(nonExistentFile.getPath() + " (No such file or directory)", e.getMessage());
        } catch (IOException e) {
            fail("Expected FileNotFoundException to be thrown");
        }
    }

    @Test
    public void testFileTextConstructor_FileText_Success() {
        FileText copiedFileText = new FileText(fileText);
        assertEquals(fileText.getFile(), copiedFileText.getFile());
        assertEquals(fileText.getCharset(), copiedFileText.getCharset());
        assertEquals(fileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructor_FileAndLines_Success() {
        FileText fileTextFromLines = FileText.fromLines(file, lines);
        assertEquals(file, fileTextFromLines.getFile());
        assertNull(fileTextFromLines.getCharset()); // Charset should be null when created from lines
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", fileTextFromLines.getFullText());
        assertArrayEquals(new String[]{"This is line 1", "This is line 2", "This is line 3"}, fileTextFromLines.toLinesArray());
    }

    @Test
    public void testGetFile_Success() {
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset_Success() {
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test
    public void testGetFullText_Success() {
        assertEquals("This is line 1\nThis is line 2\nThis is line 3\n", fileText.getFullText());
    }

    @Test
    public void testToLinesArray_Success() {
        assertArrayEquals(new String[]{"This is line 1", "This is line 2", "This is line 3"}, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn_PosWithinLine_Success() {
        FileText.LineColumn lineColumn = fileText.lineColumn(5);
        assertEquals(1, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }

    @Test
    public void testLineColumn_PosAtLineBreak_Success() {
        FileText.LineColumn lineColumn = fileText.lineColumn(19);
        assertEquals(2, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
    }

    @Test
    public void testLineColumn_PosPastEndOfLine_Success() {
        FileText.LineColumn lineColumn = fileText.lineColumn(25);
        assertEquals(2, lineColumn.getLine());
        assertEquals(6, lineColumn.getColumn());
    }

    @Test
    public void testLineColumn_PosBeforeStartOfLine_Success() {
        FileText.LineColumn lineColumn = fileText.lineColumn(0);
        assertEquals(1, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
    }

    @Test
    public void testGet_LineNoWithinBounds_Success() {
        assertEquals("This is line 2", fileText.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_LineNoOutOfBounds_ExceptionThrown() {
        fileText.get(3);
    }

    @Test
    public void testSize_Success() {
        assertEquals(3, fileText.size());
    }
}

