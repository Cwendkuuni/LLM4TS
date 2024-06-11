import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testLoadFromInputStreamWithAlternateParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(
                "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>"
                        .getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(
                "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>"
                        .getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testReadToByteBufferUnlimitedSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 0;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        Assert.assertNotNull(byteData);
        Assert.assertTrue(byteData.limit() > 0);
        Assert.assertEquals("UTF-8", Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testReadToByteBufferLimitedSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1000;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        Assert.assertNotNull(byteData);
        Assert.assertTrue(byteData.limit() > 0);
        Assert.assertTrue(byteData.limit() <= maxSize);
        Assert.assertEquals("UTF-8", Charset.forName("UTF-8").decode(byteData).toString());
    }

    @Test
    public void testGetCharsetFromContentTypeWithSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUpperCaseCharset() {
        String contentType = "text/html; charset=iso-8859-1";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }
}