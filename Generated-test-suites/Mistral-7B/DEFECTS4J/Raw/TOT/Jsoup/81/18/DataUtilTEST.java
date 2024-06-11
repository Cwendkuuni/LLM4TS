import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String filePath = "path/to/valid/file.html";
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), expectedCharset, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharsetInHeader() throws IOException {
        String filePath = "path/to/file.html";
        String invalidCharset = "GBK";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), invalidCharset, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), null, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetInHeader() throws IOException {
        String charset = "GBK";
        String inputStreamContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String inputStreamContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        String charset = "UTF-8";
        String bomContent = "\uFEFFtest content";
        InputStream inputStream = new ByteArrayInputStream(bomContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        String filePath = "path/to/non_existent_file.html";
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(new File(filePath), charset, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithIOException() throws IOException {
        String charset = "UTF-8";
        byte[] invalidData = new byte[0];
        InputStream inputStream = new ByteArrayInputStream(invalidData);

        DataUtil.load(inputStream, charset, null);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetInHeader() throws IOException {
        String charset = "GBK";
        String inputStreamContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String inputStreamContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String charset = "UTF-8";
        String bomContent = "\uFEFFtest content";
        InputStream inputStream = new ByteArrayInputStream(bomContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithIOException() throws IOException {
        String charset = "UTF-8";
        byte[] invalidData = new byte[0];
        InputStream inputStream = new ByteArrayInputStream(invalidData);

        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
    }
}