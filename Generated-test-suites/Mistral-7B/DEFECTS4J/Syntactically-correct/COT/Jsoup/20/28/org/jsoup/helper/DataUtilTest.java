import org.junit.Assert;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html"; // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        // add assertions here to check the Document object
    }

    @Test
    public void testLoadInputStream() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        String htmlData = "HTML content here..."; // replace with actual HTML data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // add assertions here to check the Document object
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        String htmlData = "HTML content here..."; // replace with actual HTML data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        Parser parser = DataUtil.Parser.htmlParser(); // or use other parsers
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        // add assertions here to check the Document object
    }

    @Test
    public void testParseByteData() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        String htmlData = "HTML content here..."; // replace with actual HTML data
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
        // add assertions here to check the Document object
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputFilePath = "inputFile.txt"; // replace with actual file path
        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', 'I', 'n', 'p', 'u', 't', '\u0003'}; // replace with expected data
        File inputFile = new File(inputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        byteData.rewind();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test(expected = HelperException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        String htmlData = "HTML content here..."; // replace with actual HTML data
        ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());
        } catch (HelperException e) {
            // expected HelperException
        }
        Assert.assertNull(document);
    }
}