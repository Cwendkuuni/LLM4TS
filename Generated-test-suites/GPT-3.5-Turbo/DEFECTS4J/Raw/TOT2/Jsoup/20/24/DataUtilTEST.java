import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] data = "test".getBytes();
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertArrayEquals(data, byteBuffer.array());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

}