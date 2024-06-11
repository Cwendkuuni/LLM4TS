import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("src/test/resources/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-16", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head></head><body></body></html>";
        File file = new File("src/test/resources/test.html");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[bufferSize];
        int read;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        while ((read = inputStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, read);
        }
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outStream.toByteArray()));
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}