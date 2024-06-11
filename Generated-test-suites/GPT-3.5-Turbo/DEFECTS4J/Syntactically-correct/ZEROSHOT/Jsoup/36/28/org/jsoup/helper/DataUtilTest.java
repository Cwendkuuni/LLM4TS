import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}