import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileTextTest {

    private File file;
    private String charsetName;
    private FileText fileText;

    @Test
    public void testConstructor() throws IOException {
        file = new File("testFile.txt");
        charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
    }

    @Test
    public void testCopyConstructor() {
        FileText copy = new FileText(fileText);
        assertNotNull(copy);
    }

    @Test
    public void testGetFile() {
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        assertNotNull(fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        assertNotNull(fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        int position = 10;
        assertNotNull(fileText.lineColumn(position));
    }

    @Test
    public void testGet() {
        int lineNo = 1;
        assertNotNull(fileText.get(lineNo));
    }

    @Test
    public void testSize() {
        assertEquals(0, fileText.size());
    }

    @Test(expected = IOException.class)
    public void testInvalidCharset() throws IOException {
        FileText invalidCharsetText = new FileText(file, "InvalidCharset");
    }

    @Test(expected = FileNotFoundException.class)
    public void testNonExistentFile() throws IOException {
        File nonExistentFile = new File("nonExistentFile.txt");
        FileText nonExistentFileText = new FileText(nonExistentFile, charsetName);
    }

    @Test
    public void testFromLines() {
        FileText fromLinesText = FileText.fromLines(file, new ArrayList<String>());
        assertNotNull(fromLinesText);
    }

    @Test
    public void testFindLineBreaks() {
        int[] lineBreaks = fileText.findLineBreaks();
        assertNotNull(lineBreaks);
    }
}