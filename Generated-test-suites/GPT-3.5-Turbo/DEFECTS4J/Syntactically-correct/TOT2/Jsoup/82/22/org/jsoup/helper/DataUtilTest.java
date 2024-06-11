import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
        // Add assertions to check document parsing
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        // Add assertions to check document parsing
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        // Add assertions to check document parsing
    }

    @Test
    public void testCrossStreams() {
        // Add test for crossStreams method with mocked input and output streams
    }

    @Test
    public void testReadToByteBuffer() {
        // Add test for readToByteBuffer method with mocked input stream and specified maxSize
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Add test for getCharsetFromContentType method with different content types
    }

    @Test
    public void testValidateCharset() {
        // Add test for validateCharset method with known valid and invalid charsets
    }

    @Test
    public void testMimeBoundary() {
        // Add test for mimeBoundary method to check if the generated string is of specified length
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Add test for detectCharsetFromBom method with different BOM patterns
    }
}