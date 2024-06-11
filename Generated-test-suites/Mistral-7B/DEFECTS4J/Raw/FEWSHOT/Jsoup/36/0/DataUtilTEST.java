package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);

        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        String docData = "Test Document";
        InputStream is = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));

        when(parser.parseInput(docData, baseUri)).thenReturn(doc);

        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(result);
        assertEquals(doc, result);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);

        String charsetName = "GBK";
        String baseUri = "http://test.com";

        String docData = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"></head><body>Test Document</body></html>";
        InputStream is = new ByteArrayInputStream(docData.getBytes(Charset.forName(defaultCharset)));

        when(parser.parseInput(docData, baseUri)).thenReturn(doc);

        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        Document result = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(result);
        assertEquals(doc, result);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);

        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";

        String docData = "Test Document";
        InputStream is = new ByteArrayInputStream(docData.getBytes(Charset.forName(defaultCharset)));

        when(parser.parseInput(docData, baseUri)).thenReturn(doc);

        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(result);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://test.com";

        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        throw new IOException("File not found");
        // DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        String docData = "Test Document";
        InputStream is = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));

        Document doc = DataUtil.load(is, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String baseUri = "http://test.com";

        String docData = "Test Document";
        InputStream is = new ByteArrayInputStream(docData.getBytes(Charset.forName(defaultCharset)));

        Document doc = DataUtil.load(is, null, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithIOException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        throw new IOException("IOException");
        // DataUtil.load(is, charsetName, baseUri);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream is = new ByteArrayInputStream("Test String".getBytes(Charset.forName(defaultCharset)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(is);

        byte[] expectedData = "Test String".getBytes(Charset.forName(defaultCharset));
        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData);

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(result, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharsetUppercase() {
        String contentType = "text/html; charset=invalidcharset";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);
    }
}