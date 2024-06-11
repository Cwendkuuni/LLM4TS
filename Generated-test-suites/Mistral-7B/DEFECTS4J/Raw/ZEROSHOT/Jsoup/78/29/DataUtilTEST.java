import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = " InvalidCharset";
        String baseUri = "http://example.com";
        DataUtil.load(new File(filePath), charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithValidCharsetAndParser() throws IOException {
        String charsetName = "UTF-8";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharsetAndParser() throws IOException {
        String charsetName = "InvalidCharset";
        String inputStreamContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "path/to/your/input.txt";
        String outputFilePath = "path/to/your/output.txt";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertTrue(inputFile.length() == outputFile.length());
    }
}