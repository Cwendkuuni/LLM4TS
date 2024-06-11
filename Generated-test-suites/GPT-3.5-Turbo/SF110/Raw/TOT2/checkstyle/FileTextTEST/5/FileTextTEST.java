import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.After;
import com.google.common.io.Closeables;

public class FileTextTest {
    
    private File file;
    private String charsetName;
    private FileText fileText;
    
    @Before
    public void setUp() {
        file = new File("testFile.txt");
        charsetName = Charset.defaultCharset().name();
    }
    
    @After
    public void tearDown() {
        file = null;
        charsetName = null;
        fileText = null;
    }
    
    @Test
    public void testConstructor() throws IOException {
        fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test
    public void testCopyConstructor() {
        FileText original = new FileText(file, charsetName);
        fileText = new FileText(original);
        assertNotNull(fileText);
        assertEquals(original.getFile(), fileText.getFile());
        assertEquals(original.getCharset(), fileText.getCharset());
        assertArrayEquals(original.toLinesArray(), fileText.toLinesArray());
    }
    
    @Test
    public void testReadFile() throws IOException {
        String content = "Test content";
        Closeables.closeQuietly(new StringReader(content));
        assertEquals(content, FileText.readFile(file, Charset.defaultCharset().newDecoder()));
    }
    
    @Test
    public void testFromLines() {
        List<String> lines = new ArrayList<>();
        lines.add("Line1");
        lines.add("Line2");
        fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        fileText = new FileText(file, charsetName);
        assertEquals(new LineColumn(1, 4), fileText.lineColumn(4));
    }
    
    @Test
    public void testGet() {
        List<String> lines = new ArrayList<>();
        lines.add("Line1");
        lines.add("Line2");
        fileText = FileText.fromLines(file, lines);
        assertEquals("Line1", fileText.get(0));
    }
    
    @Test
    public void testSize() {
        List<String> lines = new ArrayList<>();
        lines.add("Line1");
        lines.add("Line2");
        fileText = FileText.fromLines(file, lines);
        assertEquals(2, fileText.size());
    }
}