import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html"); // replace with valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null, null);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-16"));
        Document document = DataUtil.load(inputStream, "UTF-16", null);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlContent = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenLoadingFile() throws IOException {
        File file = new File("non-existent-file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenLoadingInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "HTML content here...";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlContent.getBytes()));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // add assertions to check the loaded document
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "HTML content here...";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlContent.getBytes("UTF-16")));
        Document document = DataUtil.parseByteData(byteData, "UTF-16", null, Parser.htmlParser());
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenReadingInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
    }
}