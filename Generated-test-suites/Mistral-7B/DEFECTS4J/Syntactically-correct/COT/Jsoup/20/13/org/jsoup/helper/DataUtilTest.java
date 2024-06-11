import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://www.example.com";
        DataUtil.load(file, null, baseUri);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
        Validate.notNull(document.outputSettings().charset(), "Charset must be set to the provided invalid charset");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        DataUtil.load(inputStream, null, baseUri);
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, "", baseUri);
        Assert.assertNotNull(document);
        Validate.notNull(document.outputSettings().charset(), "Charset must be set to an empty string");
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, "INVALID_CHARSET", baseUri);
        Assert.assertNotNull(document);
        Validate.notNull(document.outputSettings().charset(), "Charset must be set to the provided invalid charset");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        DataUtil.load(inputStream, "UTF-8", baseUri, null);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithEmptyCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, "", baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, "INVALID_CHARSET", baseUri, parser);
        Assert.assertNotNull(document);
        Validate.notNull(document.outputSettings().charset(), "Charset must be set to the provided invalid charset");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(null);
    }
}