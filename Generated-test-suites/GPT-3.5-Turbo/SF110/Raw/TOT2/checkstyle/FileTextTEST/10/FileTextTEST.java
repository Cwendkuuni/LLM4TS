import static org.junit.Assert.*;
import org.junit.Test;

public class FileTextTest {
    
    @Test
    public void testFileTextConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertNotNull(fileText);
        } catch (IOException e) {
            fail("Exception thrown when creating FileText object");
        }
    }
    
    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText originalFileText = new FileText(file, charsetName);
            FileText copyFileText = new FileText(originalFileText);
            assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
        } catch (IOException e) {
            fail("Exception thrown when creating FileText object");
        }
    }
    
    @Test
    public void testReadFile() {
        File file = new File("test.txt");
        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        try {
            String fileContent = FileText.readFile(file, decoder);
            assertNotNull(fileContent);
        } catch (IOException e) {
            fail("Exception thrown when reading file content");
        }
    }
    
    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            LineColumn lineColumn = fileText.lineColumn(10);
            assertNotNull(lineColumn);
        } catch (IOException e) {
            fail("Exception thrown when creating FileText object");
        }
    }
    
    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            int[] lineBreaks = fileText.findLineBreaks();
            assertNotNull(lineBreaks);
        } catch (IOException e) {
            fail("Exception thrown when creating FileText object");
        }
    }
    
    @Test
    public void testGet() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals("Line 1", fileText.get(0));
        } catch (IOException e) {
            fail("Exception thrown when creating FileText object");
        }
    }
    
    @Test
    public void testSize() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        try {
            FileText fileText = new FileText(file, charsetName);
            assertEquals(3, fileText.size());
        } catch (IOException e) {
            fail("Exception thrown when creating FileText object");
        }
    }
}