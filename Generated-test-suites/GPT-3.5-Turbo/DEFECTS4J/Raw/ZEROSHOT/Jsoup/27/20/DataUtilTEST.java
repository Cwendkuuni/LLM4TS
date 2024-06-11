import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test case 1
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document document = DataUtil.load(new File("path/to/file"), charsetName, baseUri);
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
        
        // Test case 2
        charsetName = "ISO-8859-1";
        try {
            Document document = DataUtil.load(new File("path/to/anotherfile"), charsetName, baseUri);
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test case 1
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document document = DataUtil.load(new FileInputStream(new File("path/to/file")), charsetName, baseUri);
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
        
        // Test case 2
        charsetName = "ISO-8859-1";
        try {
            Document document = DataUtil.load(new FileInputStream(new File("path/to/anotherfile")), charsetName, baseUri);
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Test case 1
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document document = DataUtil.load(new FileInputStream(new File("path/to/file")), charsetName, baseUri, Parser.htmlParser());
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
        
        // Test case 2
        charsetName = "ISO-8859-1";
        try {
            Document document = DataUtil.load(new FileInputStream(new File("path/to/anotherfile")), charsetName, baseUri, Parser.htmlParser());
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testParseByteData() {
        // Test case 1
        ByteBuffer byteData = ByteBuffer.allocate(1024); // insert test data
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // Assertions...
        
        // Test case 2
        byteData = ByteBuffer.allocate(1024); // insert different test data
        charsetName = "ISO-8859-1";
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // Assertions...
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case 1
        try {
            InputStream inStream = new FileInputStream(new File("path/to/file"));
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
        
        // Test case 2
        try {
            InputStream inStream = new FileInputStream(new File("path/to/anotherfile"));
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            // Assertions...
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case 1
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        
        // Test case 2
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}