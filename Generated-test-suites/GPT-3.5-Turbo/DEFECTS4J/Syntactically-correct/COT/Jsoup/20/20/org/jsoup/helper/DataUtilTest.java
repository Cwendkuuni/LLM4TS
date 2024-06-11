
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        byte[] bytes = new byte[1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}
