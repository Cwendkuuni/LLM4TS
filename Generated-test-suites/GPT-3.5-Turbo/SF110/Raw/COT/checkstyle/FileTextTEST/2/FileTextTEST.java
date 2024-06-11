
import static org.junit.Assert.*;
import org.junit.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {
    
    private FileText fileText;
    private final File testFile = new File("test.txt");
    private final String testCharset = "UTF-8";
    private final List<String> testLines = Arrays.asList("Line 1", "Line 2", "Line 3");
    
    @Before
    public void setup() throws IOException {
        FileWriter fileWriter = new FileWriter(testFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (String line : testLines) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        fileText = new FileText(testFile, testCharset);
    }
    
    @After
    public void cleanup() {
        testFile.delete();
    }
    
    @Test
    public void testConstructor_withFileAndCharsetName() throws IOException {
        assertEquals(testFile, fileText.getFile());
        assertEquals(Charset.forName(testCharset), fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText().toString());
        assertArrayEquals(testLines.toArray(new String[0]), fileText.toLinesArray());
    }
    
    @Test
    public void testConstructor_withFileText() {
        FileText copy = new FileText(fileText);
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertEquals(fileText.getFullText(), copy.getFullText());
        assertArrayEquals(fileText.toLinesArray(), copy.toLinesArray());
    }
    
    @Test
    public void testConstructor_withFileAndLines() {
        FileText fromLines = FileText.fromLines(testFile, testLines);
        assertEquals(testFile, fromLines.getFile());
        assertNull(fromLines.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3\n", fromLines.getFullText().toString());
        assertArrayEquals(testLines.toArray(new String[0]), fromLines.toLinesArray());
    }
    
    @Test
    public void testGetFile() {
        assertEquals(testFile, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() {
        assertEquals(Charset.forName(testCharset), fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        assertEquals("Line 1\nLine 2\nLine 3\n", fileText.getFullText().toString());
    }
    
    @Test
    public void testToLinesArray() {
        assertArrayEquals(testLines.toArray(new String[0]), fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        assertEquals(new LineColumn(2, 4), fileText.lineColumn(10));
    }
    
    @Test
    public void testGet() {
        assertEquals("Line 2", fileText.get(1));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNonExistentFile() throws IOException {
        File nonExistentFile = new File("nonexistent.txt");
        new FileText(nonExistentFile, "UTF-8");
    }
    
    @Test
    public void testConstructor_withUnsupportedCharset() {
        try {
            new FileText(testFile, "InvalidCharset");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: InvalidCharset", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testFindLineBreaks() {
        int[] expectedLineBreaks = {0, 7, 14, 18};
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, lineBreaks);
    }
    
    @Test
    public void testFindLineBreaks_withEmptyFileText() throws IOException {
        File emptyFile = new File("empty.txt");
        FileWriter fileWriter = new FileWriter(emptyFile);
        fileWriter.close();
        FileText emptyFileText = new FileText(emptyFile, "UTF-8");
        int[] expectedLineBreaks = {0};
        int[] lineBreaks = emptyFileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, lineBreaks);
        emptyFile.delete();
    }
    
    @Test
    public void testFindLineBreaks_withSingleLineFileText() throws IOException {
        File singleLineFile = new File("single_line.txt");
        FileWriter fileWriter = new FileWriter(singleLineFile);
        fileWriter.write("Single line");
        fileWriter.close();
        FileText singleLineFileText = new FileText(singleLineFile, "UTF-8");
        int[] expectedLineBreaks = {0, 11};
        int[] lineBreaks = singleLineFileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, lineBreaks);
        singleLineFile.delete();
    }

}
