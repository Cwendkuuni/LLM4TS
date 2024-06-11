import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);

        Document doc = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        String filePath = "path/to/nonexistent/file.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testParseByteDataWithValidData() {
        String htmlData = "<html><head><meta charset='UTF-8'></head><body>Test Data</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Data", doc.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlData = "<html><head><meta charset='ISO-8859-1'></head><body>Test Data</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Data", doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        DataUtil.readToByteBuffer(inputStream);
    }

}