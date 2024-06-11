import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // add assertions to check the Document object
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlData = "html data"; // replace with actual html data
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        // add assertions to check the Document object
    }

    @Test
    public void testLoadFromInputStreamWithAlternateParser() throws IOException {
        String htmlData = "html data"; // replace with actual html data
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser(); // replace with the desired Parser
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        // add assertions to check the Document object
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithNullCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, null, baseUri);
        // no need to add assertions here
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNullInputStream() throws IOException {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        // no need to add assertions here
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String htmlData = "html data"; // replace with actual html data
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlData.getBytes()));
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // add assertions to check the Document object
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String baseUri = "http://example.com";
        String htmlData = "html data containing meta charset=gb2312"; // replace with actual html data
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlData.getBytes()));
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        // add assertions to check the Document object and charsetName
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithEmptyByteBuffer() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(0);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // no need to add assertions here
    }
}