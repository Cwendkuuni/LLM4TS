import org.junit.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import com.google.common.io.*;
import java.util.regex.*;

public class FileTextTest {
    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        File file = new File("testfile.txt");
        String charsetName = "UTF-8";
        fileText = new FileText(file, charsetName);
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("testfile.txt");
        File actualFile = fileText.getFile();
        Assert.assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testGetCharset() {
        Charset expectedCharset = Charset.forName("UTF-8");
        Charset actualCharset = fileText.getCharset();
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetFullText() {
        String expectedFullText = "This is a test file.\nIt contains multiple lines.\n";
        CharSequence actualFullText = fileText.getFullText();
        Assert.assertEquals(expectedFullText, actualFullText.toString());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = {"This is a test file.", "It contains multiple lines.", ""};
        String[] actualLines = fileText.toLinesArray();
        Assert.assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testLineColumn() {
        LineColumn expectedLineColumn = new LineColumn(1, 15);
        LineColumn actualLineColumn = fileText.lineColumn(15);
        Assert.assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test
    public void testGet() {
        String expectedLine = "It contains multiple lines.";
        String actualLine = fileText.get(1);
        Assert.assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testSize() {
        int expectedSize = 3;
        int actualSize = fileText.size();
        Assert.assertEquals(expectedSize, actualSize);
    }
}