import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);
    }

    @Test
    public void testConstructorThrowsExceptionForUnsupportedCharset() {
        try {
            File file = new File("testfile.txt");
            String charsetName = "invalidCharset";
            new FileText(file, charsetName);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Success
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testConstructorThrowsExceptionForNonExistentFile() {
        try {
            File file = new File("nonexistentfile.txt");
            String charsetName = "UTF-8";
            new FileText(file, charsetName);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            // Success
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("testfile.txt");
        File actualFile = fileText.getFile();
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testGetCharset() {
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String expectedFullText = "This is a test file.";
        String actualFullText = fileText.getFullText().toString();
        assertEquals(expectedFullText, actualFullText);
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"This is a test file."};
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testLineColumn() {
        int pos = 10;
        LineColumn expectedLineColumn = new LineColumn(1, 10);
        LineColumn actualLineColumn = fileText.lineColumn(pos);
        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test
    public void testGet() {
        int lineNo = 1;
        String expectedLine = "This is a test file.";
        String actualLine = fileText.get(lineNo);
        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testSize() {
        int expectedSize = 1;
        int actualSize = fileText.size();
        assertEquals(expectedSize, actualSize);
    }
}