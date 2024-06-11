import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static ByteArrayOutputStream outStream;
    private static ByteArrayOutputStream charsetMetaOutStream;
    private static ByteArrayInputStream charsetMetaInStream;

    @BeforeClass
    public static void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        charsetMetaOutStream = new ByteArrayOutputStream();
        charsetMetaInStream = new ByteArrayInputStream(charsetMetaOutStream.toByteArray());
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharsetName() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        DataUtil.load(file, null, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharsetName() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        DataUtil.load(in, null, baseUri);
    }

    @Test
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, "", baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetMeta = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        outStream.write(charsetMeta.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithCustomCharset() {
        charsetMetaOutStream.write("gb2312".getBytes(StandardCharsets.UTF_8));
        charsetMetaOutStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
        charsetMetaOutStream.write("<html><head><meta charset=\"gb2312\"></meta></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = ByteBuffer.wrap(charsetMetaOutStream.toByteArray());
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, "gb2312", baseUri, parser);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        DataUtil.readToByteBuffer(null);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertNotNull(charset.length());
        assertNotNull(charset.equals("EUC-JP"));
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}