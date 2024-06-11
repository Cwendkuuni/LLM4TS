
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
import static org.junit.Assert.*;

public class FileTextTest {

    private FileText fileText;

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);
        
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharsetName() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UnsupportedCharset";
        fileText = new FileText(file, charsetName);
    }

    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);
        FileText newFileText = new FileText(fileText);

        assertEquals(fileText.getFile(), newFileText.getFile());
        assertEquals(fileText.getCharset(), newFileText.getCharset());
        assertEquals(fileText.getFullText(), newFileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), newFileText.toLinesArray());
    }

    @Test
    public void testFromFileLines() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");

        fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);

        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test
    public void testGetFullText() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);

        assertEquals(FileText.readFile(file, Charset.forName(charsetName)).trim(), fileText.getFullText().toString().trim());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");

        fileText = FileText.fromLines(file, lines);

        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);

        int[] lineBreaks = fileText.findLineBreaks();
        assertNotNull(lineBreaks);
        assertEquals(fileText.size() + 1, lineBreaks.length);
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);

        int pos = 10;
        FileText.LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");

        fileText = FileText.fromLines(file, lines);

        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");

        fileText = FileText.fromLines(file, lines);

        assertEquals(lines.size(), fileText.size());
    }
}

