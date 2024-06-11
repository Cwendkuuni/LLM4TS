import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("test.html"));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 100);
        assertNotNull(byteData);
        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}