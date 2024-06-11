import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    private File testFile;
    private String testText;
    private FileText fileText;

    @Before
    public void setUp() {
        testFile = new File("testfile.txt");
        testText = "Test line 1\nTest line 2\nTest line 3";
        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write(testText);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileText = new FileText(testFile, StandardCharsets.UTF_8.name());
    }

    @Test
    public void testGetFile() {
        assertEquals(testFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertEquals(testText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = new String[]{"Test line 1", "Test line 2", "Test line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        assertEquals(new LineColumn(2, 4), fileText.lineColumn(10));
        assertEquals(new LineColumn(3, 0), fileText.lineColumn(17));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        fileText.get(5);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithNonExistentFile() {
        File nonExistentFile = new File("nonexistent.txt");
        new FileText(nonExistentFile, StandardCharsets.UTF_8.name());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() {
        File unsupportedCharsetFile = new File("testfile.txt");
        new FileText(unsupportedCharsetFile, "InvalidCharset");
    }

}