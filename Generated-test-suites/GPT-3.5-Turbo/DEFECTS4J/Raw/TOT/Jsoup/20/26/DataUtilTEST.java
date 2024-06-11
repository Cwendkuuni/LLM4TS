import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Typical use case
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);

        // Error scenario - invalid file
        File invalidFile = new File("invalid.html");
        Assert.assertThrows(IOException.class, () -> DataUtil.load(invalidFile, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Typical use case
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);

        // Error scenario - invalid input stream
        InputStream invalidInputStream = new FileInputStream("invalid.html");
        Assert.assertThrows(IOException.class, () -> DataUtil.load(invalidInputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Typical use case
        InputStream inputStream = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);

        // Error scenario - invalid input stream
        InputStream invalidInputStream = new FileInputStream("invalid.xml");
        Assert.assertThrows(IOException.class, () -> DataUtil.load(invalidInputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteData() {
        // Typical use case
        String htmlData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals(htmlData, doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Typical use case
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertTrue(byteData.remaining() > 0);

        // Error scenario - invalid input stream
        InputStream invalidInputStream = new FileInputStream("invalid.html");
        Assert.assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(invalidInputStream));
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        // Edge case - non-standard charset
        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("ISO-8859-1", charset);

        // Edge case - no charset specified
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}