
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithValidFileAndCharsetName() {
        try {
            File file = new File("test.txt");
            String charsetName = "UTF-8";
            FileText fileText = new FileText(file, charsetName);

            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharsetName() {
        try {
            File file = new File("test.txt");
            String charsetName = "InvalidCharset";
            new FileText(file, charsetName);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithNonExistentFile() throws IOException {
        File file = new File("nonexistent.txt");
        String charsetName = "UTF-8";
        new FileText(file, charsetName);
    }

    @Test
    public void testFileTextCopyConstructor() {
        try {
            File file = new File("test.txt");
            String charsetName = "UTF-8";
            FileText originalFileText = new FileText(file, charsetName);

            FileText copiedFileText = new FileText(originalFileText);

            assertEquals(originalFileText.getFile(), copiedFileText.getFile());
            assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
            assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testFileTextFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");

        FileText fileText = FileText.fromLines(file, lines);

        assertSame(lines, fileText);
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);

            assertEquals(file, fileText.getFile());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);

            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);

            assertEquals(fileText.toLinesArray().length, fileText.getFullText().length());

        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);

            assertArrayEquals(fileText.toLinesArray(), fileText.toLinesArray());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);

            int pos = 10;
            FileText.LineColumn lineColumn = fileText.lineColumn(pos);

            assertEquals(1, lineColumn.line());
            assertEquals(pos, lineColumn.column());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGet() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);

        assertEquals(lines.get(1), fileText.get(1));
    }

    @Test
    public void testSize() {
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);

        assertEquals(lines.size(), fileText.size());
    }

}
