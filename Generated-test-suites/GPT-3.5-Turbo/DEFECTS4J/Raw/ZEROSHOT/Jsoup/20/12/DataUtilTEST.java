import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(in, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

        assertNotNull(byteData);
        assertTrue(byteData.remaining() > 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
}