import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document actualDocument = DataUtil.load(file, null, baseUri);
        Document expectedDocument = // create expected Document object here
        Assert.assertEquals("Document objects are not equal", expectedDocument, actualDocument);

        // check charset
        String actualCharset = actualDocument.outputSettings().charset();
        Assert.assertEquals("Unexpected charset", expectedCharset, actualCharset);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document actualDocument = DataUtil.load(file, charsetName, baseUri);
        Document expectedDocument = // create expected Document object here
        Assert.assertEquals("Document objects are not equal", expectedDocument, actualDocument);

        // check charset
        String actualCharset = actualDocument.outputSettings().charset();
        Assert.assertEquals("Unexpected charset", charsetName, actualCharset);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullInputStream() {
        File file = new File("test.html"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(null, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharsetName() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String charsetName = "XYZ";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlText = "..."; // replace with actual HTML text
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlText.getBytes());
        Document actualDocument = DataUtil.load(inputStream, null, null);
        Document expectedDocument = // create expected Document object here
        Assert.assertEquals("Document objects are not equal", expectedDocument, actualDocument);
    }

    // ... generate similar test cases for other methods in the DataUtil class

    @Test
    public void testParseByteDataWithDefaultCharset() {
        // ... generate test case for parseByteData with default charset
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        // ... generate test case for parseByteData with specified charset
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithNullByteData() {
        // ... generate test case for parseByteData with null ByteBuffer
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullCharsetName() {
        // ... generate test case for parseByteData with null charsetName
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithEmptyCharsetName() {
        // ... generate test case for parseByteData with empty charsetName
    }

    // ... add more test cases as needed

}