import org.junit.Test;
import org.jsoup.nodes.Document;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataUtilTest {

    @Test
    public void testLoad_ValidFile_ValidCharset_ValidBaseUri_Success() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(file, charsetName, baseUri);
            // Assert statements
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLoad_ValidInputStream_ValidCharset_ValidBaseUri_Success() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            // Assert statements
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLoad_ValidInputStream_ValidCharset_ValidBaseUri_CustomParser_Success() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "https://example.com";
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            // Assert statements
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testParseByteData_ValidByteBuffer_ValidCharset_ValidBaseUri_ValidParser_Success() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // Assert statements
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream_ValidMaxSize_Success() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            int maxSize = 1000;
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
            // Assert statements
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream_Success() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            // Assert statements
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_CharsertFound_ReturnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Assert statements
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_CharsertNotFound_ReturnsNull() {
        String contentType = "text/html; charset=INVALID-CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Assert statements
    }

    @Test
    public void testGetCharsetFromContentType_NullContentType_ReturnsNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Assert statements
    }

}