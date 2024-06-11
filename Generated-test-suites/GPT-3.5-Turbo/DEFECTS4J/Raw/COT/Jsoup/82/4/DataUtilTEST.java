
import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoad_File() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // Assert the functionality of the method
        // ...
    }

    @Test
    public void testLoad_InputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        // Assert the functionality of the method
        // ...
    }

    @Test
    public void testLoad_InputStream_Parser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        // Assert the functionality of the method
        // ...
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        // Assert the functionality of the method
        // ...
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = new FileInputStream("input.txt");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, parser);
        // Assert the functionality of the method
        // ...
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 1024;
        ByteBuffer buffer = DataUtil.readToByteBuffer(inStream, maxSize);
        // Assert the functionality of the method
        // ...
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Assert the functionality of the method
        // ...
    }

    // Additional test cases for potential edge cases and exceptions

    @Test
    public void testLoad_File_Exception() {
        // Test case for IOException
        // ...
    }

    @Test
    public void testLoad_InputStream_Exception() {
        // Test case for IOException
        // ...
    }

    @Test
    public void testLoad_InputStream_Parser_Exception() {
        // Test case for IOException
        // ...
    }

    @Test
    public void testCrossStreams_Exception() {
        // Test case for IOException
        // ...
    }

    @Test
    public void testParseInputStream_Exception() {
        // Test case for IOException
        // ...
    }

    @Test
    public void testReadToByteBuffer_Exception() {
        // Test case for IOException
        // ...
    }

    @Test
    public void testGetCharsetFromContentType_Exception() {
        // Test case for potential exceptions
        // ...
    }
}

