import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.UnsupportedCharsetException;

public class FileTextTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String TEST_FILE_PATH = "path/to/test/file";

    @Test
    public void testFileTextConstructorWithValidFile() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertEquals(file, fileText.getFile());
        assertEquals(CHARSET_NAME, fileText.getCharset().name());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithNonExistentFile() throws IOException {
        File file = new File("path/to/nonexistent/file");
        new FileText(file, CHARSET_NAME);
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() throws IOException {
        File file = new File(TEST_FILE_PATH);
        new FileText(file, "invalid-charset");
    }

    @Test
    public void testFileTextConstructorWithFileText() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText originalFileText = new FileText(file, CHARSET_NAME);
        FileText newFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), newFileText.getFile());
        assertEquals(originalFileText.getCharset(), newFileText.getCharset());
        assertEquals(originalFileText.getFullText(), newFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), newFileText.toLinesArray());
    }

    @Test
    public void testFileTextFromLines() throws IOException {
        File file = new File(TEST_FILE_PATH);
        List<String> lines = new ArrayList<String>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals("Line 1\nLine 2\n", fileText.getFullText());
        assertArrayEquals(new String[]{"Line 1", "Line 2"}, fileText.toLinesArray());
    }

    @Test
    public void testGetFile() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertEquals(CHARSET_NAME, fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertEquals(readFileToString(file, CHARSET_NAME), fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertArrayEquals(readFileToString(file, CHARSET_NAME).split("\n"), fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        LineColumn lineColumn = fileText.lineColumn(5);
        assertEquals(1, lineColumn.getLine());
        assertEquals(5, lineColumn.getColumn());
    }

    @Test
    public void testGet() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
    }

    @Test
    public void testSize() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, CHARSET_NAME);
        assertEquals(2, fileText.size());
    }

    private String readFileToString(File file, String charsetName) throws IOException {
        return "Line 1\nLine 2\n";
    }
}