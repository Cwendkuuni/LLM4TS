import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test loading a file with default charset.
     */
    @Test
    public void testLoadFileDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "");
        Assert.assertNotNull(doc);
    }

    /**
     * Test loading a file with specified charset.
     */
    @Test
    public void testLoadFileSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-16", "");
        Assert.assertNotNull(doc);
    }

    /**
     * Test loading a file with invalid charset.
     */
    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, "abc123", "");
    }

    /**
     * Test loading an input stream with default charset.
     */
    @Test
    public void testLoadInputStreamDefaultCharset() throws IOException {
        String html = "<html lang='en'><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, "");
        Assert.assertNotNull(doc);
    }

    /**
     * Test loading an input stream with specified charset.
     */
    @Test
    public void testLoadInputStreamSpecifiedCharset() throws IOException {
        String html = "<html lang='en'><head><meta charset='UTF-16'></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, "UTF-16", "");
        Assert.assertNotNull(doc);
    }

    /**
     * Test loading an input stream with invalid charset.
     */
    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String html = "<html lang='en'><head><meta charset='abc123'></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, "abc123", "");
    }

    /**
     * Test loading an input stream with parser.
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html lang='en'><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, "", DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    /**
     * Test reading byte data from an input stream.
     */
    @Test
    public void testReadByteData() throws IOException {
        String html = "<html lang='en'><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        Assert.assertNotNull(byteData);
    }

    /**
     * Test parsing byte data.
     */
    @Test
    public void testParseByteData() {
        String html = "<html lang='en'><head><meta charset='UTF-8'></head><body></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }
}