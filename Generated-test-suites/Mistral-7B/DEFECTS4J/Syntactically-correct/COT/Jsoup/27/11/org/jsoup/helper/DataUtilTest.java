import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("example.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        try {
            DataUtil.load(file, charsetName, baseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetDetected() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body></body></html>";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, "http://example.com");
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithEmptyCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
        Document document = DataUtil.parseByteData(byteData, "", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body></body></html>";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithContentTypeHeader() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String contentType = "text/html; charset=gb2312";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
        Document document = DataUtil.parseByteData(byteData, contentType, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithContentTypeHeaderAndValidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String contentType = "text/html; charset=UTF-8";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
        Document document = DataUtil.parseByteData(byteData, contentType, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithContentTypeHeaderAndInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String contentType = "text/html; charset=gb2312";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
        Document document = DataUtil.parseByteData(byteData, contentType, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());
    }
}