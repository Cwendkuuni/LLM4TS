import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTextTest {

    private static final String TEST_FILE_PATH = "path/to/test_file.txt";

    @Test
    public void testConstructor() throws IOException {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
        assertEquals(new File(TEST_FILE_PATH), fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertTrue(fileText.size() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorUnsupportedCharset() throws IOException {
        new FileText(new File(TEST_FILE_PATH), "InvalidCharset");
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorNonExistentFile() throws IOException {
        new FileText(new File("non_existent_file.txt"), "UTF-8");
    }

    @Test
    public void testConstructorFileText() {
        FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
        FileText copy = new FileText(fileText);
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
    }

    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File(TEST_FILE_PATH), lines);
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File(TEST_FILE_PATH), lines);

        LineColumn lineColumn1 = fileText.lineColumn(0);
        assertEquals(1, lineColumn1.lineNo);
        assertEquals(0, lineColumn1.columnNo);

        LineColumn lineColumn2 = fileText.lineColumn(6);
        assertEquals(2, lineColumn2.lineNo);
        assertEquals(0, lineColumn2.columnNo);

        LineColumn lineColumn3 = fileText.lineColumn(13);
        assertEquals(3, lineColumn3.lineNo);
        assertEquals(0, lineColumn3.columnNo);
    }

    @Test
    public void testGet() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File(TEST_FILE_PATH), lines);

        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File(TEST_FILE_PATH), lines);

        assertEquals(3, fileText.size());
    }
}