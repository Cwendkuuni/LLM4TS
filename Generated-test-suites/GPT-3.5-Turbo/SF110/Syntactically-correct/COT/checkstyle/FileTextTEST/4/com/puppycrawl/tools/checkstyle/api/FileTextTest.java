
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileTextTest {

    // Test constructor with valid file and charset name
    @Test
    public void testConstructor_validFileAndCharsetName() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        try {
            FileText fileText = new FileText(file, charsetName);
            assertTrue(fileText instanceof FileText);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test constructor with invalid file path
    @Test(expected = FileNotFoundException.class)
    public void testConstructor_invalidFilePath() throws IOException {
        File file = new File("invalid.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
    }

    // Test constructor with unsupported charset name
    @Test(expected = IllegalStateException.class)
    public void testConstructor_unsupportedCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "InvalidCharset";
        
        FileText fileText = new FileText(file, charsetName);
    }

    // Test constructor with null charset name
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = null;
        
        FileText fileText = new FileText(file, charsetName);
    }

    // Test constructor with valid FileText object
    @Test
    public void testConstructor_validFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText fileText = new FileText(originalFileText);
            assertTrue(fileText instanceof FileText);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test getFile method
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test getCharset method
    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test getFullText method
    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals("This is a test file.", fileText.getFullText());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test toLinesArray method
    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertArrayEquals(new String[]{"This is a test file."}, fileText.toLinesArray());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test lineColumn method with position at beginning of line
    @Test
    public void testLineColumn_positionAtBeginningOfLine() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            LineColumn lineColumn = fileText.lineColumn(0);
            assertEquals(1, lineColumn.getLine());
            assertEquals(0, lineColumn.getColumn());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test lineColumn method with position at middle of line
    @Test
    public void testLineColumn_positionAtMiddleOfLine() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            LineColumn lineColumn = fileText.lineColumn(5);
            assertEquals(1, lineColumn.getLine());
            assertEquals(5, lineColumn.getColumn());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test lineColumn method with position at end of line
    @Test
    public void testLineColumn_positionAtEndOfLine() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            LineColumn lineColumn = fileText.lineColumn(18);
            assertEquals(1, lineColumn.getLine());
            assertEquals(18, lineColumn.getColumn());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test lineColumn method with position at middle of text
    @Test
    public void testLineColumn_positionAtMiddleOfText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            LineColumn lineColumn = fileText.lineColumn(10);
            assertEquals(1, lineColumn.getLine());
            assertEquals(10, lineColumn.getColumn());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test get method with valid line number
    @Test
    public void testGet_validLineNumber() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals("This is a test file.", fileText.get(0));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test get method with invalid line number
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_invalidLineNumber() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        
        FileText fileText = new FileText(file, charsetName);
        fileText.get(1);
    }

    // Test size method
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(1, fileText.size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}

