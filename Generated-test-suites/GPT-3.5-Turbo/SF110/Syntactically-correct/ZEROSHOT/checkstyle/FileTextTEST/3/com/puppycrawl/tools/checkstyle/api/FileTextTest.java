import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {
    
    private FileText fileText;
    
    @Before
    public void setUp() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("Third line");
        lines.add("Fourth line");
        fileText = FileText.fromLines(file, lines);
    }
    
    @Test
    public void testGetFile() {
        File expectedFile = new File("testFile.txt");
        File actualFile = fileText.getFile();
        assertEquals(expectedFile, actualFile);
    }
    
    @Test
    public void testGetCharset() {
        assertNull(fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() {
        CharSequence expectedFullText = "First line\nSecond line\nThird line\nFourth line\n";
        CharSequence actualFullText = fileText.getFullText();
        assertEquals(expectedFullText, actualFullText);
    }
    
    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"First line", "Second line", "Third line", "Fourth line"};
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLines, actualLines);
    }
    
    @Test
    public void testLineColumn() {
        int pos = 12;
        LineColumn expectedLineColumn = new LineColumn(2, 2);
        LineColumn actualLineColumn = fileText.lineColumn(pos);
        assertEquals(expectedLineColumn, actualLineColumn);
    }
    
    @Test
    public void testGet() {
        String expectedLine = "Third line";
        String actualLine = fileText.get(2);
        assertEquals(expectedLine, actualLine);
    }
    
    @Test
    public void testSize() {
        int expectedSize = 4;
        int actualSize = fileText.size();
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("Third line");
        lines.add("Fourth line");
        FileText expectedFileText = new FileText(file, lines);
        FileText actualFileText = FileText.fromLines(file, lines);
        assertEquals(expectedFileText, actualFileText);
    }
}