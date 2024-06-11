package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.InputElement;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for {@link DataUtil} class.
 */
public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertSame(expectedCharset, charset);

        contentType = "text/html; charset=GBK";
        expectedCharset = "GBK";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertSame(expectedCharset, charset);

        contentType = "text/html"; // null should be returned if charset is not found
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("Some HTML content".getBytes(charsetName));
        Parser parser = mock(Parser.class);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, parser);

        verifyDocument(document);
    }

    @Test
    public void testParseByteDataDetectCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Some HTML content</p></body></html>"
                .getBytes());
        Parser parser = mock(Parser.class);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, parser);

        verifyDocument(document);
        assertEquals(charsetName, "UTF-8");
    }

    @Test
    public void testParseByteDataDetectCharsetInvalidMetaTag() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("<html lang=\"en\"><head><meta charset=\"gb2312\"><meta charset=\"UTF-8\"></head><body><p>Some HTML content</p></body></html>"
                .getBytes());
        Parser parser = mock(Parser.class);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, parser);

        verifyDocument(document);
        assertEquals(charsetName, "UTF-8");
    }

    @Test(expected = IOException.class)
    public void testParseByteDataInvalidCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("<html lang=\"en\"><head><meta charset=\"gb2312\"></head><body><p>Some HTML content</p></body></html>"
                .getBytes());
        Parser parser = mock(Parser.class);

        DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), charsetName, baseUri, parser);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, null, baseUri);

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Some HTML content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Some HTML content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, null, baseUri);

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"></head><body><p>Some HTML content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(htmlContent, baseUri)).thenReturn(new Document());

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals(charsetName, document.outputSettings().charset());
        verify(parser).parseInput(htmlContent, baseUri);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] inputBytes = "Some HTML content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteBuffer expectedByteData = ByteBuffer.wrap(inputBytes);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertArrayEquals(expectedByteData.array(), byteData.array());
    }

    private void verifyDocument(Document document) {
        assertNotNull(document);
        assertNotNull(document.body());
        assertNotNull(document.body().text());
        assertEquals("Some HTML content", document.body().text());
    }

    private void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected object to be not null.");
        }
    }
}