import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() {
        try {
            File file = new File("test.txt");
            FileText fileText = new FileText(file, "UTF-8");

            // Check if the file and charset are set correctly
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName("UTF-8"), fileText.getCharset());

            // Check if the full text is read correctly
            String fullText = "This is line 1\nThis is line 2\nThis is line 3";
            assertEquals(fullText, fileText.getFullText().toString());

            // Check if the lines array is populated correctly
            String[] lines = {"This is line 1", "This is line 2", "This is line 3"};
            assertArrayEquals(lines, fileText.toLinesArray());
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithFileText() {
        try {
            File file = new File("test.txt");
            List<String> lines = Arrays.asList("This is line 1", "This is line 2", "This is line 3");
            FileText originalFileText = FileText.fromLines(file, lines);

            FileText copiedFileText = new FileText(originalFileText);

            // Check if the copied file text has the same properties as the original
            assertEquals(originalFileText.getFile(), copiedFileText.getFile());
            assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
            assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLineColumn() {
        try {
            File file = new File("test.txt");
            List<String> lines = Arrays.asList("This is line 1", "This is line 2", "This is line 3");
            FileText fileText = FileText.fromLines(file, lines);

            // Check line and column for various positions
            assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
            assertEquals(new LineColumn(1, 2), fileText.lineColumn(2));
            assertEquals(new LineColumn(2, 0), fileText.lineColumn(15));
            assertEquals(new LineColumn(2, 5), fileText.lineColumn(20));
            assertEquals(new LineColumn(3, 0), fileText.lineColumn(34));
            assertEquals(new LineColumn(3, 5), fileText.lineColumn(39));

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGet() {
        try {
            File file = new File("test.txt");
            List<String> lines = Arrays.asList("This is line 1", "This is line 2", "This is line 3");
            FileText fileText = FileText.fromLines(file, lines);

            // Check if get() returns correct line string
            assertEquals("This is line 1", fileText.get(0));
            assertEquals("This is line 2", fileText.get(1));
            assertEquals("This is line 3", fileText.get(2));

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSize() {
        try {
            File file = new File("test.txt");
            List<String> lines = Arrays.asList("This is line 1", "This is line 2", "This is line 3");
            FileText fileText = FileText.fromLines(file, lines);

            // Check if size() returns correct number of lines
            assertEquals(3, fileText.size());

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}