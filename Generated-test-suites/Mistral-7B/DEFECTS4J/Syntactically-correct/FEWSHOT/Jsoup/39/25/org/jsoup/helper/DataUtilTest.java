package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    private static final String CHARSET_TEST = "text/html; charset=UTF-8";
    private static final String CHARSET_TEST_INVALID = "text/html; charset=invalidCharset";
    private static final String CHARSET_TEST_EMPTY = "text/html";
    private static final String HTML_CONTENT = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "testBaseUri");
        assertEquals("testBaseUri", doc.baseUri());
        testDocument(doc, HTML_CONTENT);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_CONTENT.getBytes());
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document doc = DataUtil.load(inStream, "UTF-8", "testBaseUri");
        assertEquals("testBaseUri", doc.baseUri());
        testDocument(doc, HTML_CONTENT);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_CONTENT.getBytes());
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        Document doc = DataUtil.load(inStream, "UTF-8", "testBaseUri", Parser.xmlParser());
        assertEquals("testBaseUri", doc.baseUri());
        // testDocument expects htmlParser by default
        testDocument(doc, HTML_CONTENT, Parser.xmlParser());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_CONTENT.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, charsetName, "testBaseUri", Parser.htmlParser());
        assertEquals("testBaseUri", doc.baseUri());
        testDocument(doc, HTML_CONTENT);

        // Test charset detection
        ByteBuffer byteDataInvalid = ByteBuffer.wrap(HTML_CONTENT.getBytes());
        byteDataInvalid.rewind();
        Charset defaultCharset = Charset.forName("UTF-8");
        String docData = DataUtil.parseByteData(byteDataInvalid, null, "testBaseUri", Parser.htmlParser()).html();
        assertEquals(HTML_CONTENT, docData);
        assertEquals("UTF-8", DataUtil.parseByteData(byteDataInvalid, null, "testBaseUri", Parser.htmlParser()).outputSettings().charset());

        ByteArrayOutputStream outStreamInvalid = new ByteArrayOutputStream();
        outStreamInvalid.write("Invalid byte data".getBytes());
        ByteBuffer byteDataInvalidContent = ByteBuffer.wrap(outStreamInvalid.toByteArray());
        Document docInvalid = DataUtil.parseByteData(byteDataInvalidContent, null, "testBaseUri", Parser.htmlParser());
        assertTrue(docInvalid.outputSettings().charset().equals(defaultCharset) || docInvalid.outputSettings().charset().isEmpty());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_CONTENT.getBytes());
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 0);
        assertEquals(HTML_CONTENT.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(HTML_CONTENT.getBytes());
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 10);
        assertEquals(10, byteData.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferException() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(new byte[1]);
        InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        DataUtil.readToByteBuffer(inStream, -1);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(CHARSET_TEST));
        assertNull(DataUtil.getCharsetFromContentType(CHARSET_TEST_INVALID));
        assertNull(DataUtil.getCharsetFromContentType(CHARSET_TEST_EMPTY));
    }

    private void testDocument(Document doc, String htmlContent, Parser parser) {
        assertEquals(htmlContent, doc.html());
        assertEquals(parser, doc.parser());
    }
}