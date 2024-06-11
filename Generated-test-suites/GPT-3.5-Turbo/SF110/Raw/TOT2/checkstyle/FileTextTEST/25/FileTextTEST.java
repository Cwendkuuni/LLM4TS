import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

public class FileTextTest {

    private File testFile = new File("test.txt");

    @Test
    public void testConstructorWithFileAndCharsetName() throws IOException {
        FileText fileText = new FileText(testFile, "UTF-8");
        assertNotNull(fileText);
    }

    @Test
    public void testConstructorWithFileText() {
        FileText originalFileText = new FileText(testFile, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
    }

    @Test
    public void testReadFile() throws IOException {
        FileText fileText = new FileText(testFile, "UTF-8");
        String fullText = fileText.getFullText().toString();
        assertEquals("This is a test file.\nTesting 123.", fullText);
    }

    @Test
    public void testFromFileLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText fileText = FileText.fromLines(testFile, lines);
        assertNotNull(fileText);
    }

    @Test
    public void testGetFile() {
        FileText fileText = new FileText(testFile, "UTF-8");
        assertEquals(testFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(testFile, "UTF-8");
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(testFile, "UTF-8");
        String[] linesArray = fileText.toLinesArray();
        assertEquals("This is a test file.", linesArray[0]);// one line only for this test
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(testFile, "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(1, lineColumn.getLine());
        assertEquals(10, lineColumn.getColumn());
    }

    @Test
    public void testGetWithValidLineNo() {
        FileText fileText = new FileText(testFile, "UTF-8");
        assertEquals("This is a test file.", fileText.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidLineNo() {
        FileText fileText = new FileText(testFile, "UTF-8");
        fileText.get(3);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(testFile, "UTF-8");
        assertEquals(2, fileText.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadFileWithInvalidFile() throws IOException {
        FileText.readFile(new File("invalidFile.txt"), Charset.forName("UTF-8").newDecoder());
    }

}