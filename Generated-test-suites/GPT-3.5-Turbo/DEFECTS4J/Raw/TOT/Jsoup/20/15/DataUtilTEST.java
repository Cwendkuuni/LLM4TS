
package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void load_validFile_returnsDocument() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void load_invalidFile_throwsIOException() throws IOException {
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void load_inputStream_returnsDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test
    public void load_inputStreamWithParser_returnsDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void parseByteData_charsetNameNull_returnsDocument() {
        Charset charset = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(null, null, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void parseByteData_charsetNameNotNull_returnsDocument() {
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(null, "UTF-8", baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void readToByteBuffer_validInputStream_returnsByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
    }

    @Test
    public void getCharsetFromContentType_validContentType_returnsCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_invalidContentType_returnsNull() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}
