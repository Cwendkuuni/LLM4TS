package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil methods
 */
public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String docData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamDocumentWithParser() throws IOException {
        String docData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://localhost";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithNullCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://localhost";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Parser parser = Parser.htmlParser();
        String docData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(docData.getBytes(charsetName));
        ByteBuffer byteData = readToByteBuffer(inputStream);

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharsetName() throws IOException {
        String baseUri = "http://localhost";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);

        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "test.html";
        InputStream inputStream = new FileInputStream(new File(filePath));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    private ByteBuffer readToByteBuffer(InputStream inputStream) throws IOException {
        return DataUtil.readToByteBuffer(inputStream);
    }
}