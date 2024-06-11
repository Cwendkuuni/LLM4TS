import org.junit.*;
import java.nio.charset.*;
import java.io.*;
import java.util.*;

public class FileTextTest {

    private FileText fileText;
    private File testFile;
    
    @Before
    public void setUp() throws IOException {
        testFile = new File("test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Line1");
        bw.newLine();
        bw.write("Line2");
        bw.newLine();
        bw.write("Line3");
        bw.newLine();
        bw.close();
        
        fileText = new FileText(testFile, "UTF-8");
    }
    
    @After
    public void tearDown() {
        testFile.delete();
    }
    
    @Test
    public void testConstructor() {
        Assert.assertNotNull(fileText);
        Assert.assertEquals(testFile, fileText.getFile());
        Assert.assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        Assert.assertEquals("Line1\nLine2\nLine3\n", fileText.getFullText().toString());
        Assert.assertArrayEquals(new String[]{"Line1", "Line2", "Line3"}, fileText.toLinesArray());
        Assert.assertEquals(3, fileText.size());
    }
    
    @Test
    public void testLineColumn() {
        Assert.assertEquals(new FileText.LineColumn(1, 0), fileText.lineColumn(0));
        Assert.assertEquals(new FileText.LineColumn(3, 0), fileText.lineColumn(10));
        Assert.assertEquals(new FileText.LineColumn(2, 2), fileText.lineColumn(5));
    }
    
    @Test
    public void testGet() {
        Assert.assertEquals("Line1", fileText.get(0));
        Assert.assertEquals("Line2", fileText.get(1));
        Assert.assertEquals("Line3", fileText.get(2));
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(3, fileText.size());
    }
    
    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("Line4", "Line5");
        FileText newText = FileText.fromLines(testFile, lines);
        Assert.assertNotNull(newText);
        Assert.assertEquals(testFile, newText.getFile());
        Assert.assertNull(newText.getCharset());
        Assert.assertEquals("Line4\nLine5\n", newText.getFullText().toString());
        Assert.assertArrayEquals(new String[]{"Line4", "Line5"}, newText.toLinesArray());
        Assert.assertEquals(2, newText.size());
    }
}