

import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.*;
import static org.junit.Assert.*;

public class FileTextTest {

    private static File testFile;
    private static FileText fileText;

    @BeforeClass
    public static void setUp() throws Exception {
        testFile = new File("test.txt");
        fileText = new FileText(testFile, "UTF-8");
    }
    
    @AfterClass
    public static void tearDown() throws Exception {
        // Clean up any resources
        testFile.delete();
    }

    @Test
    public void testConstructorFileAndCharsetName_validFileAndCharset() throws Exception {
        assertEquals("UTF-8", fileText.getCharset().name());
        assertEquals("test.txt", fileText.getFile().getName());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(0, fileText.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorFileAndCharsetName_invalidCharset() throws Exception {
        FileText invalidCharsetText = new FileText(testFile, "invalidCharset");
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorFileAndCharsetName_invalidFile() throws Exception {
        File invalidFile = new File("invalid.txt");
        FileText invalidFileText = new FileText(invalidFile, "UTF-8");
    }

    @Test
    public void testConstructorFileText_validFileText() throws Exception {
        FileText newFileText = new FileText(fileText);
        assertEquals("UTF-8", newFileText.getCharset().name());
        assertEquals("test.txt", newFileText.getFile().getName());
        assertEquals(fileText.getFullText(), newFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), newFileText.toLinesArray());
    }

    @Test
    public void testFromLines_validFileAndLines() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText newText = FileText.fromLines(testFile, lines);
        assertEquals("UTF-8", newText.getCharset().name());
        assertEquals("test.txt", newText.getFile().getName());
        assertEquals("line 1\nline 2\nline 3\n", newText.getFullText());
        assertArrayEquals(lines.toArray(), newText.toLinesArray());
    }

    @Test
    public void testLineColumn_validPositionInText() {
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }

    @Test
    public void testLineColumn_invalidPositionInText() {
        LineColumn lineColumn = fileText.lineColumn(1000);
        assertEquals(0, lineColumn.getLine());
        assertEquals(-1, lineColumn.getColumn());
    }

    @Test
    public void testGet_validLineIndex() {
        fileText = new FileText(testFile, Arrays.asList("line 1", "line 2", "line 3"));
        assertEquals("line 2", fileText.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_invalidLineIndex() {
        fileText.get(10);
    }

    @Test
    public void testSize_emptyFile() {
        fileText = new FileText(testFile, new ArrayList<String>());
        assertEquals(0, fileText.size());
    }

    @Test
    public void testSize_nonEmptyFile() {
        fileText = new FileText(testFile, Arrays.asList("line 1", "line 2", "line 3"));
        assertEquals(3, fileText.size());
    }

}

