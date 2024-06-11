package org.jsoup.helper;

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
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static Document document;

    @BeforeClass
    public static void setUp() throws IOException {
        File file = new File(TEST_FILE);
        document = DataUtil.load(file, "UTF-8", "");
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File(TEST_FILE);
        Document doc = DataUtil.load(file, null, "");
        assertNotNull(doc);
        assertEquals(document, doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File(TEST_FILE);
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        assertEquals(document, doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(testInputStream, null, "");
        assertNotNull(doc);
        assertEquals(document, doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(testInputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals(document, doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(testInputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
        assertNotNull(doc);
        // TODO: Verify the XML parsing result instead of checking for null.
    }

    @Test
    public void testParseByteData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = DataUtil.Parser.htmlParser();

        // Test with empty byte buffer.
        Document doc = DataUtil.parseByteData(ByteBuffer.allocate(0), charsetName, baseUri, parser);
        assertNotNull(doc);

        // Test with byte buffer containing HTML content.
        String htmlContent = "<html><body></body></html>";
        byte[] htmlBytes = htmlContent.getBytes(Charset.forName(charsetName));
        ByteBuffer htmlByteBuffer = ByteBuffer.wrap(htmlBytes);
        doc = DataUtil.parseByteData(htmlByteBuffer, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(document.html(), htmlContent);

        // Test with byte buffer containing HTML content with a specified charset.
        String htmlContentWithCharset = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        byte[] htmlBytesWithCharset = htmlContentWithCharset.getBytes(Charset.forName("UTF-16"));
        ByteBuffer htmlByteBufferWithCharset = ByteBuffer.wrap(htmlBytesWithCharset);
        doc = DataUtil.parseByteData(htmlByteBufferWithCharset, "UTF-16", baseUri, parser);
        assertNotNull(doc);
        assertEquals(document.html(), htmlContentWithCharset);

        // Test with byte buffer containing invalid charset.
        String invalidHtmlContent = "<html><head><meta charset=\"invalidCharset\"></head><body></body></html>";
        byte[] invalidHtmlBytes = invalidHtmlContent.getBytes(Charset.forName(charsetName));
        ByteBuffer invalidHtmlByteBuffer = ByteBuffer.wrap(invalidHtmlBytes);
        doc = DataUtil.parseByteData(invalidHtmlByteBuffer, charsetName, baseUri, parser);
        assertNotNull(doc);
        // TODO: Verify the parser behavior when it encounters an invalid charset.
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"A"});
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteArray = new byte[]{0x41};
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedByteArray);
        assertEquals(expectedByteBuffer, byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
}