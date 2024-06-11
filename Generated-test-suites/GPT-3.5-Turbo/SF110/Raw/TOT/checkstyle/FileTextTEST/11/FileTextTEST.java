import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class FileTextTest {

    private final File file = new File("testFile.txt");
    private final String charsetName = "UTF-8";
    private final String[] lines = {"Line 1", "Line 2", "Line 3"};
    private final String fullText = "Line 1\nLine 2\nLine 3";

    @Test
    public void testFileTextConstructorWithFileAndCharsetName() throws IOException {
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText.getFile());
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(charsetName, fileText.getCharset().name());
        assertNotNull(fileText.getFullText());
        assertEquals(fullText, fileText.getFullText());
        assertArrayEquals(lines, fileText.toLinesArray());
    }

    @Test(expected = IOException.class)
    public void testFileTextConstructorWithInvalidFile() throws IOException {
        File invalidFile = new File("invalidFile.txt");
        new FileText(invalidFile, charsetName);
    }

    @Test
    public void testFileTextConstructorWithFileText() {
        FileText originalFileText = new FileText(file, charsetName);
        FileText copiedFileText = new FileText(originalFileText);
        assertNotNull(copiedFileText.getFile());
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertNotNull(copiedFileText.getCharset());
        assertEquals(originalFileText.getCharset().name(), copiedFileText.getCharset().name());
        assertNotNull(copiedFileText.getFullText());
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test
    public void testFromLinesWithList() {
        List<String> lineList = Arrays.asList(lines);
        FileText fileText = FileText.fromLines(file, lineList);
        assertNotNull(fileText.getFile());
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(null, fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals(fullText, fileText.getFullText());
        assertArrayEquals(lines, fileText.toLinesArray());
    }

    @Test
    public void testFromLinesWithFileText() {
        FileText lineFileText = new FileText(file, Arrays.asList(lines));
        FileText fileText = FileText.fromLines(file, lineFileText);
        assertNotNull(fileText.getFile());
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(null, fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals(fullText, fileText.getFullText());
        assertArrayEquals(lines, fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText.getFile());
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText.getCharset());
        assertEquals(charsetName, fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText.getFullText());
        assertEquals(fullText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(file, charsetName);
        assertArrayEquals(lines, fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(file, charsetName);
        int pos = 10;
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(10, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(file, charsetName);
        assertEquals("Line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(file, charsetName);
        assertEquals(3, fileText.size());
    }
}