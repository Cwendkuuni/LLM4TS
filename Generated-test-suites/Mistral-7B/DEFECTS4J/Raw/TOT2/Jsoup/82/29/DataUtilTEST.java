import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FileDocument;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "file:/path/to/test.html";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(input, charsetName, baseUri);
        Assert.assertNotNull(doc);
        input.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        InputStream input = new FileInputStream(new File("test.html"));
        try {
            Document doc = DataUtil.load(input, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            input.close();
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        input.close();
    }

    @Test
    public void testParseInputStreamWithNullInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseInputStream(null, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithEmptyByteBuffer() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseInputStream(byteData.asInputStream(), charsetName, baseUri, Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        InputStream input = new FileInputStream(new File("test.html"));
        try {
            Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            input.close();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        OutputStream output = new FileOutputStream("test_output.html");
        DataUtil.crossStreams(input, output);
        Assert.assertTrue(new File("test_output.html").length() > 0);
        input.close();
        output.close();
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        InputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
        input.close();
    }

    @Test
    public void testValidateCharset() {
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("uTF-8"));
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t"));
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r"));
        Assert.assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r\n"));
        Assert.assertEquals(null, DataUtil.validateCharset("InvalidCharset"));
        Assert.assertEquals(null, DataUtil.validateCharset(""));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);

        contentType = "text/html; charset=invalid-charset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);
    }
}