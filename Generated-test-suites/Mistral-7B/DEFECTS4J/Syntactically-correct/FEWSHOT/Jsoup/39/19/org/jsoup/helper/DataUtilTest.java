package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data</p>\n" +
                "</body>\n" +
                "</html>";

        byte[] byteData = docData.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);

        String charsetName = null;
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());

        charsetName = "gb2312";
        document = DataUtil.parseByteData(byteBuffer, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());

        charsetName = "UTF-8";
        document = DataUtil.parseByteData(byteBuffer, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());

        charsetName = "invalid-charset";
        document = DataUtil.parseByteData(byteBuffer, charsetName, null, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testLoad() throws IOException {
        String testFilePath = "test.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        File testFile = new File(testFilePath);
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String testFilePath = "test.html";
        String charsetName = "invalid-charset";
        String baseUri = "http://example.com";

        File testFile = new File(testFilePath);
        try {
            Document document = DataUtil.load(testFile, charsetName, baseUri);
            assertNull(document);
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadWithNullCharset() throws IOException {
        String testFilePath = "test.html";
        String charsetName = null;
        String baseUri = "http://example.com";

        File testFile = new File(testFilePath);
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadStream() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "gb2312";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("gb2312", document.outputSettings().charset());

        charsetName = null;
        document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadStreamWithInvalidCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "invalid-charset";
        String baseUri = "http://example.com";

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNull(document);
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadStreamWithNullCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }
}