import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.io.ByteBufferInputSource;
import org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    private Document defaultDocument;
    private Document customCharsetDocument;
    private Document invalidCharsetDocument;
    private Document bomDocument;
    private Document unsupportedCharsetDocument;

    @Before
    public void setUp() throws IOException {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        defaultDocument = DataUtil.parseString(htmlContent, Parser.htmlParser());

        String htmlContentWithCharset = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        String customCharsetHtmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"></head><body></body></html>";
        customCharsetDocument = DataUtil.parseString(htmlContentWithCharset, Parser.htmlParser());
        ByteArrayInputStream customCharsetStream = new ByteArrayInputStream(customCharsetHtmlContent.getBytes(StandardCharsets.UTF_16));
        unsupportedCharsetDocument = DataUtil.load(customCharsetStream, null, null, Parser.htmlParser());

        String htmlBomContent = "\uFEFF<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        bomDocument = DataUtil.parseString(htmlBomContent, Parser.htmlParser());

        String invalidCharsetHtmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=XYZ\"></head><body></body></html>";
        invalidCharsetDocument = DataUtil.parseString(invalidCharsetHtmlContent, Parser.htmlParser());
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("inputFile.html");
        Document document = DataUtil.load(inputFile, null, null);
        assertEquals(defaultDocument, document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("inputFileWithCustomCharset.html");
        Document document = DataUtil.load(inputFile, "UTF-16", null);
        assertEquals(customCharsetDocument, document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(defaultDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals(defaultDocument, document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(customCharsetDocument.html().getBytes(StandardCharsets.UTF_16));
        Document document = DataUtil.load(inputStream, "UTF-16", null);
        assertEquals(customCharsetDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidInputStream() throws IOException {
        File inputFile = new File("invalidFile.html");
        DataUtil.load(inputFile, null, null);
        fail("IOException expected but not thrown");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(defaultDocument, document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_16));
        Document document = DataUtil.parseByteData(byteData, "UTF-16", null, Parser.htmlParser());
        assertEquals(customCharsetDocument, document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithUnsupportedCharset() {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=XYZ\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseByteData(byteData, "XYZ", null, Parser.htmlParser());
        fail("IOException expected but not thrown");
    }

    @Test
    public void testParseByteDataWithBOM() {
        String htmlContent = "\uFEFF<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(bomDocument, document);
    }

    @Test
    public void testGetCharsetFromContentTypeWithSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=XYZ";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}