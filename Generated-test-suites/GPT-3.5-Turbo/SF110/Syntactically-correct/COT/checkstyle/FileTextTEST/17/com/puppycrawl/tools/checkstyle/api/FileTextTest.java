
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {
    
    @Test
    public void testConstructorWithFileAndCharsetName() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
            // Add additional assertions
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText duplicatedFileText = new FileText(originalFileText);
            assertEquals(originalFileText.getFile(), duplicatedFileText.getFile());
            assertEquals(originalFileText.getCharset(), duplicatedFileText.getCharset());
            assertEquals(originalFileText.getFullText(), duplicatedFileText.getFullText());
            assertArrayEquals(originalFileText.toLinesArray(), duplicatedFileText.toLinesArray());
            // Add additional assertions
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testStaticFromLinesMethodWithFileAndListOfString() {
        File file = new File("test.txt");
        List<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
        // Add additional assertions
    }
    
    @Test
    public void testGetFileMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(file, fileText.getFile());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetCharsetMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(Charset.forName(charsetName), fileText.getCharset());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetFullTextMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(fileText.getFullText(), readFile(file, Charset.forName(charsetName)));
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testToLinesArrayMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertArrayEquals(fileText.toLinesArray(), fileText.toArray());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testLineColumnMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            int[] lineBreakPositions = fileText.findLineBreaks();
            for (int i = 0; i < lineBreakPositions.length - 1; i++) {
                int lineNo = i + 1;
                int startOfLine = lineBreakPositions[i];
                int columnNo = 0;
                
                // Test line start and end
                assertEquals(new LineColumn(lineNo, columnNo), fileText.lineColumn(startOfLine));
                assertEquals(new LineColumn(lineNo, columnNo), fileText.lineColumn(startOfLine + lineText.length() - 1));
                
                // Test middle of the line
                for (int j = startOfLine + 1; j < lineBreakPositions[i + 1]; j++) {
                    assertEquals(new LineColumn(lineNo, ++columnNo), fileText.lineColumn(j));
                }
            }
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            int size = fileText.size();
            for (int i = 0; i < size; i++) {
                assertEquals(fileText.get(i), fileText.toLinesArray()[i]);
            }
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSizeMethod() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(fileText.size(), fileText.toArray().length);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    private static String readFile(File file, Charset charset) throws IOException {
        // Implement the method to read the file
    }
}
