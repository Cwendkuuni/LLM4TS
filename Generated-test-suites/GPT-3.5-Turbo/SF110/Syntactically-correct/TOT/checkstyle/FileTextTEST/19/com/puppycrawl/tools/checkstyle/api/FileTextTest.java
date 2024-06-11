
import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testConstructorWithExistingFileAndValidCharsetName() throws Exception {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);

        assertNotNull(fileText);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithNonExistingFile() throws Exception {
        File file = new File("nonexistingfile.txt");
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithUnsupportedCharsetName() throws Exception {
        File file = new File("testfile.txt");
        String charsetName = "NonExistingCharset";

        FileText fileText = new FileText(file, charsetName);
    }

    @Test
    public void testConstructorWithFileTextObject() throws Exception {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);

        FileText clonedFileText = new FileText(originalFileText);

        assertEquals(originalFileText.getCharset(), clonedFileText.getCharset());
        assertArrayEquals(originalFileText.toLinesArray(), clonedFileText.toLinesArray());
        assertEquals(originalFileText.getFullText(), clonedFileText.getFullText());
        assertEquals(originalFileText.size(), clonedFileText.size());
    }

    @Test
    public void testFromFileLines() {
        File file = new File("testfile.txt");
        List<String> lines = Arrays.asList("line1", "line2", "line3");

        FileText fileText = FileText.fromLines(file, lines);

        assertNotInstanceOf(FileText.class, lines);
        assertNotNull(fileText);
    }

    @Test
    public void testGetFile() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        File retrievedFile = fileText.getFile();

        assertEquals(file, retrievedFile);
    }

    @Test
    public void testGetCharset() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        Charset retrievedCharset = fileText.getCharset();

        assertNotNull(retrievedCharset);
        assertEquals(charsetName, retrievedCharset.name());
    }

    @Test
    public void testGetFullText() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        String expectedFullText = "line1\nline2\nline3";

        CharSequence retrievedFullText = fileText.getFullText();

        assertNotNull(retrievedFullText);
        assertEquals(expectedFullText, retrievedFullText);
    }

    @Test
    public void testToLinesArray() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        String[] expectedLinesArray = {"line1", "line2", "line3"};

        String[] retrievedLinesArray = fileText.toLinesArray();

        assertNotNull(retrievedLinesArray);
        assertArrayEquals(expectedLinesArray, retrievedLinesArray);
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int[] expectedLineBreaks = {0, 6, 12};

        int[] retrievedLineBreaks = fileText.findLineBreaks();

        assertNotNull(retrievedLineBreaks);
        assertArrayEquals(expectedLineBreaks, retrievedLineBreaks);
    }

    @Test
    public void testLineColumnWithValidPositionInFirstLine() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int position = 3;
        LineColumn expectedLineColumn = new LineColumn(1, 3);

        LineColumn retrievedLineColumn = fileText.lineColumn(position);

        assertEquals(expectedLineColumn, retrievedLineColumn);
    }

    @Test
    public void testLineColumnWithValidPositionInLastLine() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int position = 12;
        LineColumn expectedLineColumn = new LineColumn(3, 0);

        LineColumn retrievedLineColumn = fileText.lineColumn(position);

        assertEquals(expectedLineColumn, retrievedLineColumn);
    }

    @Test
    public void testGetWithValidLineNo() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int lineNo = 2;
        String expectedLine = "line2";

        String retrievedLine = fileText.get(lineNo);

        assertEquals(expectedLine, retrievedLine);
    }

    @Test
    public void testSize() {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int expectedSize = 3;

        int retrievedSize = fileText.size();

        assertEquals(expectedSize, retrievedSize);
    }
}

