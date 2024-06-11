import org.junit.*;
import java.io.*;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        Assert.assertEquals(file, fileText.getFile());
        Assert.assertEquals(Charset.forName(charsetName), fileText.getCharset());
        
        String expectedText = "This is a test file";
        Assert.assertEquals(expectedText, fileText.getFullText().toString());
        
        String[] expectedLines = {"This is a test file"};
        Assert.assertArrayEquals(expectedLines, fileText.toLinesArray());
    }
    
    @Test
    public void testFileTextCopyConstructor() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        FileText copiedFileText = new FileText(fileText);
        
        Assert.assertEquals(fileText.getFile(), copiedFileText.getFile());
        Assert.assertEquals(fileText.getCharset(), copiedFileText.getCharset());
        Assert.assertEquals(fileText.getFullText(), copiedFileText.getFullText());
        Assert.assertArrayEquals(fileText.toLinesArray(), copiedFileText.toLinesArray());
    }
    
    @Test
    public void testFileTextFromLines() {
        File file = new File("test.txt");
        String[] lines = {"This is a test file"};
        
        FileText fileText = FileText.fromLines(file, java.util.Arrays.asList(lines));
        
        Assert.assertEquals(file, fileText.getFile());
        Assert.assertNull(fileText.getCharset());
        
        String expectedText = "This is a test file\n";
        Assert.assertEquals(expectedText, fileText.getFullText().toString());
        
        Assert.assertArrayEquals(lines, fileText.toLinesArray());
    }
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        Assert.assertEquals(file, fileText.getFile());
    }
    
    @Test
    public void testGetCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        Assert.assertEquals(Charset.forName(charsetName), fileText.getCharset());
    }
    
    @Test
    public void testGetFullText() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String expectedText = "This is a test file";
        Assert.assertEquals(expectedText, fileText.getFullText().toString());
    }
    
    @Test
    public void testToLinesArray() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String[] expectedLines = {"This is a test file"};
        Assert.assertArrayEquals(expectedLines, fileText.toLinesArray());
    }
    
    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        FileText.LineColumn lineColumn = fileText.lineColumn(5);
        
        Assert.assertEquals(1, lineColumn.getLineNo());
        Assert.assertEquals(5, lineColumn.getColumnNo());
    }
    
    @Test
    public void testGet() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        String expectedLine = "This is a test file";
        Assert.assertEquals(expectedLine, fileText.get(0));
    }
    
    @Test
    public void testSize() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        
        Assert.assertEquals(1, fileText.size());
    }
}