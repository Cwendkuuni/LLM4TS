
import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    private static final String FILE_PATH = "path/to/file.txt";

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        File file = new File(FILE_PATH);
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test
    public void testConstructorWithFileText() {
        File file = new File(FILE_PATH);
        String charsetName = "UTF-8";
        FileText original = new FileText(file, charsetName);

        FileText copy = new FileText(original);

        assertEquals(original.getFile(), copy.getFile());
        assertEquals(original.getCharset(), copy.getCharset());
        assertEquals(original.getFullText(), copy.getFullText());
        assertArrayEquals(original.toLinesArray(), copy.toLinesArray());
    }

    @Test
    public void testFromFileLines() {
        File file = new File(FILE_PATH);
        List<String> lines = Arrays.asList("line1", "line2", "line3");

        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }

    @Test
    public void testGetLineColumn() {
        FileText fileText = new FileText(new File(FILE_PATH), "UTF-8");

        LineColumn lineColumn = fileText.lineColumn(10);

        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }

    @Test
    public void testGetByLineNo() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(new File(FILE_PATH), lines);

        String line = fileText.get(1);

        assertEquals("line2", line);
    }

    @Test
    public void testGetSize() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = FileText.fromLines(new File(FILE_PATH), lines);

        int size = fileText.size();

        assertEquals(3, size);
    }
}

