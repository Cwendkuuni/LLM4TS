import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test(expected = IOException.class)
    public void load_fileNotFound_throwsIOException() throws IOException {
        DataUtil.load(new File("nonExistentFile.html"), null, null);
    }

    @Test
    public void load_validFileAndCharset_returnsDocument() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void load_inputStreamAndCharset_returnsDocument() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void load_inputStreamCharsetAndParser_returnsDocument() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void parseByteData_charsetIsNull_returnsDocument() {
        Document doc = DataUtil.parseByteData(null, null, "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void parseByteData_charsetIsUTF8_returnsDocument() {
        Document doc = DataUtil.parseByteData(null, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void readToByteBuffer_validInput_returnsByteBuffer() throws IOException {
        File testFile = new File("test.html");
        FileInputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(1191, byteBuffer.remaining());
    }

    @Test
    public void getCharsetFromContentType_validContentType_returnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_invalidContentType_returnsNull() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void getCharsetFromContentType_emptyContentType_returnsNull() {
        String contentType = "";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}