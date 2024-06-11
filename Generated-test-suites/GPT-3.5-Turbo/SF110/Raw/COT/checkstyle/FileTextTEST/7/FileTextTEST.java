import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class FileTextTest {

    @Test
    public void testConstructor() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }
    
    @Test
    public void testConstructor_FileText() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText1 = new FileText(file, lines);
        FileText fileText2 = new FileText(fileText1);
        assertEquals(fileText1.getFile(), fileText2.getFile());
        assertEquals(fileText1.getCharset(), fileText2.getCharset());
        assertEquals(fileText1.getFullText(), fileText2.getFullText());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
    }

    @Test
    public void testFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        StringBuilder fullTextBuilder = new StringBuilder();
        for (String line : lines) {
            fullTextBuilder.append(line).append('\n');
        }
        assertEquals(fullTextBuilder.toString(), fileText.getFullText().toString());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testGetFile() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testGetFullText() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        StringBuilder fullTextBuilder = new StringBuilder();
        for (String line : lines) {
            fullTextBuilder.append(line).append('\n');
        }
        assertEquals(fullTextBuilder.toString(), fileText.getFullText().toString());
    }

    @Test
    public void testToLinesArray() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        LineColumn lineColumn = fileText.lineColumn(7);
        assertEquals(2, lineColumn.getLine());
        assertEquals(3, lineColumn.getColumn());
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals("Line 2", fileText.get(1));
    }

    @Test
    public void testSize() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals(3, fileText.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_FileNotFound() throws IOException {
        File file = new File("nonExistentFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConstructor_UnsupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "InvalidCharset");
    }

    @Test
    public void testConstructor_EmptyFile() throws IOException {
        File file = new File("emptyFile.txt");
        file.createNewFile();
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("", fileText.getFullText().toString());
    }
}