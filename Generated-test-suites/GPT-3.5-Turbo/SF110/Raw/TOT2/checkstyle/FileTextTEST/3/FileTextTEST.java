import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class FileTextTest {
    
    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertNotNull(fileText);
    }
    
    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);
        FileText copiedFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFullText(), copiedFileText.getFullText());
    }
    
    @Test
    public void testReadFile() throws IOException {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        String content = FileText.readFile(file, decoder);
        assertNotNull(content);
    }
    
    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText);
    }
    
    @Test
    public void testGetters() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName(charsetName), fileText.getCharset());
        assertEquals("", fileText.getFullText());
        assertArrayEquals(new String[0], fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("line 1", "line 2", "line 3");
        FileText fileText = FileText.fromLines(file, lines);
        LineColumn lc = fileText.lineColumn(8);
        assertEquals(2, lc.getLine());
        assertEquals(2, lc.getColumn());
    }
}