import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions for the loaded data
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions for the loaded data
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions for the loaded data
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions for the parsed data
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "<html></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(data);
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        byte[] result = new byte[byteData.remaining()];
        byteData.get(result);
        assertEquals(data, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}