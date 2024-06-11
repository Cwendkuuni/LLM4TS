
import org.junit.Test;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FileTextTest {

    @Test
    public void testConstructor_withExistingFileAndSupportedCharset() throws Exception {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        // Act
        FileText fileText = new FileText(file, charsetName);

        // Assert
        assertNotNull(fileText.getFile());
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals("", fileText.getFullText().toString());
        assertNotNull(fileText.toLinesArray());
        assertEquals(0, fileText.toLinesArray().length);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_withUnsupportedCharset() throws Exception {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "invalidCharset";

        // Act
        FileText fileText = new FileText(file, charsetName);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNonExistingFile() throws Exception {
        // Arrange
        File file = new File("nonExisting.txt");
        String charsetName = "UTF-8";

        // Act
        FileText fileText = new FileText(file, charsetName);
    }

    @Test
    public void testConstructor_withFileTextObject() {
        // Arrange
        FileText fileText = createMockFileTextObject();

        // Act
        FileText newFileText = new FileText(fileText);

        // Assert
        assertEquals(fileText.getFile(), newFileText.getFile());
        assertEquals(fileText.getCharset(), newFileText.getCharset());
        assertEquals(fileText.getFullText(), newFileText.getFullText());
        assertNotNull(newFileText.toLinesArray());
        assertEquals(fileText.toLinesArray().length, newFileText.toLinesArray().length);
        assertEquals(Arrays.toString(fileText.toLinesArray()), Arrays.toString(newFileText.toLinesArray()));
    }

    @Test
    public void testFromLines_withValidFileAndLinesList() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");

        // Act
        FileText fileText = FileText.fromLines(file, lines);

        // Assert
        assertNotNull(fileText);
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(lines.size(), fileText.size());
        assertEquals(fileText.get(lines.size() - 1), fileText.toLinesArray()[lines.size() - 1]);
    }

    @Test
    public void testFromLines_withFileTextObject() {
        // Arrange
        FileText fileText = createMockFileTextObject();

        // Act
        FileText newFileText = FileText.fromLines(fileText.getFile(), fileText);

        // Assert
        assertEquals(fileText, newFileText);
    }

    @Test
    public void testGetFile() {
        // Arrange
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        // Act
        File returnedFile = fileText.getFile();

        // Assert
        assertEquals(file, returnedFile);
    }

    @Test
    public void testGetCharset() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        // Act
        Charset charset = fileText.getCharset();

        // Assert
        assertEquals(Charset.forName(charsetName), charset);
    }

    @Test
    public void testGetFullText() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        // Act
        CharSequence fullText = fileText.getFullText();

        // Assert
        assertNotNull(fullText);
        assertEquals("", fullText.toString());
    }

    @Test
    public void testToLinesArray() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        // Act
        String[] linesArray = fileText.toLinesArray();

        // Assert
        assertNotNull(linesArray);
        assertEquals(0, linesArray.length);
    }

    @Test
    public void testFindLineBreaks_whenLineBreaksExist() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        // Act
        int[] lineBreaks = fileText.findLineBreaks();

        // Assert
        assertNotNull(lineBreaks);
        assertEquals(lines.size() + 1, lineBreaks.length);
        for (int i = 1; i < lineBreaks.length; i++) {
            assertEquals((i * 6), lineBreaks[i]);
        }
    }

    @Test
    public void testFindLineBreaks_whenNoLineBreaksFound() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1 without line breaks");
        FileText fileText = FileText.fromLines(file, lines);

        // Act
        int[] lineBreaks = fileText.findLineBreaks();

        // Assert
        assertNotNull(lineBreaks);
        assertEquals(lines.size() + 1, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
        assertEquals(lines.get(0).length(), lineBreaks[1]);
    }

    @Test
    public void testLineColumn_withValidPosition() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int position = 10;

        // Act
        LineColumn lineColumn = fileText.lineColumn(position);

        // Assert
        assertNotNull(lineColumn);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(position, lineColumn.getColumnNo());
    }

    @Test
    public void testLineColumn_withInvalidPosition() {
        // Arrange
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int position = 100;

        // Act
        LineColumn lineColumn = fileText.lineColumn(position);

        // Assert
        assertNotNull(lineColumn);
        assertEquals(1, lineColumn.getLineNo());
        assertEquals(position, lineColumn.getColumnNo());
    }

    @Test
    public void testGet_withValidLineNo() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText fileText = FileText.fromLines(file, lines);
        int lineNo = 1;

        // Act
        String line = fileText.get(lineNo);

        // Assert
        assertNotNull(line);
        assertEquals(lines.get(lineNo), line);
    }

    @Test
    public void testGet_withInvalidLineNo() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText fileText = FileText.fromLines(file, lines);
        int lineNo = 5;

        // Act
        String line = fileText.get(lineNo);

        // Assert
        assertNull(line);
    }

    @Test
    public void testSize_withNonEmptyFileText() {
        // Arrange
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        FileText fileText = FileText.fromLines(file, lines);

        // Act
        int size = fileText.size();

        // Assert
        assertEquals(lines.size(), size);
    }

    @Test
    public void testSize_withEmptyFileText() {
        // Arrange
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        // Act
        int size = fileText.size();

        // Assert
        assertEquals(0, size);
    }

    private FileText createMockFileTextObject() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        return FileText.fromLines(file, lines);
    }
}

